package com.example.domain.languages

object LocalizationEngine {

    enum class StringKey {
        APP_NAME,
        CALCULATOR,
        TOOLS,
        HISTORY,
        SETTINGS,
        CLEAR,
        ALL_CLEAR,
        EQUALS,
        DEGREE,
        RADIAN,
        DECIMAL_PRECISION,
        HAPTIC_FEEDBACK,
        SOUND_FEEDBACK,
        THEME,
        LANGUAGE,
        NUMERAL_SCRIPT,
        FAVORITES,
        NO_HISTORY,
        COPY,
        DELETE,
        CLEAR_ALL
    }

    private val translations: Map<StringKey, Map<String, String>> = mapOf(
        StringKey.APP_NAME to mapOf(
            "en" to "Open Calculator", "bn" to "ওপেন ক্যালকুলেটর", "hi" to "ओपन कैलकुलेटर",
            "es" to "Open Calculator", "fr" to "Open Calculator", "ar" to "الآلة الحاسبة المفتوحة",
            "zh" to "开放计算器", "ru" to "Калькулятор", "de" to "Open Calculator", "ja" to "電卓"
        ),
        StringKey.CALCULATOR to mapOf(
            "en" to "Calculator", "bn" to "ক্যালকুলেটর", "hi" to "कैलकुलेटर",
            "es" to "Calculadora", "fr" to "Calculatrice", "ar" to "الحاسبة",
            "zh" to "计算器", "ru" to "Калькулятор", "de" to "Rechner", "ja" to "計算機",
            "pt" to "Calculadora", "it" to "Calcolatrice", "tr" to "Hesap Makinesi", "ur" to "کیلکولیٹر"
        ),
        StringKey.TOOLS to mapOf(
            "en" to "Tools", "bn" to "টুলস", "hi" to "उपकरण",
            "es" to "Herramientas", "fr" to "Outils", "ar" to "أدوات",
            "zh" to "工具", "ru" to "Инструменты", "de" to "Werkzeuge", "ja" to "ツール",
            "pt" to "Ferramentas", "it" to "Strumenti", "tr" to "Araçlar", "ur" to "اوزار"
        ),
        StringKey.HISTORY to mapOf(
            "en" to "History", "bn" to "ইতিহাস", "hi" to "इतिहास",
            "es" to "Historial", "fr" to "Historique", "ar" to "السجل",
            "zh" to "历史", "ru" to "История", "de" to "Verlauf", "ja" to "履歴",
            "pt" to "Histórico", "it" to "Cronologia", "tr" to "Geçmiş", "ur" to "تاریخچہ"
        ),
        StringKey.SETTINGS to mapOf(
            "en" to "Settings", "bn" to "সেটিংস", "hi" to "सेटिंग्स",
            "es" to "Ajustes", "fr" to "Paramètres", "ar" to "الإعدادات",
            "zh" to "设置", "ru" to "Настройки", "de" to "Einstellungen", "ja" to "設定",
            "pt" to "Configurações", "it" to "Impostazioni", "tr" to "Ayarlar", "ur" to "ترتیبات"
        ),
        StringKey.CLEAR to mapOf(
            "en" to "Clear", "bn" to "মুছুন", "hi" to "मिटाएं",
            "es" to "Borrar", "fr" to "Effacer", "ar" to "مسح",
            "zh" to "清除", "ru" to "Стереть", "de" to "Löschen", "ja" to "クリア"
        ),
        StringKey.ALL_CLEAR to mapOf(
            "en" to "AC", "bn" to "সব মুছুন", "hi" to "सब मिटाएं",
            "es" to "AC", "fr" to "AC", "ar" to "مسح الكل",
            "zh" to "全清", "ru" to "Сброс", "de" to "AC", "ja" to "全消去"
        ),
        StringKey.EQUALS to mapOf(
            "en" to "Equals", "bn" to "সমান", "hi" to "बराबर",
            "es" to "Igual", "fr" to "Égal", "ar" to "يساوي",
            "zh" to "等于", "ru" to "Равно", "de" to "Gleich", "ja" to "等号"
        ),
        StringKey.DEGREE to mapOf(
            "en" to "DEG", "bn" to "ডিগ্রি", "hi" to "डिग्री",
            "es" to "DEG", "fr" to "DEG", "ar" to "درجة",
            "zh" to "角度", "ru" to "ГРАД", "de" to "DEG", "ja" to "度"
        ),
        StringKey.RADIAN to mapOf(
            "en" to "RAD", "bn" to "রেডিয়ান", "hi" to "रेडियन",
            "es" to "RAD", "fr" to "RAD", "ar" to "راديان",
            "zh" to "弧度", "ru" to "РАД", "de" to "RAD", "ja" to "ラジアン"
        ),
        StringKey.DECIMAL_PRECISION to mapOf(
            "en" to "Decimal Precision", "bn" to "দশমিক নির্ভুলতা", "hi" to "दशमलव परिशुद्धता",
            "es" to "Precisión decimal", "fr" to "Précision décimale", "ar" to "الدقة العشرية",
            "zh" to "小数精度", "ru" to "Десятичная точность", "de" to "Dezimalstellen", "ja" to "小数点精度"
        ),
        StringKey.HAPTIC_FEEDBACK to mapOf(
            "en" to "Haptic Feedback", "bn" to "হ্যাপটিক ভাইব্রেশন", "hi" to "हैप्टिक कंपन",
            "es" to "Vibración táctil", "fr" to "Retour haptique", "ar" to "الاهتزاز اللمسي",
            "zh" to "触觉震动", "ru" to "Виброотклик", "de" to "Haptisches Feedback", "ja" to "触覚フィードバック"
        ),
        StringKey.SOUND_FEEDBACK to mapOf(
            "en" to "Key Click Sound", "bn" to "কী সাউন্ড", "hi" to "कुंजी ध्वनि",
            "es" to "Sonido de teclas", "fr" to "Son des touches", "ar" to "صوت المفاتيح",
            "zh" to "按键音效", "ru" to "Звук нажатия", "de" to "Tastenton", "ja" to "キータップ音"
        ),
        StringKey.THEME to mapOf(
            "en" to "Theme", "bn" to "থিম", "hi" to "थीम",
            "es" to "Tema", "fr" to "Thème", "ar" to "المظهر",
            "zh" to "主题", "ru" to "Тема", "de" to "Design", "ja" to "テーマ"
        ),
        StringKey.LANGUAGE to mapOf(
            "en" to "App Language", "bn" to "অ্যাপের ভাষা", "hi" to "ऐप भाषा",
            "es" to "Idioma de la aplicación", "fr" to "Langue de l'application", "ar" to "لغة التطبيق",
            "zh" to "应用语言", "ru" to "Язык приложения", "de" to "App-Sprache", "ja" to "アプリの言語"
        ),
        StringKey.NUMERAL_SCRIPT to mapOf(
            "en" to "Numeral Script", "bn" to "সংখ্যার লিপি", "hi" to "संख्या लिपि",
            "es" to "Sistema numérico", "fr" to "Système de chiffres", "ar" to "نظام الأرقام",
            "zh" to "数字系统", "ru" to "Система цифр", "de" to "Ziffernsystem", "ja" to "数字体系"
        ),
        StringKey.FAVORITES to mapOf(
            "en" to "Favorites", "bn" to "প্রিয়", "hi" to "पसंदीदा",
            "es" to "Favoritos", "fr" to "Favoris", "ar" to "المفضلة",
            "zh" to "收藏", "ru" to "Избранное", "de" to "Favoriten", "ja" to "お気に入り"
        ),
        StringKey.NO_HISTORY to mapOf(
            "en" to "No calculations recorded", "bn" to "কোনো হিসাব রেকর্ড করা নেই", "hi" to "कोई गणना दर्ज नहीं है",
            "es" to "Sin cálculos registrados", "fr" to "Aucun calcul enregistré", "ar" to "لا توجد حسابات مسجلة",
            "zh" to "暂无计算记录", "ru" to "Нет записей", "de" to "Keine Rechnungen", "ja" to "計算履歴がありません"
        ),
        StringKey.COPY to mapOf(
            "en" to "Copy", "bn" to "কপি", "hi" to "कॉपी",
            "es" to "Copiar", "fr" to "Copier", "ar" to "نسخ",
            "zh" to "复制", "ru" to "Копировать", "de" to "Kopieren", "ja" to "コピー"
        ),
        StringKey.DELETE to mapOf(
            "en" to "Delete", "bn" to "মুছুন", "hi" to "हटाएं",
            "es" to "Eliminar", "fr" to "Supprimer", "ar" to "حذف",
            "zh" to "删除", "ru" to "Удалить", "de" to "Löschen", "ja" to "削除"
        ),
        StringKey.CLEAR_ALL to mapOf(
            "en" to "Clear All", "bn" to "সব মুছুন", "hi" to "सभी हटाएं",
            "es" to "Borrar todo", "fr" to "Tout effacer", "ar" to "مسح الكل",
            "zh" to "清空全部", "ru" to "Очистить всё", "de" to "Alles löschen", "ja" to "すべて消去"
        )
    )

    fun getString(key: StringKey, languageCode: String): String {
        val langMap = translations[key] ?: return key.name
        return langMap[languageCode] ?: langMap["en"] ?: key.name
    }
}
