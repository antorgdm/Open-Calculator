# Open Calculator

A free, offline, all-in-one calculator for Android. It combines a scientific calculator with a set of everyday tools (unit and currency conversion, finance, dates, geometry, statistics, programmer mode) in one app. No account, no ads, no analytics, no internet permission. Everything stays on your device.

- **Package:** `com.aistudio.omnicalc.vxrtzq` (change before publishing)
- **Platform:** Android 7.0+ (minSdk 24), targets SDK 36
- **Built with:** Kotlin 2.2, Jetpack Compose (Material 3), Room, Android Gradle Plugin 9.1

## Features

### Calculator
- Basic and scientific modes (toggle with the science button)
- Operators `+ − × ÷ ^ ! %`, brackets, unary minus, **implicit multiplication** (`2π`, `2(3)`, `(1+2)(3+4)`)
- Percent works like a phone calculator: `200 + 10%` = 220, `50%` = 0.5
- Functions: `sin cos tan`, inverse `sin⁻¹ cos⁻¹ tan⁻¹`, `sinh cosh tanh`, `log ln √ ∛ abs`, `x²`, `xʸ`, `1/x`, `n!`
- Constants `π` and `e`, scientific notation (`EXP` key, e.g. `2E3`)
- **DEG / RAD** angle mode
- **Live result preview** while you type
- Floating-point noise is cleaned up (`sin(180°)` = 0, `tan(90°)` = undefined)
- Memory keys: `MC MR M+ M−`, plus an `Ans` value from the last result
- Copy result, paste an expression, smart backspace (removes `sin(` as one unit)
- Clear errors: division by zero, invalid syntax, domain errors, overflow

### History
- Every calculation is saved on the device (Room database)
- Search, mark favorites, filter favorites, copy, delete, clear all
- Tap an item to reuse it in the calculator

### Tools
| Tool | What it does |
|---|---|
| Currency Converter | 186 currencies using **built-in approximate rates** (not live, fully offline) |
| Unit Converter | 13 categories: length, weight, area, volume, temperature, speed, time, data storage, pressure, energy, power, frequency, angle |
| Percentage Tools | percent of a number, percent change, discount, profit/loss, markup/margin, add/remove tax (VAT/GST) |
| Financial Calculator | simple and compound interest, loan EMI, SIP, tip and bill split |
| Date & Time | age calculator, difference between dates, add days to a date, working days |
| Geometry | square, rectangle, triangle, circle, trapezoid, parallelogram, cube, cuboid, cylinder, cone, sphere (area, perimeter, volume) |
| Programmer Calc | HEX / DEC / OCT / BIN conversion, BYTE/WORD/DWORD/QWORD, AND, OR, XOR, NOT, shifts, bit toggles |
| Stats & Equations | mean, median, mode, standard deviation, variance, quartiles; linear, quadratic and 2×2 system solvers with steps |
| World Languages & Numerals | 143 languages and 16 numeral systems (Bengali, Arabic-Indic, Devanagari, Thai and more) with a number translator |

### Settings and personalization
- Themes: System, Light, Dark, and a "Classic Flat" palette
- App language and numeral script (display digits in your own script)
- Decimal precision (2–12 places)
- Haptic feedback and key sound toggles
- Privacy and About dialogs

## Privacy
The app works entirely offline. It requests only the `VIBRATE` permission, collects no personal data, contains no third-party SDKs, and excludes app data from Android backup. See [`PRIVACY_POLICY.md`](PRIVACY_POLICY.md) and [`TERMS_AND_CONDITIONS.md`](TERMS_AND_CONDITIONS.md).

## Project structure
```
app/src/main/java/com/example/
├── MainActivity.kt          # entry point, navigation, theme, system bars
├── data/
│   ├── database/            # Room: history entity, DAO, repository
│   └── preferences/         # settings stored in SharedPreferences
├── domain/                  # pure Kotlin logic (no Android UI)
│   ├── evaluator/           # expression engine: tokenizer → shunting-yard → RPN
│   ├── converters/          # currency + unit conversion
│   ├── finance/ datetime/ geometry/ statistics/ equations/ programmer/
│   └── languages/           # world languages and numeral scripts
└── ui/
    ├── calculator/          # screen, buttons, ViewModel
    ├── history/ settings/ tools/ components/ theme/
```

## Build and run
1. Install the latest **Android Studio** (SDK 36 required) and open this folder.
2. Let Gradle sync, then press **Run** (device or emulator), or use **Build → Build APK(s)**.
3. Debug APK: `app/build/outputs/apk/debug/app-debug.apk`.
4. For a release build use **Build → Generate Signed App Bundle / APK**, or set the `KEYSTORE_PATH`, `STORE_PASSWORD` and `KEY_PASSWORD` environment variables.

Run the unit tests with `./gradlew test` (or from Android Studio). `CalculatorEngineRegressionTest` covers the expression engine.

## Known limitations
- Currency rates are fixed, approximate values and are **not updated automatically**.
- The programmer bit board shows only the lowest 32 bits in 64-bit mode.
- Most screen labels are English; only parts of the UI follow the selected language.
- Financial tools show amounts with a `$` sign regardless of currency.
- Changing the database schema currently resets saved history (no migrations yet).

## License
All rights reserved by SE7EN unless you add an open-source license. Add a `LICENSE` file if you want to publish the source publicly.

## Contact
mdrabiulalam056@gmail.com


<img width="1920" height="1080" alt="Artboard 1" src="https://github.com/user-attachments/assets/23b6c270-de53-49c2-ae8a-a8708f664bd9" />
<img width="1920" height="1080" alt="Artboard 1" src="https://github.com/user-attachments/assets/2816ac52-ed15-48f8-b8fd-0dff91d6b6d3" />
<img width="1920" height="1080" alt="Tools" src="https://github.com/user-attachments/assets/db5b4513-4bd9-4cf6-be07-51995bda336b" />
<img width="1920" height="1080" alt="Artboard 2" src="https://github.com/user-attachments/assets/1adc3528-bf70-4056-8905-b8ead4572f39" />
