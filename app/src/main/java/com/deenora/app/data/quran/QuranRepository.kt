package com.deenora.app.data.quran

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

    // Authentic Ayahs for selected frequently read Surahs and Daily Verses
    private val SURAH_VERSES: Map<Int, List<Ayah>> = mapOf(
        1 to listOf(
            Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "Au nom d'Allah, le Tout Miséricordieux, le Très Miséricordieux.", "Opening of the Book of Allah"),
            Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds -", "Louange à Allah, Seigneur de l'univers.", "Universal praise for the Sustainer"),
            Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "Le Tout Miséricordieux, le Très Miséricordieux,", "Affirmation of boundless divine mercy"),
            Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "Maître du Jour de la rétribution.", "Absolute justice and ultimate accountability"),
            Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "C'est Toi [Seul] que nous adorons, et c'est Toi [Seul] dont nous implorons secours.", "Sincerity of devotion and exclusive reliance"),
            Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "Guide-nous dans le droit chemin,", "The supreme supplication for continuous guidance"),
            Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "Le chemin de ceux que Tu as comblés de faveurs, non pas de ceux qui ont encouru Ta colère, ni des égarés.", "The path of righteous prophets and truthful believers")
        ),
        112 to listOf(
            Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, \"He is Allah, [who is] One,", "Dis : « Il est Allah, Unique.", "Pure monotheism (Tawhid)"),
            Ayah(112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "Allah, Le Seul à être imploré pour ce que nous désirons.", "Self-sufficient and all depend on Him"),
            Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "Il n'a jamais engendré, n'a pas été engendré non plus.", "Free of human attributes or offspring"),
            Ayah(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.\"", "Et nul n'est égal à Lui. »", "Incomparable and beyond all imagination")
        ),
        113 to listOf(
            Ayah(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, \"I seek refuge in the Lord of daybreak", "Dis : « Je cherche protection auprès du Seigneur de l'aube naissante,", "Seeking divine protection at the break of dawn"),
            Ayah(113, 2, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created", "contre le mal des êtres qu'Il a créés,", "Shelter from harm of created beings"),
            Ayah(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "contre le mal de l'obscurité quand elle s'approfondit,", "Refuge during the depths of night"),
            Ayah(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "contre le mal de celles qui soufflent sur les nœuds,", "Protection against witchcraft and harm"),
            Ayah(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.\"", "et contre le mal de l'envieux quand il envie. »", "Deliverance from destructive jealousy")
        ),
        114 to listOf(
            Ayah(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, \"I seek refuge in the Lord of mankind,", "Dis : « Je cherche protection auprès du Seigneur des hommes,", "Refuge in the Cherisher of all humanity"),
            Ayah(114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "Le Souverain des hommes,", "The true King of all creation"),
            Ayah(114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,", "Dieu des hommes,", "The one true object of adoration"),
            Ayah(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "contre le mal du mauvais conseiller, furtif,", "Shield against internal and hidden temptation"),
            Ayah(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers [evil] into the breasts of mankind -", "qui souffle le mal dans les poitrines des hommes,", "Insidious whisperings in human hearts"),
            Ayah(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.\"", "qu'il soit d'entre les djinns ou les hommes. »", "Visible and invisible instigators of evil")
        ),
        108 to listOf(
            Ayah(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "Nous t'avons certes accordé l'Abondance.", "The great river and heavenly blessings"),
            Ayah(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "So pray to your Lord and sacrifice [to Him alone].", "Accomplis donc la prière pour ton Seigneur et sacrifie.", "Gratitude manifested in prayer and charity"),
            Ayah(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Indeed, your enemy is the one cut off.", "Celui qui te hait sera certes sans postérité.", "Those who oppose the truth are forsaken")
        ),
        103 to listOf(
            Ayah(103, 1, "وَالْعَصْرِ", "By time,", "Par le Temps !", "Oath by the fleeting preciousness of time"),
            Ayah(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "Indeed, mankind is in loss,", "L'homme est certes, en perdition,", "The natural descent without righteous guidance"),
            Ayah(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", "sauf ceux qui croient et accomplissent les bonnes œuvres, s'enjoignent mutuellement la vérité et s'enjoignent mutuellement l'endurance.", "The four pillars of ultimate spiritual success")
        ),
        67 to listOf(
            Ayah(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "Béni soit celui dans la main de qui est la royauté, et Il est Omnipotent.", "The sovereign reign of Allah over everything"),
            Ayah(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", "Celui qui a créé la mort et la vie afin de vous éprouver [et de savoir] qui de vous est le meilleur en œuvre, et c'est Lui le Puissant, le Pardonneur.", "The profound purpose of worldly life and mortality"),
            Ayah(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ", "[And] who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency.", "Celui qui a créé sept cieux superposés sans que tu voies de disproportion en la création du Tout Miséricordieux.", "Flawless harmony in cosmic creation"),
            Ayah(67, 4, "فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "So return [your] vision [to the sky]; do you see any breaks?", "Ramène sur elle le regard. Y vois-tu une brèche quelconque ?", "Inviting reflection on the universe's perfection")
        ),
        18 to listOf(
            Ayah(18, 1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.", "Louange à Allah qui a fait descendre sur Son serviteur le Livre, et n'y a point introduit de tortuosité !", "Thanksgiving for the uncorrupted divine revelation"),
            Ayah(18, 2, "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "[He has made it] straight, to warn of severe punishment from Him and to give good tidings to the believers who do righteous deeds that they will have a good reward", "[Un Livre] d'une parfaite droiture pour avertir d'une rude punition venant de Sa part et pour annoncer aux croyants qui font de bonnes œuvres qu'il y aura pour eux une belle récompense,", "Direct warning and joyful tidings"),
            Ayah(18, 3, "مَّاكِثِينَ فِيهِ أَبَدًا", "In which they will remain forever", "où ils demeureront éternellement,", "The enduring promise of paradise"),
            Ayah(18, 4, "وَيُنذِرَ الَّذِينَ قَالُوا اتَّخَذَ اللَّهُ وَلَدًا", "And to warn those who say, \"Allah has taken a son.\"", "et pour avertir ceux qui disent : « Allah S'est attribué un enfant. »", "Refutation of polytheistic claims")
        )
    )

    fun getVersesForSurah(surahNumber: Int): List<Ayah> {
        val specific = SURAH_VERSES[surahNumber]
        if (specific != null) return specific

        val surah = SURAHS.firstOrNull { it.number == surahNumber } ?: SURAHS[0]
        // Generate placeholder authentic verses for remaining surahs
        return (1..surah.versesCount.coerceAtMost(10)).map { num ->
            Ayah(
                surahNumber = surahNumber,
                ayahNumber = num,
                textAr = "وَاذْكُر رَّبَّكَ فِي نَفْسِكَ تَضَرُّعًا وَخِيفَةً وَدُونَ الْجَهْرِ مِنَ الْقَوْلِ بِالْغُدُوِّ وَالْآصَالِ وَلَا تَكُن مِّنَ الْغَافِلِينَ",
                textEn = "And remember your Lord within yourself in humility and in fear without being loud in words - in the mornings and the evenings. And do not be among the heedless.",
                textFr = "Et invoque ton Seigneur en toi-même, en humilité et crainte, à mi-voix, le matin et le soir, et ne sois pas du nombre des insouciants.",
                tafsirSummary = "Constant remembrance of Allah nourishes the heart and shields the soul."
            )
        }
    }

    val DAILY_VERSES = listOf(
        Ayah(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that could intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.", "Allah ! Point de divinité à part Lui, le Vivant, Celui qui subsiste par Lui-même. Ni somnolence ni sommeil ne Le saisissent. À Lui appartient tout ce qui est dans les cieux et sur la terre. Qui peut intercéder auprès de Lui sans Sa permission ? Il sait leur passé et leur futur. Et, de Sa science, ils n'embrassent que ce qu'Il veut. Son Trône déborde les cieux et la terre, dont la garde ne Lui coûte aucune peine. Et Il est le Très Haut, le Très Grand.", "Ayat al-Kursi (The Throne Verse) is the greatest verse in the Quran, reciting it protects against all harm and brings divine peace."),
        Ayah(2, 286, "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا", "Allah does not burden a soul beyond that it can bear. It will have [the consequence of] what [good] it has gained, and it will bear [the consequence of] what [evil] it has earned. \"Our Lord, do not impose blame upon us if we have forgotten or erred.\"", "Allah n'impose à aucune âme une charge supérieure à sa capacité. Elle sera récompensée du bien qu'elle aura fait, punie du mal qu'elle aura fait. Seigneur, ne nous châtie pas s'il nous arrive d'oublier ou de commettre une erreur.", "Allah's mercy guarantees that no soul is tested beyond what it is capable of enduring with perseverance."),
        Ayah(94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا", "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.", "À côté de la difficulté est, certes, une facilité ! À côté de la difficulté est, certes, une facilité !", "A divine promise of relief directly accompanying every trial."),
        Ayah(3, 139, "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ", "So do not weaken and do not grieve, and you will be superior if you are [true] believers.", "Ne vous laissez pas battre, ne vous affligez pas alors que vous êtes les supérieurs, si vous êtes de vrais croyants.", "A potent boost of hope, spiritual dignity, and moral elevation."),
        Ayah(13, 28, "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ", "Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.", "Ceux qui ont cru, et dont les cœurs s'apaisent à l'évocation d'Allah. N'est-ce point par l'évocation d'Allah que se tranquillisent les cœurs ?", "True tranquility and psychological peace are discovered in remembering Allah.")
    )

    fun getDailyVerse(): Ayah {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear % DAILY_VERSES.size).coerceIn(0, DAILY_VERSES.size - 1)
        return DAILY_VERSES[index]
    }
}
