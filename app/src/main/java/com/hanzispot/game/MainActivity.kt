package com.hanzispot.game

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
        PairWord("買", "买", "mǎi", "to buy"), PairWord("賣", "卖", "mài", "to sell"),
        PairWord("鐘", "钟", "zhōng", "clock"),
        PairWord("鏡", "镜", "jìng", "mirror"),
        PairWord("燈", "灯", "dēng", "lamp"),
        PairWord("傘", "伞", "sǎn", "umbrella"),
        PairWord("紙", "纸", "zhǐ", "paper"),
        PairWord("筆", "笔", "bǐ", "pen"),
        PairWord("畫", "画", "huà", "painting"),
        PairWord("頁", "页", "yè", "page"),
        PairWord("線", "线", "xiàn", "thread"),
        PairWord("網", "网", "wǎng", "net"),
        PairWord("繩", "绳", "shéng", "rope"),
        PairWord("籃", "篮", "lán", "basket"),
        PairWord("盤", "盘", "pán", "plate"),
        PairWord("鍋", "锅", "guō", "pot"),
        PairWord("鑰", "钥", "yào", "key"),
        PairWord("鎖", "锁", "suǒ", "lock"),
        PairWord("鐵", "铁", "tiě", "iron"),
        PairWord("銅", "铜", "tóng", "copper"),
        PairWord("銀", "银", "yín", "silver"),
        PairWord("錢", "钱", "qián", "money"),
        PairWord("橋", "桥", "qiáo", "bridge"),
        PairWord("樓", "楼", "lóu", "building"),
        PairWord("園", "园", "yuán", "garden"),
        PairWord("島", "岛", "dǎo", "island"),
        PairWord("灣", "湾", "wān", "bay"),
        PairWord("灘", "滩", "tān", "beach"),
        PairWord("嶺", "岭", "lǐng", "mountain ridge"),
        PairWord("霧", "雾", "wù", "fog"),
        PairWord("塵", "尘", "chén", "dust"),
        PairWord("煙", "烟", "yān", "smoke"),
        PairWord("陽", "阳", "yáng", "sun"),
        PairWord("陰", "阴", "yīn", "shade"),
        PairWord("蟲", "虫", "chóng", "insect"),
        PairWord("蝦", "虾", "xiā", "shrimp"),
        PairWord("龜", "龟", "guī", "turtle"),
        PairWord("鴨", "鸭", "yā", "duck"),
        PairWord("鵝", "鹅", "é", "goose"),
        PairWord("雞", "鸡", "jī", "chicken"),
        PairWord("豬", "猪", "zhū", "pig"),
        PairWord("獅", "狮", "shī", "lion"),
        PairWord("獵", "猎", "liè", "to hunt"),
        PairWord("獸", "兽", "shòu", "beast"),
        PairWord("驢", "驴", "lǘ", "donkey"),
        PairWord("駱", "骆", "luò", "camel (as in 駱駝)"),
        PairWord("蘋", "苹", "píng", "apple (as in 蘋果)"),
        PairWord("蘿", "萝", "luó", "radish (as in 蘿蔔)"),
        PairWord("蔥", "葱", "cōng", "scallion"),
        PairWord("麥", "麦", "mài", "wheat"),
        PairWord("麵", "面", "miàn", "flour or noodles"),
        PairWord("餅", "饼", "bǐng", "flatbread"),
        PairWord("飯", "饭", "fàn", "cooked rice"),
        PairWord("飽", "饱", "bǎo", "full after eating"),
        PairWord("餓", "饿", "è", "hungry"),
        PairWord("飲", "饮", "yǐn", "to drink"),
        PairWord("湯", "汤", "tāng", "soup"),
        PairWord("鹽", "盐", "yán", "salt"),
        PairWord("藥", "药", "yào", "medicine"),
        PairWord("醫", "医", "yī", "medicine or doctor"),
        PairWord("膚", "肤", "fū", "skin"),
        PairWord("齒", "齿", "chǐ", "tooth"),
        PairWord("齡", "龄", "líng", "age"),
        PairWord("髮", "发", "fà", "hair"),
        PairWord("臉", "脸", "liǎn", "face"),
        PairWord("腳", "脚", "jiǎo", "foot"),
        PairWord("腦", "脑", "nǎo", "brain"),
        PairWord("聲", "声", "shēng", "sound"),
        PairWord("響", "响", "xiǎng", "to sound"),
        PairWord("讀", "读", "dú", "to read"),
        PairWord("寫", "写", "xiě", "to write"),
        PairWord("問", "问", "wèn", "to ask"),
        PairWord("認", "认", "rèn", "to recognize"),
        PairWord("識", "识", "shí", "to know"),
        PairWord("記", "记", "jì", "to remember"),
        PairWord("講", "讲", "jiǎng", "to talk"),
        PairWord("請", "请", "qǐng", "please"),
        PairWord("謝", "谢", "xiè", "to thank"),
        PairWord("試", "试", "shì", "to try"),
        PairWord("課", "课", "kè", "lesson"),
        PairWord("練", "练", "liàn", "to practice"),
        PairWord("習", "习", "xí", "to study"),
        PairWord("親", "亲", "qīn", "close or related"),
        PairWord("遠", "远", "yuǎn", "far"),
        PairWord("進", "进", "jìn", "to enter"),
        PairWord("過", "过", "guò", "to pass"),
        PairWord("運", "运", "yùn", "to transport"),
        PairWord("轉", "转", "zhuǎn", "to turn"),
        PairWord("選", "选", "xuǎn", "to choose"),
        PairWord("達", "达", "dá", "to reach"),
        PairWord("歸", "归", "guī", "to return"),
        PairWord("離", "离", "lí", "to leave"),
        PairWord("雙", "双", "shuāng", "pair"),
        PairWord("單", "单", "dān", "single"),
        PairWord("萬", "万", "wàn", "ten thousand"),
        PairWord("億", "亿", "yì", "one hundred million"),
        PairWord("幾", "几", "jǐ", "how many"),
        PairWord("個", "个", "gè", "general measure word"),
        PairWord("總", "总", "zǒng", "total"),
        PairWord("點", "点", "diǎn", "dot or point"),
        PairWord("紅", "红", "hóng", "red"),
        PairWord("綠", "绿", "lǜ", "green"),
        PairWord("藍", "蓝", "lán", "blue"),
        PairWord("黃", "黄", "huáng", "yellow"),
        PairWord("輕", "轻", "qīng", "light in weight"),
        PairWord("難", "难", "nán", "difficult"),
        PairWord("簡", "简", "jiǎn", "simple"),
        PairWord("複", "复", "fù", "to repeat"),
        PairWord("舊", "旧", "jiù", "old"),
        PairWord("歲", "岁", "suì", "year of age"),
        PairWord("夢", "梦", "mèng", "dream"),
        PairWord("驚", "惊", "jīng", "startled"),
        PairWord("憶", "忆", "yì", "to recall"),
        PairWord("願", "愿", "yuàn", "wish"),
        PairWord("慶", "庆", "qìng", "to celebrate"),
        PairWord("歡", "欢", "huān", "joy"),
        PairWord("戲", "戏", "xì", "play or drama"),
        PairWord("觀", "观", "guān", "to observe"),
        PairWord("現", "现", "xiàn", "to appear"),
        PairWord("實", "实", "shí", "real"),
        PairWord("廣", "广", "guǎng", "wide"),
        PairWord("農", "农", "nóng", "agriculture"),
        PairWord("業", "业", "yè", "occupation"),
        PairWord("師", "师", "shī", "teacher"),
        PairWord("團", "团", "tuán", "group"),
        PairWord("隊", "队", "duì", "team"),
        PairWord("號", "号", "hào", "number")
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
    private var speech: TextToSpeech? = null
    private var speechStatus: Int? = null
    private var activeUtterance: String? = null
    private var utteranceNumber = 0
    private val prefs by lazy { getSharedPreferences("records", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speech = TextToSpeech(this) { status ->
            Handler(Looper.getMainLooper()).post {
                if (isDestroyed) return@post
                speechStatus = status
                if (status == TextToSpeech.SUCCESS) {
                    speech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) = Unit
                        override fun onDone(utteranceId: String?) = Unit
                        @Deprecated("Required by UtteranceProgressListener")
                        override fun onError(utteranceId: String?) = reportSpeechError(utteranceId)
                        override fun onError(utteranceId: String?, errorCode: Int) =
                            reportSpeechError(utteranceId)
                    })
                }
            }
        }
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
        stopSpeech()
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
            pronunciationButton(word)
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
    private fun pronunciationButton(word: PairWord) {
        button("${word.traditional}  →  ${word.simplified}   ·   Listen") {
            android.app.AlertDialog.Builder(this)
                .setTitle("Hear ${word.traditional} / ${word.simplified}")
                .setItems(arrayOf("Mandarin · 普通話", "Cantonese · 廣東話")) { _, option ->
                    pronounce(word, cantonese = option == 1)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun pronounce(word: PairWord, cantonese: Boolean) {
        val engine = speech
        if (engine == null || speechStatus != TextToSpeech.SUCCESS) {
            speechMessage(if (speechStatus == null) "Speech is starting. Try again in a moment."
                else "Text-to-speech is unavailable. Check your Android text-to-speech settings.")
            return
        }
        val voice = engine.voices.orEmpty().filter { candidate ->
            val locale = candidate.locale
            val isCantonese = locale.language == "yue" ||
                (locale.language == "zh" && locale.country in setOf("HK", "MO"))
            val isMandarin = locale.language == "cmn" ||
                (locale.language == "zh" && !isCantonese)
            !candidate.isNetworkConnectionRequired &&
                TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED !in candidate.features.orEmpty() &&
                (if (cantonese) isCantonese else isMandarin)
        }.maxWithOrNull(compareBy({ it.quality }, { -it.latency }))
        val language = if (cantonese) "Cantonese" else "Mandarin"
        if (voice == null || engine.setVoice(voice) != TextToSpeech.SUCCESS) {
            speechMessage("Install a $language voice in Android text-to-speech settings.")
            return
        }
        val locale = voice.locale
        val character = if (cantonese || locale.script == "Hant" || locale.country == "TW") {
            word.traditional
        } else {
            word.simplified
        }
        val utteranceId = "character-${++utteranceNumber}"
        activeUtterance = utteranceId
        if (engine.speak(character, TextToSpeech.QUEUE_FLUSH, null, utteranceId) != TextToSpeech.SUCCESS) {
            reportSpeechError(utteranceId)
        }
    }

    private fun reportSpeechError(utteranceId: String?) {
        runOnUiThread {
            if (!isDestroyed && utteranceId != null && utteranceId == activeUtterance) {
                speechMessage("Could not play the pronunciation. Check your text-to-speech voice.")
            }
        }
    }

    private fun speechMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun stopSpeech() {
        activeUtterance = null
        speech?.stop()
    }

    override fun onStop() {
        stopSpeech()
        super.onStop()
    }

    override fun onDestroy() {
        speech?.shutdown()
        speech = null
        super.onDestroy()
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
            addText("The last pair was")
            pronunciationButton(word)
            addText("${word.pinyin} · ${word.meaning}")
        }
        button("Play again") { start(side) }
        button("Choose difficulty") { playing = false; render() }
    }
}
