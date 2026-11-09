# Hanzi Spot

A native Android game written in Kotlin using Android Views. Find one Traditional Chinese character among Simplified characters, then find one Simplified character among Traditional characters in the next round.

## Open in Android Studio

1. Extract the ZIP if you downloaded it.
2. In Android Studio, choose **Open** and select the **HanziSpot** folder containing `settings.gradle.kts`.
3. Let Gradle sync finish. Select JDK 17 or 21 as the Gradle JDK in Settings > Build, Execution, Deployment > Build Tools > Gradle. If Android Studio bundles Java 25, download/select JDK 17 or 21 instead. Install Android SDK Platform 35 if prompted; internet is needed for the first sync.
4. Select an emulator or connected phone running Android 6.0 or newer, then click **Run**.

The project includes the standard Gradle wrapper. From a terminal with JDK 17 or 21 and the Android SDK configured, use `gradlew.bat :app:assembleDebug` on Windows, or `sh gradlew :app:assembleDebug` on macOS/Linux. The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Play

The game draws 10 rounds from 156 playable Traditional and Simplified character pairs.

- Choose a 3×3, 4×4, or 5×5 grid.
- Tap the single character in the requested script.
- Correct answers earn 10 points. A hint highlights the answer and reduces that round to 5 points.
- Each different wrong tile costs one of three hearts. Tapping a previously wrong tile cannot cost another heart.
- After a correct answer, see the Traditional → Simplified pair, pinyin, and English meaning, then continue.
- Tap **Listen** on the revealed pair and choose Mandarin or Cantonese. Install the corresponding offline voice in Android’s text-to-speech settings if needed. For characters with multiple readings, the voice engine may select a different reading from the one shown.
- Complete ten rounds or play until the hearts run out. Best scores are saved separately for each difficulty.

There is no timer, account, advertising, network permission, or external runtime service. The game works offline after installation. Current game state survives Activity recreation, including rotation; completed best scores persist between launches.

## Project

- `app/src/main/java/com/hanzispot/game/MainActivity.kt`: screens, character dataset, game rules, and saved state.
- `app/src/main/res/values/styles.xml`: application theme.
- Android Gradle Plugin 8.9.1, Gradle 8.11.1, Kotlin 2.2.21; compile/target SDK 35, minimum SDK 23.

The vocabulary uses individual character pairs, not a general text conversion engine. Pronunciations and meanings shown are representative readings; some characters have additional readings or meanings in context.

## Manual verification

Run all three difficulties; check that each grid contains exactly one different character, the requested script alternates, and the ten-round result appears. Test a hint, three mistakes, replay, leaving via the menu confirmation, rotation before/after answering, and reopening to verify saved high scores. Check portrait and landscape on a small-screen emulator; content scrolls when needed.

## Validation in the creation environment

The Kotlin source compiled successfully using the Android Studio Kotlin compiler against the installed Android SDK API 37 stubs. XML parsing, character-pair uniqueness, and Gradle wrapper structure checks passed. Full APK assembly was attempted but failed with the installed Java 25.0.3 runtime (Gradle reported 25.0.3). This project uses Gradle 8.11.1: select JDK 17 or 21 and install SDK Platform 35 before building. No emulator or device run was performed.

## Documentation website

Open [doc/index.html](doc/index.html), or serve this project root and visit /doc/. The documentation includes setup instructions, gameplay rules, a searchable character reference, and developer notes. See [docs/README.md](docs/README.md) for static hosting instructions.
