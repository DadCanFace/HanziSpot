package com.hanzispot.game

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*
import java.util.Locale
import kotlin.random.Random

class MainActivity : Activity() {
    private data class PairWord(val traditional: String, val simplified: String, val pinyin: String, val meaning: String)
    private val words = listOf(
        PairWord("學", "学", "xué", "to learn"), PairWord("書", "书", "shū", "book"),
        PairWord("國", "国", "guó", "country"), PairWord("門", "门", "mén", "door"),
        PairWord("馬", "马", "mǎ", "horse"), PairWord("鳥", "鸟", "niǎo", "bird"),
        PairWord("魚", "鱼", "yú", "fish"), PairWord("車", "车", "chē", "vehicle"),
        PairWord("雲", "云", "yún", "cloud"), PairWord("電", "电", "diàn", "electricity"),
        PairWord("風", "风", "fēng", "wind"), PairWord("雨", "雨", "yǔ", "rain"),
        PairWord("長", "长", "cháng", "long"), PairWord("東", "东", "dōng", "east"),
        PairWord("見", "见", "jiàn", "to see"), PairWord("聽", "听", "tīng", "to listen"),
        PairWord("說", "说", "shuō", "to speak"), PairWord("語", "语", "yǔ", "language"),
        PairWord("愛", "爱", "ài", "love"), PairWord("樂", "乐", "lè", "joy"),
        PairWord("時", "时", "shí", "time"), PairWord("間", "间", "jiān", "between"),
        PairWord("開", "开", "kāi", "to open"), PairWord("關", "关", "guān", "to close"),
        PairWord("體", "体", "tǐ", "body"), PairWord("頭", "头", "tóu", "head"),
        PairWord("貓", "猫", "māo", "cat"), PairWord("龍", "龙", "lóng", "dragon"),
        PairWord("葉", "叶", "yè", "leaf"), PairWord("樹", "树", "shù", "tree"),
        PairWord("買", "买", "mǎi", "to buy"), PairWord("賣", "卖", "mài", "to sell")
    ).filter { it.traditional != it.simplified }
    private val ink = Color.rgb(28, 49, 48)
    private val teal = Color.rgb(22, 125, 115)
    private val paper = Color.rgb(247, 244, 237)
    private var side = 3
    private var order = intArrayOf()
    private var round = 0
    private var score = 0
    private var lives = 3
    private var target = 0
    private var solved = false
    private var hinted = false
    private var playing = false
    private var mistakes = mutableSetOf<Int>()
    private lateinit var root: LinearLayout
    private val prefs by lazy { getSharedPreferences("records", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            side = it.getInt("side", 3); order = it.getIntArray("order") ?: intArrayOf()
            round = it.getInt("round"); score = it.getInt("score"); lives = it.getInt("lives", 3)
            target = it.getInt("target"); solved = it.getBoolean("solved")
            hinted = it.getBoolean("hinted"); playing = it.getBoolean("playing")
            mistakes = (it.getIntArray("mistakes") ?: intArrayOf()).toMutableSet()
        }
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("side", side); outState.putIntArray("order", order)
        outState.putInt("round", round); outState.putInt("score", score); outState.putInt("lives", lives)
        outState.putInt("target", target); outState.putBoolean("solved", solved)
        outState.putBoolean("hinted", hinted); outState.putBoolean("playing", playing)
        outState.putIntArray("mistakes", mistakes.toIntArray())
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun shape(color: Int, stroke: Int = color) = GradientDrawable().apply {
        setColor(color); cornerRadius = dp(18).toFloat(); setStroke(dp(1), stroke)
    }
    private fun label(text: String, size: Float = 16f, color: Int = ink): TextView = TextView(this).apply {
        this.text = text; textSize = size; setTextColor(color); gravity = Gravity.CENTER
        setPadding(0, dp(8), 0, dp(8))
    }
    private fun addText(text: String, size: Float = 16f, color: Int = ink) {
        root.addView(label(text, size, color))
    }
    private fun button(text: String, action: () -> Unit) {
        root.addView(Button(this).apply {
            this.text = text; isAllCaps = false; textSize = 16f; setTextColor(Color.WHITE)
            background = shape(teal); setPadding(dp(12), dp(10), dp(12), dp(10))
            setOnClickListener { action() }
        }, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(12) })
    }
    private fun start(size: Int) {
        side = size; order = words.indices.shuffled().take(10).toIntArray()
        round = 0; score = 0; lives = 3; playing = true
        newBoard(); render()
    }
    private fun newBoard() {
        target = Random.nextInt(side * side); solved = false; hinted = false; mistakes.clear()
    }
    private fun render() {
        val scroll = ScrollView(this).apply { setBackgroundColor(paper); isFillViewport = true }
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(24), dp(24), dp(28))
        }
        scroll.addView(root)
        setContentView(scroll)
        // Respect system bars on devices with enforced edge-to-edge display.
        scroll.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop,
                insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets
        }
        scroll.requestApplyInsets()
        addText("漢  /  汉", 36f, teal)
        addText("HANZI SPOT", 13f, teal)
        if (!playing) { home(); return }
        if (lives <= 0 || round >= order.size) { results(); return }
        board()
    }
    private fun home() {
        addText("A small difference.\nA whole new character.", 28f)
        addText("Find the one character written in the other Chinese script. Tap it, then learn the pair.")
        addText("10 rounds · 3 hearts · no time limit", 14f, teal)
        button("Easy  ·  3 × 3") { start(3) }
        button("Medium  ·  4 × 4") { start(4) }
        button("Hard  ·  5 × 5") { start(5) }
        addText("Best scores", 18f)
        addText((3..5).joinToString("    ") { "${it}×$it: ${prefs.getInt("best$it", 0)}" }, 14f)
        addText("Traditional 繁體 · Simplified 简体\nBoth are valid writing systems. This game practices visual recognition of individual characters.", 13f)
    }
    private fun board() {
        val word = words[order[round]]
        val findTraditional = round % 2 == 0
        addText("ROUND ${round + 1} / 10     •     $score PTS     •     ${"♥".repeat(lives)}", 13f, teal)
        addText(if (findTraditional) "Find the Traditional one" else "Find the Simplified one", 25f)
        addText(if (findTraditional) "找出繁體字 · Tap the different character" else "找出简体字 · Tap the different character", 14f)
        val grid = GridLayout(this).apply { columnCount = side; rowCount = side }
        root.addView(grid, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16) })
        val available = resources.displayMetrics.widthPixels - dp(48)
        val cellHeight = (available / side).coerceAtMost(dp(88))
        for (i in 0 until side * side) {
            val special = i == target
            val text = if (special == findTraditional) word.traditional else word.simplified
            val tile = Button(this).apply {
                this.text = text; textSize = if (side == 5) 27f else 34f
                typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                textLocale = if (text == word.traditional) Locale.TRADITIONAL_CHINESE else Locale.SIMPLIFIED_CHINESE
                isAllCaps = false; setTextColor(ink); setPadding(0, 0, 0, 0)
                background = shape(when {
                    solved && special -> Color.rgb(184, 225, 209)
                    i in mistakes -> Color.rgb(248, 213, 204)
                    hinted && special -> Color.rgb(249, 225, 155)
                    else -> Color.WHITE
                }, Color.rgb(222, 225, 216))
                contentDescription = "Row ${i / side + 1}, column ${i % side + 1}: $text"
                isEnabled = !solved && i !in mistakes
                setOnClickListener {
                    if (special) {
                        solved = true; score += if (hinted) 5 else 10
                    } else { mistakes.add(i); lives-- }
                    render()
                }
            }
            grid.addView(tile, GridLayout.LayoutParams().apply {
                width = 0; height = cellHeight; columnSpec = GridLayout.spec(i % side, 1f)
                rowSpec = GridLayout.spec(i / side); setMargins(dp(3), dp(3), dp(3), dp(3))
            })
        }
        if (solved) {
            addText("You found it!  +${if (hinted) 5 else 10}", 20f, teal)
            addText("${word.traditional}  →  ${word.simplified}", 32f)
            addText("Traditional  →  Simplified\n${word.pinyin} · ${word.meaning}")
            button(if (round == 9) "See results" else "Next character  →") { round++; newBoard(); render() }
        } else {
            addText(if (mistakes.isEmpty()) "Look closely at the strokes." else "That is the majority script. Try another tile.", 14f)
            if (!hinted) button("Hint  ·  earn 5 instead of 10 points") { hinted = true; render() }
            else addText("Hint: look for the golden tile.", 14f, teal)
        }
        button("Back to menu") {
            android.app.AlertDialog.Builder(this).setTitle("Leave this game?")
                .setMessage("Your current round and score will be lost.")
                .setNegativeButton("Keep playing", null)
                .setPositiveButton("Leave") { _, _ -> playing = false; render() }.show()
        }
    }
    private fun results() {
        val best = maxOf(score, prefs.getInt("best$side", 0))
        prefs.edit().putInt("best$side", best).apply()
        addText(if (lives > 0) "Beautifully spotted." else "Keep your eyes curious.", 28f)
        addText("$score", 64f, teal)
        addText("POINTS · ${side} × $side", 14f)
        addText("${round + if (solved && round < 10) 1 else 0} / 10 pairs found\nBest at this difficulty: $best")
        if (lives <= 0 && round < order.size) {
            val word = words[order[round]]
            addText("The last pair was ${word.traditional} → ${word.simplified}\n${word.pinyin} · ${word.meaning}")
        }
        button("Play again") { start(side) }
        button("Choose difficulty") { playing = false; render() }
    }
}
