package com.example.domain.languages

data class WorldLanguage(
    val code: String,          // ISO 639-1 or 639-2/3
    val englishName: String,
    val nativeName: String,
    val countryOrRegion: String,
    val family: String,
    val numeralScript: NumeralScript = NumeralScript.LATIN
)

enum class NumeralScript(
    val displayName: String,
    val zeroToNine: String
) {
    LATIN("Western Arabic (0-9)", "0123456789"),
    ARABIC_INDIC("Eastern Arabic (٠-٩)", "٠١٢٣٤٥٦٧٨٩"),
    PERSIAN("Persian (۰-۹)", "۰۱۲۳۴۵۶۷۸۹"),
    BENGALI("Bengali (০-৯)", "০১২৩৪৫৬৭৮৯"),
    DEVANAGARI("Devanagari (०-९)", "०१२३४५६७८९"),
    GUJARATI("Gujarati (૦-૯)", "૦૧૨৩৪૫૬૭૮૯"),
    GURMUKHI("Gurmukhi / Punjabi (੦-੯)", "੦੧੨੩੪੫੬੭੮੯"),
    TAMIL("Tamil (௦-௯)", "௦௧௨௩௪௫௬௭௮௯"),
    TELUGU("Telugu (౦-౯)", "౦౧౨౩౪౫౬౭౮౯"),
    KANNADA("Kannada (೦-೯)", "೦೧೨৩৪၅೬೭༨೯"),
    MALAYALAM("Malayalam (൦-൯)", "൦൧൨൩൪൫൬൭൮൯"),
    THAI("Thai (๐-๙)", "๐๑๒๓၄၅၆๗၈๙"),
    KHMER("Khmer (០-៩)", "០១២៣៤၅៦៧៨៩"),
    MYANMAR("Burmese (၀-၉)", "၀၁၂၃၄၅၆၇၈၉"),
    LAO("Lao (໐-໙)", "໐໑໒໓໔໕໖໗໘໙"),
    TIBETAN("Tibetan (༠-༩)", "༠༡၂༣༤၅၆༧༨༩");

    fun convert(input: String): String {
        if (this == LATIN) return input
        val sb = StringBuilder()
        for (ch in input) {
            if (ch in '0'..'9') {
                val digitIndex = ch - '0'
                if (digitIndex in zeroToNine.indices) {
                    sb.append(zeroToNine[digitIndex])
                } else {
                    sb.append(ch)
                }
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }
}
