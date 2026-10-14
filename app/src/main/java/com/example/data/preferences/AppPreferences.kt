package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM, DARK, LIGHT
}

enum class AngleMode {
    DEGREE, RADIAN
}

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("omnicalc_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(
        try {
            ThemeMode.valueOf(prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
    )
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(prefs.getBoolean(KEY_HAPTICS, true))
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(prefs.getBoolean(KEY_SOUND, false))
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _decimalPrecision = MutableStateFlow(prefs.getInt(KEY_PRECISION, 8))
    val decimalPrecision: StateFlow<Int> = _decimalPrecision.asStateFlow()

    private val _angleMode = MutableStateFlow(
        try {
            AngleMode.valueOf(prefs.getString(KEY_ANGLE_MODE, AngleMode.DEGREE.name) ?: AngleMode.DEGREE.name)
        } catch (_: Exception) {
            AngleMode.DEGREE
        }
    )
    val angleMode: StateFlow<AngleMode> = _angleMode.asStateFlow()

    private val _languageCode = MutableStateFlow(prefs.getString(KEY_LANGUAGE_CODE, "en") ?: "en")
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    private val _numeralScript = MutableStateFlow(
        try {
            com.example.domain.languages.NumeralScript.valueOf(
                prefs.getString(KEY_NUMERAL_SCRIPT, com.example.domain.languages.NumeralScript.LATIN.name)
                    ?: com.example.domain.languages.NumeralScript.LATIN.name
            )
        } catch (_: Exception) {
            com.example.domain.languages.NumeralScript.LATIN
        }
    )
    val numeralScript: StateFlow<com.example.domain.languages.NumeralScript> = _numeralScript.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS, enabled).apply()
        _hapticsEnabled.value = enabled
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
        _soundEnabled.value = enabled
    }

    fun setDecimalPrecision(precision: Int) {
        val clamped = precision.coerceIn(2, 12)
        prefs.edit().putInt(KEY_PRECISION, clamped).apply()
        _decimalPrecision.value = clamped
    }

    fun setAngleMode(mode: AngleMode) {
        prefs.edit().putString(KEY_ANGLE_MODE, mode.name).apply()
        _angleMode.value = mode
    }

    fun setLanguageCode(code: String) {
        prefs.edit().putString(KEY_LANGUAGE_CODE, code).apply()
        _languageCode.value = code
    }

    fun setNumeralScript(script: com.example.domain.languages.NumeralScript) {
        prefs.edit().putString(KEY_NUMERAL_SCRIPT, script.name).apply()
        _numeralScript.value = script
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_HAPTICS = "haptics_enabled"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_PRECISION = "decimal_precision"
        private const val KEY_ANGLE_MODE = "angle_mode"
        private const val KEY_LANGUAGE_CODE = "language_code"
        private const val KEY_NUMERAL_SCRIPT = "numeral_script"

        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(context: Context): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppPreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
