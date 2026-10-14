package com.example.domain.languages

object LanguageEngine {

    val allLanguages: List<WorldLanguage> = listOf(
        // Major International Languages
        WorldLanguage("en", "English", "English", "Global / UK / US / Canada / Australia", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("zh", "Chinese (Mandarin)", "中文 (普通话)", "China / Taiwan / Singapore", "Sino-Tibetan", NumeralScript.LATIN),
        WorldLanguage("es", "Spanish", "Español", "Spain / Latin America", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("hi", "Hindi", "हिन्दी", "India", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),
        WorldLanguage("bn", "Bengali", "বাংলা", "Bangladesh / India", "Indo-European / Indo-Aryan", NumeralScript.BENGALI),
        WorldLanguage("ar", "Arabic", "العربية", "Middle East / North Africa", "Afroasiatic / Semitic", NumeralScript.ARABIC_INDIC),
        WorldLanguage("pt", "Portuguese", "Português", "Brazil / Portugal / Angola / Mozambique", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("ru", "Russian", "Русский", "Russia / CIS", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("ja", "Japanese", "日本語", "Japan", "Japonic", NumeralScript.LATIN),
        WorldLanguage("de", "German", "Deutsch", "Germany / Austria / Switzerland", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("fr", "French", "Français", "France / Canada / Francophone Africa", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("ur", "Urdu", "اردو", "Pakistan / India", "Indo-European / Indo-Aryan", NumeralScript.PERSIAN),
        WorldLanguage("id", "Indonesian", "Bahasa Indonesia", "Indonesia", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("it", "Italian", "Italiano", "Italy / Switzerland", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("tr", "Turkish", "Türkçe", "Turkey / Cyprus", "Turkic", NumeralScript.LATIN),
        WorldLanguage("ko", "Korean", "한국어", "South Korea / North Korea", "Koreanic", NumeralScript.LATIN),
        WorldLanguage("vi", "Vietnamese", "Tiếng Việt", "Vietnam", "Austroasiatic", NumeralScript.LATIN),
        WorldLanguage("pl", "Polish", "Polski", "Poland", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("fa", "Persian (Farsi)", "فارسی", "Iran / Afghanistan / Tajikistan", "Indo-European / Iranian", NumeralScript.PERSIAN),
        WorldLanguage("uk", "Ukrainian", "Українська", "Ukraine", "Indo-European / Slavic", NumeralScript.LATIN),

        // South Asian Languages
        WorldLanguage("pa", "Punjabi", "ਪੰਜਾਬੀ / پنجابی", "India / Pakistan", "Indo-European / Indo-Aryan", NumeralScript.GURMUKHI),
        WorldLanguage("mr", "Marathi", "मराठी", "India (Maharashtra)", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),
        WorldLanguage("te", "Telugu", "తెలుగు", "India (Andhra / Telangana)", "Dravidian", NumeralScript.TELUGU),
        WorldLanguage("ta", "Tamil", "தமிழ்", "India / Sri Lanka / Singapore", "Dravidian", NumeralScript.TAMIL),
        WorldLanguage("gu", "Gujarati", "ગુજરાતી", "India (Gujarat)", "Indo-European / Indo-Aryan", NumeralScript.GUJARATI),
        WorldLanguage("kn", "Kannada", "ಕನ್ನಡ", "India (Karnataka)", "Dravidian", NumeralScript.KANNADA),
        WorldLanguage("ml", "Malayalam", "മലയാളം", "India (Kerala)", "Dravidian", NumeralScript.MALAYALAM),
        WorldLanguage("or", "Odia", "ଓଡ଼ିଆ", "India (Odisha)", "Indo-European / Indo-Aryan", NumeralScript.LATIN),
        WorldLanguage("as", "Assamese", "অসমীয়া", "India (Assam)", "Indo-European / Indo-Aryan", NumeralScript.BENGALI),
        WorldLanguage("ne", "Nepali", "नेपाली", "Nepal / India", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),
        WorldLanguage("si", "Sinhala", "සිංහල", "Sri Lanka", "Indo-European / Indo-Aryan", NumeralScript.LATIN),
        WorldLanguage("sd", "Sindhi", "سنڌي", "Pakistan / India", "Indo-European / Indo-Aryan", NumeralScript.PERSIAN),
        WorldLanguage("ps", "Pashto", "پښتو", "Afghanistan / Pakistan", "Indo-European / Iranian", NumeralScript.PERSIAN),
        WorldLanguage("dv", "Dhivehi (Maldivian)", "ދިވެހި", "Maldives", "Indo-European / Indo-Aryan", NumeralScript.ARABIC_INDIC),
        WorldLanguage("bho", "Bhojpuri", "भोजपुरी", "India / Nepal", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),
        WorldLanguage("mai", "Maithili", "मैथिली", "India / Nepal", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),
        WorldLanguage("sa", "Sanskrit", "संस्कृतम्", "India", "Indo-European / Indo-Aryan", NumeralScript.DEVANAGARI),

        // Southeast & East Asian Languages
        WorldLanguage("th", "Thai", "ไทย", "Thailand", "Kra-Dai", NumeralScript.THAI),
        WorldLanguage("my", "Burmese", "မြန်မာစာ", "Myanmar", "Sino-Tibetan", NumeralScript.MYANMAR),
        WorldLanguage("km", "Khmer", "ភាសាខ្មែរ", "Cambodia", "Austroasiatic", NumeralScript.KHMER),
        WorldLanguage("lo", "Lao", "ພາສາລາວ", "Laos", "Kra-Dai", NumeralScript.LAO),
        WorldLanguage("tl", "Tagalog (Filipino)", "Tagalog", "Philippines", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("ms", "Malay", "Bahasa Melayu", "Malaysia / Brunei / Singapore", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("jv", "Javanese", "Basa Jawa", "Indonesia", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("su", "Sundanese", "Basa Sunda", "Indonesia", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("ceb", "Cebuano", "Bisaya", "Philippines", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("ilo", "Ilocano", "Ilokano", "Philippines", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("yue", "Cantonese (Yue)", "粵語", "Hong Kong / Macau / Guangdong", "Sino-Tibetan", NumeralScript.LATIN),
        WorldLanguage("mn", "Mongolian", "Монгол хэл", "Mongolia / Inner Mongolia", "Mongolic", NumeralScript.LATIN),
        WorldLanguage("bo", "Tibetan", "བོད་སྐད", "Tibet / China / India", "Sino-Tibetan", NumeralScript.TIBETAN),

        // European Languages
        WorldLanguage("nl", "Dutch", "Nederlands", "Netherlands / Belgium / Suriname", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("el", "Greek", "Ελληνικά", "Greece / Cyprus", "Indo-European / Hellenic", NumeralScript.LATIN),
        WorldLanguage("sv", "Swedish", "Svenska", "Sweden / Finland", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("da", "Danish", "Dansk", "Denmark", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("fi", "Finnish", "Suomi", "Finland", "Uralic / Finno-Ugric", NumeralScript.LATIN),
        WorldLanguage("no", "Norwegian", "Norsk", "Norway", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("cs", "Czech", "Čeština", "Czech Republic", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("hu", "Hungarian", "Magyar", "Hungary", "Uralic / Finno-Ugric", NumeralScript.LATIN),
        WorldLanguage("ro", "Romanian", "Română", "Romania / Moldova", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("bg", "Bulgarian", "Български", "Bulgaria", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("sr", "Serbian", "Српски / Srpski", "Serbia / Bosnia", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("hr", "Croatian", "Hrvatski", "Croatia / Bosnia", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("bs", "Bosnian", "Bosanski", "Bosnia and Herzegovina", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("sk", "Slovak", "Slovenčina", "Slovakia", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("sl", "Slovenian", "Slovenščina", "Slovenia", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("lt", "Lithuanian", "Lietuvių", "Lithuania", "Indo-European / Baltic", NumeralScript.LATIN),
        WorldLanguage("lv", "Latvian", "Latviešu", "Latvia", "Indo-European / Baltic", NumeralScript.LATIN),
        WorldLanguage("et", "Estonian", "Eesti", "Estonia", "Uralic / Finno-Ugric", NumeralScript.LATIN),
        WorldLanguage("ga", "Irish (Gaeilge)", "Gaeilge", "Ireland", "Indo-European / Celtic", NumeralScript.LATIN),
        WorldLanguage("cy", "Welsh", "Cymraeg", "Wales (UK)", "Indo-European / Celtic", NumeralScript.LATIN),
        WorldLanguage("gd", "Scottish Gaelic", "Gàidhlig", "Scotland (UK)", "Indo-European / Celtic", NumeralScript.LATIN),
        WorldLanguage("eu", "Basque", "Euskara", "Spain / France", "Language Isolate", NumeralScript.LATIN),
        WorldLanguage("ca", "Catalan", "Català", "Spain / Andorra", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("gl", "Galician", "Galego", "Spain (Galicia)", "Indo-European / Romance", NumeralScript.LATIN),
        WorldLanguage("is", "Icelandic", "Íslenska", "Iceland", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("sq", "Albanian", "Shqip", "Albania / Kosovo", "Indo-European / Albanian", NumeralScript.LATIN),
        WorldLanguage("mk", "Macedonian", "Македонски", "North Macedonia", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("be", "Belarusian", "Беларуская", "Belarus", "Indo-European / Slavic", NumeralScript.LATIN),
        WorldLanguage("mt", "Maltese", "Malti", "Malta", "Afroasiatic / Semitic", NumeralScript.LATIN),
        WorldLanguage("lb", "Luxembourgish", "Lëtzebuergesch", "Luxembourg", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("fo", "Faroese", "Føroyskt", "Faroe Islands", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("la", "Latin", "Latina", "Vatican / Historical", "Indo-European / Italic", NumeralScript.LATIN),
        WorldLanguage("eo", "Esperanto", "Esperanto", "International Constructed", "Constructed", NumeralScript.LATIN),

        // Middle Eastern & Central Asian Languages
        WorldLanguage("he", "Hebrew", "עברית", "Israel", "Afroasiatic / Semitic", NumeralScript.LATIN),
        WorldLanguage("ku", "Kurdish", "Kurdî / کوردی", "Kurdistan / Iraq / Turkey / Iran", "Indo-European / Iranian", NumeralScript.LATIN),
        WorldLanguage("az", "Azerbaijani", "Azərbaycan", "Azerbaijan / Iran", "Turkic", NumeralScript.LATIN),
        WorldLanguage("uz", "Uzbek", "Oʻzbekcha", "Uzbekistan", "Turkic", NumeralScript.LATIN),
        WorldLanguage("kk", "Kazakh", "Қазақ тілі", "Kazakhstan", "Turkic", NumeralScript.LATIN),
        WorldLanguage("tg", "Tajik", "Тоҷикӣ", "Tajikistan", "Indo-European / Iranian", NumeralScript.LATIN),
        WorldLanguage("tk", "Turkmen", "Türkmençe", "Turkmenistan", "Turkic", NumeralScript.LATIN),
        WorldLanguage("ky", "Kyrgyz", "Кыргызча", "Kyrgyzstan", "Turkic", NumeralScript.LATIN),
        WorldLanguage("ka", "Georgian", "ქართული", "Georgia", "Kartvelian", NumeralScript.LATIN),
        WorldLanguage("hy", "Armenian", "Հայերեն", "Armenia", "Indo-European / Armenian", NumeralScript.LATIN),
        WorldLanguage("tt", "Tatar", "Татар теле", "Russia (Tatarstan)", "Turkic", NumeralScript.LATIN),
        WorldLanguage("ug", "Uyghur", "ئۇيغۇرچە", "China (Xinjiang)", "Turkic", NumeralScript.PERSIAN),

        // African Languages
        WorldLanguage("sw", "Swahili", "Kiswahili", "East Africa (Tanzania, Kenya, Uganda)", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("ha", "Hausa", "Harshen Hausa", "Nigeria / Niger / West Africa", "Afroasiatic / Chadic", NumeralScript.LATIN),
        WorldLanguage("yo", "Yoruba", "Èdè Yorùbá", "Nigeria / Benin", "Niger-Congo", NumeralScript.LATIN),
        WorldLanguage("ig", "Igbo", "Asụsụ Igbo", "Nigeria", "Niger-Congo", NumeralScript.LATIN),
        WorldLanguage("am", "Amharic", "አማርኛ", "Ethiopia", "Afroasiatic / Semitic", NumeralScript.LATIN),
        WorldLanguage("om", "Oromo", "Afaan Oromoo", "Ethiopia / Kenya", "Afroasiatic / Cushitic", NumeralScript.LATIN),
        WorldLanguage("ti", "Tigrinya", "ትግርኛ", "Eritrea / Ethiopia", "Afroasiatic / Semitic", NumeralScript.LATIN),
        WorldLanguage("so", "Somali", "Soomaaliga", "Somalia / Djibouti / Ethiopia", "Afroasiatic / Cushitic", NumeralScript.LATIN),
        WorldLanguage("zu", "Zulu", "isiZulu", "South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("xh", "Xhosa", "isiXhosa", "South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("af", "Afrikaans", "Afrikaans", "South Africa / Namibia", "Indo-European / Germanic", NumeralScript.LATIN),
        WorldLanguage("sn", "Shona", "chiShona", "Zimbabwe / Mozambique", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("st", "Sesotho", "Sesotho", "Lesotho / South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("tn", "Setswana", "Setswana", "Botswana / South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("ny", "Chichewa", "Chichewa", "Malawi / Zambia / Mozambique", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("lg", "Luganda", "Oluganda", "Uganda", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("rw", "Kinyarwanda", "Ikinyarwanda", "Rwanda", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("rn", "Kirundi", "Ikirundi", "Burundi", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("mg", "Malagasy", "Fiteny Malagasy", "Madagascar", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("wo", "Wolof", "Wolof", "Senegal / Gambia", "Niger-Congo", NumeralScript.LATIN),
        WorldLanguage("ff", "Fulani (Fula)", "Fulfulde / Pulaar", "West / Central Africa", "Niger-Congo", NumeralScript.LATIN),
        WorldLanguage("bm", "Bambara", "Bamanankan", "Mali", "Niger-Congo", NumeralScript.LATIN),
        WorldLanguage("ln", "Lingala", "Lingála", "DR Congo / Republic of Congo", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("kg", "Kongo", "Kikongo", "DR Congo / Angola", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("ts", "Tsonga", "Xitsonga", "South Africa / Mozambique", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("ss", "Swati", "SiSwati", "Eswatini / South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("ve", "Venda", "Tshivenḓa", "South Africa / Zimbabwe", "Niger-Congo / Bantu", NumeralScript.LATIN),
        WorldLanguage("nr", "Southern Ndebele", "isiNdebele", "South Africa", "Niger-Congo / Bantu", NumeralScript.LATIN),

        // Americas & Indigenous Languages
        WorldLanguage("qu", "Quechua", "Runa Simi", "Peru / Bolivia / Ecuador", "Quechuan", NumeralScript.LATIN),
        WorldLanguage("gn", "Guarani", "Avañe'ẽ", "Paraguay / Argentina", "Tupian", NumeralScript.LATIN),
        WorldLanguage("ay", "Aymara", "Aymar aru", "Bolivia / Peru", "Aymaran", NumeralScript.LATIN),
        WorldLanguage("nah", "Nahuatl", "Nāhuatl", "Mexico", "Uto-Aztecan", NumeralScript.LATIN),
        WorldLanguage("myn", "Mayan", "Maya", "Guatemala / Mexico", "Mayan", NumeralScript.LATIN),
        WorldLanguage("chr", "Cherokee", "ᏣᎳᎩ (Tsalagi)", "United States", "Iroquoian", NumeralScript.LATIN),
        WorldLanguage("iu", "Inuktitut", "ᐃᓄᒃᑎᑐᑦ", "Canada (Nunavut)", "Eskimo-Aleut", NumeralScript.LATIN),
        WorldLanguage("nv", "Navajo", "Diné bizaad", "United States", "Na-Dene", NumeralScript.LATIN),
        WorldLanguage("kl", "Greenlandic", "Kalaallisut", "Greenland", "Eskimo-Aleut", NumeralScript.LATIN),
        WorldLanguage("ht", "Haitian Creole", "Kreyòl Ayisyen", "Haiti", "Creole / French-based", NumeralScript.LATIN),
        WorldLanguage("pap", "Papiamento", "Papiamentu", "Aruba / Curacao / Bonaire", "Creole", NumeralScript.LATIN),

        // Pacific & Oceanic Languages
        WorldLanguage("sm", "Samoan", "Gagana Sāmoa", "Samoa / American Samoa", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("to", "Tongan", "Lea Faka-Tonga", "Tonga", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("fj", "Fijian", "Na Vosa Vakaviti", "Fiji", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("mi", "Maori", "Te Reo Māori", "New Zealand", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("haw", "Hawaiian", "ʻŌlelo Hawaiʻi", "Hawaii (US)", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("ty", "Tahitian", "Reo Tahiti", "French Polynesia", "Austronesian", NumeralScript.LATIN),
        WorldLanguage("bi", "Bislama", "Bislama", "Vanuatu", "Creole / English-based", NumeralScript.LATIN),
        WorldLanguage("tpi", "Tok Pisin", "Tok Pisin", "Papua New Guinea", "Creole / English-based", NumeralScript.LATIN),
        WorldLanguage("ch", "Chamorro", "Finu' Chamorro", "Guam / Northern Mariana Islands", "Austronesian", NumeralScript.LATIN)
    )

    fun searchLanguages(query: String): List<WorldLanguage> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allLanguages
        return allLanguages.filter {
            it.englishName.lowercase().contains(q) ||
            it.nativeName.lowercase().contains(q) ||
            it.code.lowercase().contains(q) ||
            it.countryOrRegion.lowercase().contains(q) ||
            it.family.lowercase().contains(q)
        }
    }

    fun convertDigits(input: String, script: NumeralScript): String {
        if (script == NumeralScript.LATIN) return input
        val targetDigits = script.zeroToNine
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                val index = ch - '0'
                sb.append(targetDigits[index])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    // Number to words translations in multiple languages
    fun numberToWords(num: Long, langCode: String): String {
        if (num == 0L) {
            return when (langCode) {
                "bn" -> "শূন্য"
                "hi" -> "शून्य"
                "es" -> "cero"
                "fr" -> "zéro"
                "de" -> "null"
                "ar" -> "صفر"
                "ru" -> "ноль"
                "zh" -> "零"
                "ja" -> "ゼロ"
                "pt" -> "zero"
                "it" -> "zero"
                "tr" -> "sıfır"
                "id" -> "nol"
                else -> "zero"
            }
        }

        return when (langCode) {
            "bn" -> bengaliNumberToWords(num)
            "es" -> spanishNumberToWords(num)
            "fr" -> frenchNumberToWords(num)
            "hi" -> hindiNumberToWords(num)
            "zh" -> chineseNumberToWords(num)
            else -> englishNumberToWords(num)
        }
    }

    private fun englishNumberToWords(n: Long): String {
        if (n < 0) return "minus " + englishNumberToWords(-n)
        if (n < 20) {
            val units = listOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen")
            return units[n.toInt()]
        }
        if (n < 100) {
            val tens = listOf("", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")
            val rem = n % 10
            return tens[(n / 10).toInt()] + (if (rem != 0L) " " + englishNumberToWords(rem) else "")
        }
        if (n < 1000) {
            val rem = n % 100
            return englishNumberToWords(n / 100) + " hundred" + (if (rem != 0L) " and " + englishNumberToWords(rem) else "")
        }
        if (n < 1_000_000) {
            val rem = n % 1000
            return englishNumberToWords(n / 1000) + " thousand" + (if (rem != 0L) ", " + englishNumberToWords(rem) else "")
        }
        if (n < 1_000_000_000) {
            val rem = n % 1_000_000
            return englishNumberToWords(n / 1_000_000) + " million" + (if (rem != 0L) ", " + englishNumberToWords(rem) else "")
        }
        val rem = n % 1_000_000_000
        return englishNumberToWords(n / 1_000_000_000) + " billion" + (if (rem != 0L) ", " + englishNumberToWords(rem) else "")
    }

    private fun bengaliNumberToWords(n: Long): String {
        if (n < 0) return "মাইনাস " + bengaliNumberToWords(-n)
        val bnWords = mapOf(
            1L to "এক", 2L to "দুই", 3L to "তিন", 4L to "চার", 5L to "পাঁচ",
            6L to "ছয়", 7L to "সাত", 8L to "আট", 9L to "নয়", 10L to "দশ",
            11L to "এগারো", 12L to "বারো", 13L to "তেরো", 14L to "চৌদ্দ", 15L to "পনেরো",
            16L to "ষোল", 17L to "সতেরো", 18L to "আঠারো", 19L to "উনিশ", 20L to "কুড়ি / বিশ",
            25L to "পঁচিশ", 30L to "ত্রিশ", 40L to "চল্লিশ", 50L to "পঞ্চাশ",
            60L to "ষাট", 70L to "সত্তর", 80L to "আশি", 90L to "নব্বই", 100L to "একশত"
        )
        if (bnWords.containsKey(n)) return bnWords[n]!!
        if (n < 100) return "সংখ্যা $n"
        if (n < 1000) return bengaliNumberToWords(n / 100) + " শত " + (if (n % 100 != 0L) bengaliNumberToWords(n % 100) else "")
        if (n < 100000) return bengaliNumberToWords(n / 1000) + " হাজার " + (if (n % 1000 != 0L) bengaliNumberToWords(n % 1000) else "")
        if (n < 10000000) return bengaliNumberToWords(n / 100000) + " লাখ " + (if (n % 100000 != 0L) bengaliNumberToWords(n % 100000) else "")
        return bengaliNumberToWords(n / 10000000) + " কোটি " + (if (n % 10000000 != 0L) bengaliNumberToWords(n % 10000000) else "")
    }

    private fun spanishNumberToWords(n: Long): String {
        if (n < 0) return "menos " + spanishNumberToWords(-n)
        val units = listOf("", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve",
            "diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve", "veinte")
        if (n <= 20) return units[n.toInt()]
        if (n < 30) return "veinti" + units[(n % 10).toInt()]
        if (n < 100) {
            val tens = listOf("", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa")
            val rem = n % 10
            return tens[(n / 10).toInt()] + (if (rem != 0L) " y " + units[rem.toInt()] else "")
        }
        if (n == 100L) return "cien"
        if (n < 1000) {
            val rem = n % 100
            return (if (n / 100 == 1L) "ciento" else units[(n / 100).toInt()] + "cientos") + (if (rem != 0L) " " + spanishNumberToWords(rem) else "")
        }
        if (n < 1_000_000) {
            val thousands = n / 1000
            val rem = n % 1000
            val prefix = if (thousands == 1L) "mil" else spanishNumberToWords(thousands) + " mil"
            return prefix + (if (rem != 0L) " " + spanishNumberToWords(rem) else "")
        }
        return "$n (en español)"
    }

    private fun frenchNumberToWords(n: Long): String {
        if (n < 0) return "moins " + frenchNumberToWords(-n)
        val units = listOf("", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
            "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf")
        if (n < 20) return units[n.toInt()]
        if (n < 70) {
            val tens = listOf("", "", "vingt", "trente", "quarante", "cinquante", "soixante")
            val rem = n % 10
            return tens[(n / 10).toInt()] + (if (rem == 1L) " et un" else if (rem > 0) "-" + units[rem.toInt()] else "")
        }
        if (n < 100) return "nombre $n"
        if (n < 1000) {
            val h = n / 100
            val rem = n % 100
            val pref = if (h == 1L) "cent" else units[h.toInt()] + " cent"
            return pref + (if (rem != 0L) " " + frenchNumberToWords(rem) else "")
        }
        return "$n (en français)"
    }

    private fun hindiNumberToWords(n: Long): String {
        if (n < 0) return "ऋण " + hindiNumberToWords(-n)
        val hiWords = mapOf(
            1L to "एक", 2L to "दो", 3L to "तीन", 4L to "चार", 5L to "पाँच",
            6L to "छह", 7L to "सात", 8L to "आठ", 9L to "नौ", 10L to "दस",
            20L to "बीस", 30L to "तीस", 40L to "चालीस", 50L to "पचास",
            100L to "एक सौ", 1000L to "एक हज़ार"
        )
        if (hiWords.containsKey(n)) return hiWords[n]!!
        if (n < 100) return "संख्या $n"
        if (n < 1000) return hindiNumberToWords(n / 100) + " सौ " + (if (n % 100 != 0L) hindiNumberToWords(n % 100) else "")
        if (n < 100000) return hindiNumberToWords(n / 1000) + " हज़ार " + (if (n % 1000 != 0L) hindiNumberToWords(n % 1000) else "")
        return hindiNumberToWords(n / 100000) + " लाख " + (if (n % 100000 != 0L) hindiNumberToWords(n % 100000) else "")
    }

    private fun chineseNumberToWords(n: Long): String {
        if (n < 0) return "负 " + chineseNumberToWords(-n)
        val digits = listOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
        if (n in 0..9) return digits[n.toInt()]
        if (n in 10..19) return if (n == 10L) "十" else "十" + digits[(n % 10).toInt()]
        if (n in 20..99) return digits[(n / 10).toInt()] + "十" + (if (n % 10 != 0L) digits[(n % 10).toInt()] else "")
        if (n in 100..999) return digits[(n / 100).toInt()] + "百" + (if (n % 100 != 0L) chineseNumberToWords(n % 100) else "")
        if (n in 1000..9999) return digits[(n / 1000).toInt()] + "千" + (if (n % 1000 != 0L) chineseNumberToWords(n % 1000) else "")
        return "$n (中文)"
    }

    // Roman Numerals converter
    fun toRomanNumerals(number: Long): String {
        if (number <= 0 || number > 3999) return "N/A (Range 1-3999)"
        var n = number.toInt()
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val numerals = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
        val sb = StringBuilder()
        for (i in values.indices) {
            while (n >= values[i]) {
                n -= values[i]
                sb.append(numerals[i])
            }
        }
        return sb.toString()
    }
}
