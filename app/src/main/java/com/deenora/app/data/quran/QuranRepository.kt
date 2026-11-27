package com.deenora.app.data.quran

import android.content.Context
import com.deenora.app.ui.i18n.AppLanguage

enum class RevelationType {
    MAKKI, MADANI
}

data class Surah(
    val number: Int,
    val nameAr: String,
    val nameEn: String,
    val nameFr: String,
    val meaningEn: String,
    val meaningFr: String,
    val versesCount: Int,
    val revelationType: RevelationType,
    val juzStart: Int
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> nameAr
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.FRENCH -> nameFr
    }

    fun getMeaning(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> ""
        AppLanguage.ENGLISH -> meaningEn
        AppLanguage.FRENCH -> meaningFr
    }
}

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textAr: String,
    val textEn: String,
    val textFr: String,
    val tafsirSummary: String = ""
)

data class JuzInfo(
    val number: Int,
    val startSurahNumber: Int,
    val startAyahNumber: Int,
    val nameAr: String,
    val nameEn: String
)

object QuranRepository {

    val SURAHS: List<Surah> = listOf(
        Surah(1, "الفَاتِحَة", "Al-Fatihah", "Al-Fatiha", "The Opening", "L'Ouverture", 7, RevelationType.MAKKI, 1),
        Surah(2, "البَقَرَة", "Al-Baqarah", "Al-Baqara", "The Cow", "La Vache", 286, RevelationType.MADANI, 1),
        Surah(3, "آل عِمْرَان", "Ali 'Imran", "Al Imran", "Family of Imran", "La Famille d'Imran", 200, RevelationType.MADANI, 3),
        Surah(4, "النِّسَاء", "An-Nisa", "An-Nisa", "The Women", "Les Femmes", 176, RevelationType.MADANI, 4),
        Surah(5, "المَائِدَة", "Al-Ma'idah", "Al-Ma'ida", "The Table Spread", "La Table Servie", 120, RevelationType.MADANI, 6),
        Surah(6, "الأَنْعَام", "Al-An'am", "Al-An'am", "The Cattle", "Les Bestiaux", 165, RevelationType.MAKKI, 7),
        Surah(7, "الأَعْرَاف", "Al-A'raf", "Al-A'raf", "The Heights", "Les Murailles", 206, RevelationType.MAKKI, 8),
        Surah(8, "الأَنْفَال", "Al-Anfal", "Al-Anfal", "The Spoils of War", "Le Butin", 75, RevelationType.MADANI, 9),
        Surah(9, "التَّوْبَة", "At-Tawbah", "At-Tawba", "The Repentance", "Le Repentir", 129, RevelationType.MADANI, 10),
        Surah(10, "يُونُس", "Yunus", "Younous", "Jonah", "Jonas", 109, RevelationType.MAKKI, 11),
        Surah(11, "هُود", "Hud", "Houd", "Hud", "Houd", 123, RevelationType.MAKKI, 11),
        Surah(12, "يُوسُف", "Yusuf", "Youssouf", "Joseph", "Joseph", 111, RevelationType.MAKKI, 12),
        Surah(13, "الرَّعْد", "Ar-Ra'd", "Ar-Ra'd", "The Thunder", "Le Tonnerre", 43, RevelationType.MADANI, 13),
        Surah(14, "إِبْرَاهِيم", "Ibrahim", "Ibrahim", "Abraham", "Abraham", 52, RevelationType.MAKKI, 13),
        Surah(15, "الحِجْر", "Al-Hijr", "Al-Hijr", "The Rocky Tract", "La Vallée des Roches", 99, RevelationType.MAKKI, 14),
        Surah(16, "النَّحْل", "An-Nahl", "An-Nahl", "The Bee", "Les Abeilles", 128, RevelationType.MAKKI, 14),
        Surah(17, "الإِسْرَاء", "Al-Isra", "Al-Isra", "The Night Journey", "Le Voyage Nocturne", 111, RevelationType.MAKKI, 15),
        Surah(18, "الكَهْف", "Al-Kahf", "Al-Kahf", "The Cave", "La Caverne", 110, RevelationType.MAKKI, 15),
        Surah(19, "مَرْيَم", "Maryam", "Maryam", "Mary", "Marie", 98, RevelationType.MAKKI, 16),
        Surah(20, "طه", "Ta-Ha", "Ta-Ha", "Ta-Ha", "Ta-Ha", 135, RevelationType.MAKKI, 16),
        Surah(21, "الأَنْبِيَاء", "Al-Anbiya", "Al-Anbiya", "The Prophets", "Les Prophètes", 112, RevelationType.MAKKI, 17),
        Surah(22, "الحَجّ", "Al-Hajj", "Al-Hajj", "The Pilgrimage", "Le Pèlerinage", 78, RevelationType.MADANI, 17),
        Surah(23, "المُؤْمِنُون", "Al-Mu'minun", "Al-Mu'minoune", "The Believers", "Les Croyants", 118, RevelationType.MAKKI, 18),
        Surah(24, "النُّور", "An-Nur", "An-Nour", "The Light", "La Lumière", 64, RevelationType.MADANI, 18),
        Surah(25, "الفُرْقَان", "Al-Furqan", "Al-Fourqane", "The Criterion", "Le Discernement", 77, RevelationType.MAKKI, 18),
        Surah(26, "الشُّعَرَاء", "Ash-Shu'ara", "Ach-Chou'ara", "The Poets", "Les Poètes", 227, RevelationType.MAKKI, 19),
        Surah(27, "النَّمْل", "An-Naml", "An-Naml", "The Ant", "Les Fourmis", 93, RevelationType.MAKKI, 19),
        Surah(28, "القَصَص", "Al-Qasas", "Al-Qasas", "The Stories", "Le Récit", 88, RevelationType.MAKKI, 20),
        Surah(29, "العَنْكَبُوت", "Al-'Ankabut", "Al-'Ankabout", "The Spider", "L'Araignée", 69, RevelationType.MAKKI, 20),
        Surah(30, "الرُّوم", "Ar-Rum", "Ar-Roum", "The Romans", "Les Romains", 60, RevelationType.MAKKI, 21),
        Surah(31, "لُقْمَان", "Luqman", "Louqman", "Luqman", "Louqman", 34, RevelationType.MAKKI, 21),
        Surah(32, "السَّجْدَة", "As-Sajdah", "As-Sajda", "The Prostration", "La Prosternation", 30, RevelationType.MAKKI, 21),
        Surah(33, "الأَحْزَاب", "Al-Ahzab", "Al-Ahzab", "The Combined Forces", "Les Coalisés", 73, RevelationType.MADANI, 21),
        Surah(34, "سَبَأ", "Saba", "Saba", "Sheba", "Saba", 54, RevelationType.MAKKI, 22),
        Surah(35, "فَاطِر", "Fatir", "Fatir", "The Originator", "Le Créateur", 45, RevelationType.MAKKI, 22),
        Surah(36, "يس", "Ya-Sin", "Ya-Sin", "Ya-Sin", "Ya-Sin", 83, RevelationType.MAKKI, 22),
        Surah(37, "الصَّافَّات", "As-Saffat", "As-Saffat", "Those who set the Ranks", "Les Rangés", 182, RevelationType.MAKKI, 23),
        Surah(38, "ص", "Sad", "Sad", "The Letter Sad", "Sad", 88, RevelationType.MAKKI, 23),
        Surah(39, "الزُّمَر", "Az-Zumar", "Az-Zoumar", "The Troops", "Les Groupes", 75, RevelationType.MAKKI, 23),
        Surah(40, "غَافِر", "Ghafir", "Ghafir", "The Forgiver", "Le Pardonneur", 85, RevelationType.MAKKI, 24),
        Surah(41, "فُصِّلَت", "Fussilat", "Foussilat", "Explained in Detail", "Les Versets Détaillés", 54, RevelationType.MAKKI, 24),
        Surah(42, "الشُّورَى", "Ash-Shura", "Ach-Choura", "The Consultation", "La Consultation", 53, RevelationType.MAKKI, 25),
        Surah(43, "الزُّخْرُف", "Az-Zukhruf", "Az-Zoukhrouf", "The Ornaments of Gold", "L'Ornement", 89, RevelationType.MAKKI, 25),
        Surah(44, "الدُّخَان", "Ad-Dukhan", "Ad-Doukhan", "The Smoke", "La Fumée", 59, RevelationType.MAKKI, 25),
        Surah(45, "الجَاثِيَة", "Al-Jathiyah", "Al-Jathiya", "The Crouching", "L'Agenouillée", 37, RevelationType.MAKKI, 25),
        Surah(46, "الأَحْقَاف", "Al-Ahqaf", "Al-Ahqaf", "The Wind-Curved Sandhills", "Les Dunes", 35, RevelationType.MAKKI, 26),
        Surah(47, "مُحَمَّد", "Muhammad", "Mouhammad", "Muhammad", "Mouhammad", 38, RevelationType.MADANI, 26),
        Surah(48, "الفَتْح", "Al-Fath", "Al-Fath", "The Victory", "La Victoire Éclatante", 29, RevelationType.MADANI, 26),
        Surah(49, "الحُجُرَات", "Al-Hujurat", "Al-Houjourat", "The Rooms", "Les Appartements", 18, RevelationType.MADANI, 26),
        Surah(50, "ق", "Qaf", "Qaf", "The Letter Qaf", "Qaf", 45, RevelationType.MAKKI, 26),
        Surah(51, "الذَّارِيَات", "Adh-Dhariyat", "Adh-Dhariyat", "The Winnowing Winds", "Qui Éparpillent", 60, RevelationType.MAKKI, 26),
        Surah(52, "الطُّور", "At-Tur", "At-Tour", "The Mount", "Le Mont", 49, RevelationType.MAKKI, 27),
        Surah(53, "النَّجْم", "An-Najm", "An-Najm", "The Star", "L'Étoile", 62, RevelationType.MAKKI, 27),
        Surah(54, "القَمَر", "Al-Qamar", "Al-Qamar", "The Moon", "La Lune", 55, RevelationType.MAKKI, 27),
        Surah(55, "الرَّحْمَٰن", "Ar-Rahman", "Ar-Rahman", "The Beneficent", "Le Tout Miséricordieux", 78, RevelationType.MADANI, 27),
        Surah(56, "الوَاقِعَة", "Al-Waqi'ah", "Al-Waqi'a", "The Inevitable", "L'Événement", 96, RevelationType.MAKKI, 27),
        Surah(57, "الحَدِيد", "Al-Hadid", "Al-Hadid", "The Iron", "Le Fer", 29, RevelationType.MADANI, 27),
        Surah(58, "المُجَادَلَة", "Al-Mujadila", "Al-Moujadala", "The Pleading Woman", "La Discussion", 22, RevelationType.MADANI, 28),
        Surah(59, "الحَشْر", "Al-Hashr", "Al-Hachr", "The Exile", "L'Exode", 24, RevelationType.MADANI, 28),
        Surah(60, "المُمْتَحَنَة", "Al-Mumtahanah", "Al-Moumtahana", "She that is to be examined", "L'Éprouvée", 13, RevelationType.MADANI, 28),
        Surah(61, "الصَّفّ", "As-Saff", "As-Saff", "The Ranks", "Le Rang", 14, RevelationType.MADANI, 28),
        Surah(62, "الجُمُعَة", "Al-Jumu'ah", "Al-Joumou'a", "The Congregation", "Le Vendredi", 11, RevelationType.MADANI, 28),
        Surah(63, "المُنَافِقُون", "Al-Munafiqun", "Al-Mounafiqoune", "The Hypocrites", "Les Hypocrites", 11, RevelationType.MADANI, 28),
        Surah(64, "التَّغَابُن", "At-Taghabun", "At-Taghaboun", "Mutual Disillusion", "La Grande Perte", 18, RevelationType.MADANI, 28),
        Surah(65, "الطَّلَاق", "At-Talaq", "At-Talaq", "The Divorce", "Le Divorce", 12, RevelationType.MADANI, 28),
        Surah(66, "التَّحْرِيم", "At-Tahrim", "At-Tahrim", "The Prohibition", "L'Interdiction", 12, RevelationType.MADANI, 28),
        Surah(67, "المُلْك", "Al-Mulk", "Al-Moulk", "The Sovereignty", "La Royauté", 30, RevelationType.MAKKI, 29),
        Surah(68, "القَلَم", "Al-Qalam", "Al-Qalam", "The Pen", "La Plume", 52, RevelationType.MAKKI, 29),
        Surah(69, "الحَاقَّة", "Al-Haqqah", "Al-Haqqa", "The Reality", "L'Inévitable", 52, RevelationType.MAKKI, 29),
        Surah(70, "المَعَارِج", "Al-Ma'arij", "Al-Ma'arij", "The Ascending Stairways", "Les Voies d'Ascension", 44, RevelationType.MAKKI, 29),
        Surah(71, "نُوح", "Nuh", "Nouh", "Noah", "Noé", 28, RevelationType.MAKKI, 29),
        Surah(72, "الجِنّ", "Al-Jinn", "Al-Jinn", "The Jinn", "Les Djinns", 28, RevelationType.MAKKI, 29),
        Surah(73, "المُزَّمِّل", "Al-Muzzammil", "Al-Mouzzammil", "The Enshrouded One", "L'Enveloppé", 20, RevelationType.MAKKI, 29),
        Surah(74, "المُدَّثِّر", "Al-Muddaththir", "Al-Mouddathir", "The Cloaked One", "Le Revêtu d'un Manteau", 56, RevelationType.MAKKI, 29),
        Surah(75, "القِيَامَة", "Al-Qiyamah", "Al-Qiyama", "The Resurrection", "La Résurrection", 40, RevelationType.MAKKI, 29),
        Surah(76, "الإِنْسَان", "Al-Insan", "Al-Insan", "The Human", "L'Homme", 31, RevelationType.MADANI, 29),
        Surah(77, "المُرْسَلَات", "Al-Mursalat", "Al-Moursalat", "The Emissaries", "Les Envoyés", 50, RevelationType.MAKKI, 29),
        Surah(78, "النَّبَأ", "An-Naba", "An-Naba", "The Tidings", "La Nouvelle", 40, RevelationType.MAKKI, 30),
        Surah(79, "النَّازِعَات", "An-Nazi'at", "An-Nazi'at", "Those who drag forth", "Les Anges qui Arrachent", 46, RevelationType.MAKKI, 30),
        Surah(80, "عَبَسَ", "'Abasa", "'Abasa", "He Frowned", "Il s'est Renfrogné", 42, RevelationType.MAKKI, 30),
        Surah(81, "التَّكْوِير", "At-Takwir", "At-Takwir", "The Overthrowing", "L'Obscurcissement", 29, RevelationType.MAKKI, 30),
        Surah(82, "الانْفِطَار", "Al-Infitar", "Al-Infitar", "The Cleaving", "La Rupture", 19, RevelationType.MAKKI, 30),
        Surah(83, "المُطَفِّفِين", "Al-Mutaffifin", "Al-Moutaffifine", "The Defrauding", "Les Fraudeurs", 36, RevelationType.MAKKI, 30),
        Surah(84, "الانْشِقَاق", "Al-Inshiqaq", "Al-Inchiqaq", "The Splitting Open", "La Déchirure", 25, RevelationType.MAKKI, 30),
        Surah(85, "البُرُوج", "Al-Buruj", "Al-Bourouj", "The Mansions of the Stars", "Les Constellations", 22, RevelationType.MAKKI, 30),
        Surah(86, "الطَّارِق", "At-Tariq", "At-Tariq", "The Morning Star", "L'Astre Nocturne", 17, RevelationType.MAKKI, 30),
        Surah(87, "الأَعْلَى", "Al-A'la", "Al-A'la", "The Most High", "Le Très-Haut", 19, RevelationType.MAKKI, 30),
        Surah(88, "الغَاشِيَة", "Al-Ghashiyah", "Al-Ghashiya", "The Overwhelming", "L'Enveloppante", 26, RevelationType.MAKKI, 30),
        Surah(89, "الفَجْر", "Al-Fajr", "Al-Fajr", "The Dawn", "L'Aube", 30, RevelationType.MAKKI, 30),
        Surah(90, "البَلَد", "Al-Balad", "Al-Balad", "The City", "La Cité", 20, RevelationType.MAKKI, 30),
        Surah(91, "الشَّمْس", "Ash-Shams", "Ach-Chams", "The Sun", "Le Soleil", 15, RevelationType.MAKKI, 30),
        Surah(92, "اللَّيْل", "Al-Layl", "Al-Layl", "The Night", "La Nuit", 21, RevelationType.MAKKI, 30),
        Surah(93, "الضُّحَى", "Ad-Duha", "Ad-Douha", "The Morning Brightness", "Le Jour Montant", 11, RevelationType.MAKKI, 30),
        Surah(94, "الشَّرْح", "Ash-Sharh", "Ach-Charh", "The Relief", "L'Ouverture", 8, RevelationType.MAKKI, 30),
        Surah(95, "التِّين", "At-Tin", "At-Tin", "The Fig", "Le Figuier", 8, RevelationType.MAKKI, 30),
        Surah(96, "العَلَق", "Al-'Alaq", "Al-'Alaq", "The Clot", "L'Adhérence", 19, RevelationType.MAKKI, 30),
        Surah(97, "القَدْر", "Al-Qadr", "Al-Qadr", "The Decree", "La Destinée", 5, RevelationType.MAKKI, 30),
        Surah(98, "البَيِّنَة", "Al-Bayyinah", "Al-Bayyina", "The Clear Proof", "La Preuve", 8, RevelationType.MADANI, 30),
        Surah(99, "الزَّلْزَلَة", "Az-Zalzalah", "Az-Zalzala", "The Earthquake", "La Secousse", 8, RevelationType.MADANI, 30),
        Surah(100, "العَادِيَات", "Al-'Adiyat", "Al-'Adiyat", "The Courser", "Les Coursiers", 11, RevelationType.MAKKI, 30),
        Surah(101, "القَارِعَة", "Al-Qari'ah", "Al-Qari'a", "The Calamity", "Le Fracas", 11, RevelationType.MAKKI, 30),
        Surah(102, "التَّكَاثُر", "At-Takathur", "At-Takathour", "The Rivalry in World Increase", "La Course aux Richesses", 8, RevelationType.MAKKI, 30),
        Surah(103, "العَصْر", "Al-'Asr", "Al-'Asr", "The Declining Day", "Le Temps", 3, RevelationType.MAKKI, 30),
        Surah(104, "الهُمَزَة", "Al-Humazah", "Al-Houmaza", "The Traducer", "Les Calomniateurs", 9, RevelationType.MAKKI, 30),
        Surah(105, "الفِيل", "Al-Fil", "Al-Fil", "The Elephant", "L'Éléphant", 5, RevelationType.MAKKI, 30),
        Surah(106, "قُرَيْش", "Quraysh", "Quraych", "Quraysh", "Les Qoraïch", 4, RevelationType.MAKKI, 30),
        Surah(107, "المَاعُون", "Al-Ma'un", "Al-Ma'oun", "The Small Kindnesses", "L'Ustensile", 7, RevelationType.MAKKI, 30),
        Surah(108, "الكَوْثَر", "Al-Kawthar", "Al-Kawthar", "The Abundance", "L'Abondance", 3, RevelationType.MAKKI, 30),
        Surah(109, "الكَافِرُون", "Al-Kafirun", "Al-Kafiroune", "The Disbelievers", "Les Infidèles", 6, RevelationType.MAKKI, 30),
        Surah(110, "النَّصْر", "An-Nasr", "An-Nasr", "The Divine Support", "Les Secours", 3, RevelationType.MADANI, 30),
        Surah(111, "المَسَد", "Al-Masad", "Al-Masad", "The Palm Fiber", "Les Fibres", 5, RevelationType.MAKKI, 30),
        Surah(112, "الإِخْلَاص", "Al-Ikhlas", "Al-Ikhlas", "The Sincerity", "Le Monothéisme Pur", 4, RevelationType.MAKKI, 30),
        Surah(113, "الفَلَق", "Al-Falaq", "Al-Falaq", "The Daybreak", "L'Aube Naissante", 5, RevelationType.MAKKI, 30),
        Surah(114, "النَّاس", "An-Nas", "An-Nas", "Mankind", "Les Hommes", 6, RevelationType.MAKKI, 30)
    )

    val JUZ_LIST: List<JuzInfo> = (1..30).map { i ->
        val surahNum = when (i) {
            1 -> 1; 2 -> 2; 3 -> 2; 4 -> 3; 5 -> 4; 6 -> 4; 7 -> 5; 8 -> 6; 9 -> 7; 10 -> 8
            11 -> 9; 12 -> 11; 13 -> 12; 14 -> 15; 15 -> 17; 16 -> 18; 17 -> 21; 18 -> 23; 19 -> 25; 20 -> 27
            21 -> 29; 22 -> 33; 23 -> 36; 24 -> 39; 25 -> 41; 26 -> 46; 27 -> 51; 28 -> 58; 29 -> 67; 30 -> 78
            else -> 1
        }
        val startAyah = when (i) {
            2 -> 142; 3 -> 253; 4 -> 93; 5 -> 24; 6 -> 148; 7 -> 82; 8 -> 111; 9 -> 88; 10 -> 41
            11 -> 93; 12 -> 6; 13 -> 53; 14 -> 1; 15 -> 1; 16 -> 75; 17 -> 1; 18 -> 1; 19 -> 21; 20 -> 56
            21 -> 46; 22 -> 31; 23 -> 28; 24 -> 32; 25 -> 47; 26 -> 1; 27 -> 31; 28 -> 1; 29 -> 1; 30 -> 1
            else -> 1
        }
        JuzInfo(
            number = i,
            startSurahNumber = surahNum,
            startAyahNumber = startAyah,
            nameAr = "الجزء $i",
            nameEn = "Juz $i"
        )
    }

    fun getVersesForSurah(context: Context, surahNumber: Int): List<Ayah> {
        if (surahNumber !in 1..114) return emptyList()
        return try {
            val json = context.assets.open("quran/surah_${surahNumber}.json")
                .bufferedReader(Charsets.UTF_8).use { it.readText() }
            val root = org.json.JSONObject(json)
            val verses = root.getJSONObject("verse")
            val expected = SURAHS.first { it.number == surahNumber }.versesCount
            val result = (1..expected).map { number ->
                val key = "verse_${number}"
                require(verses.has(key)) { "Missing Quran verse $surahNumber:$number" }
                val text = verses.getString(key).trim()
                require(text.isNotEmpty()) { "Empty Quran verse $surahNumber:$number" }
                Ayah(surahNumber, number, text, "", "", "")
            }
            require(result.size == expected)
            result
        } catch (_: Exception) {
            // Never substitute fabricated text for Quranic verses.
            emptyList()
        }
    }

    // Daily verse cards are read directly from the same verified, bundled Uthmani text.
    // Keeping only references here prevents hand-copied or truncated Quran text.
    private val DAILY_VERSE_REFERENCES = listOf(
        2 to 255,
        2 to 286,
        94 to 5,
        3 to 139,
        13 to 28
    )

    fun getDailyVerse(context: Context): Ayah {
        val today = java.time.LocalDate.now()
        val index = Math.floorMod(today.toEpochDay(), DAILY_VERSE_REFERENCES.size.toLong()).toInt()
        val (surahNumber, ayahNumber) = DAILY_VERSE_REFERENCES[index]
        return getVersesForSurah(context, surahNumber)
            .firstOrNull { it.ayahNumber == ayahNumber }
            ?: Ayah(surahNumber, ayahNumber, "", "", "", "")
    }

}
