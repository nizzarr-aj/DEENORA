package com.deenora.app.data.azkar

import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings

enum class ZikrCategory {
    MORNING,
    EVENING,
    AFTER_PRAYER,
    SLEEP,
    WAKEUP,
    MOSQUE,
    TRAVEL,
    PROTECTION;

    fun getTitle(lang: AppLanguage): String = when (this) {
        MORNING -> AppStrings.morningAzkar(lang)
        EVENING -> AppStrings.eveningAzkar(lang)
        AFTER_PRAYER -> AppStrings.afterPrayerAzkar(lang)
        SLEEP -> AppStrings.sleepAzkar(lang)
        WAKEUP -> AppStrings.wakeUpAzkar(lang)
        MOSQUE -> when (lang) {
            AppLanguage.ARABIC -> "أدعية المسجد"
            AppLanguage.ENGLISH -> "Mosque Duas"
            AppLanguage.FRENCH -> "Invocations de la Mosquée"
        }
        TRAVEL -> AppStrings.travelAzkar(lang)
        PROTECTION -> AppStrings.protectionAzkar(lang)
    }
}

data class ZikrItem(
    val id: String,
    val category: ZikrCategory,
    val arabicText: String,
    val transliteration: String,
    val translationEn: String,
    val translationFr: String,
    val targetCount: Int,
    val virtueAr: String,
    val virtueEn: String,
    val virtueFr: String,
    val reference: String
)

data class DailyDua(
    val titleAr: String,
    val titleEn: String,
    val titleFr: String,
    val arabicText: String,
    val transliteration: String,
    val translationEn: String,
    val translationFr: String,
    val source: String
)

data class IslamicReminder(
    val titleAr: String,
    val titleEn: String,
    val titleFr: String,
    val arabicText: String,
    val translationEn: String,
    val translationFr: String,
    val narrator: String
)

object AzkarRepository {

    val AZKAR_ITEMS: List<ZikrItem> = listOf(
        // Morning
        ZikrItem(
            id = "m_1",
            category = ZikrCategory.MORNING,
            arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ.",
            transliteration = "Asbahna wa-asbahal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadir.",
            translationEn = "We have entered the morning and kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without partner.",
            translationFr = "Nous voici au matin et la royauté appartient à Allah. Louange à Allah. Il n'y a de divinité digne d'adoration qu'Allah, Seul sans associé.",
            targetCount = 1,
            virtueAr = "من قالها حين يصبح أجير من الشيطان وحفظ يومه كله",
            virtueEn = "Reciting this in the morning protects from evil and grants blessings throughout the day",
            virtueFr = "Récitée au matin, elle accorde la bénédiction et la protection divine",
            reference = "صحيح مسلم"
        ),
        ZikrItem(
            id = "m_2",
            category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ.",
            transliteration = "Allahumma Anta Rabbi la ilaha illa Ant, khalaqtani wa ana 'abduk, wa ana 'ala 'ahdika wa wa'dika mastata't, a'udhu bika min sharri ma sana't, abu'u laka bini'matika 'alayya, wa abu'u laka bidhanbi faghfir li, fa-innahu la yaghfirudh-dhunuba illa Ant.",
            translationEn = "O Allah, You are my Lord, none has the right to be worshipped except You. You created me and I am Your servant, and I abide by Your covenant as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me and I acknowledge my sin, so forgive me, for none forgives sins except You.",
            translationFr = "Ô Allah ! Tu es mon Seigneur, nul n'est digne d'adoration si ce n'est Toi. Tu m'as créé et je suis Ton serviteur. Je me conforme à Ton pacte et à Ta promesse autant que je le puis. Je me réfugie auprès de Toi contre le mal que j'ai commis. Je reconnais Tes bienfaits sur moi et je reconnais mon péché. Pardonne-moi donc, car nul ne pardonne les péchés si ce n'est Toi.",
            targetCount = 1,
            virtueAr = "سيد الاستغفار: من قاله موقناً به فمات من يومه أو ليلته دخل الجنة",
            virtueEn = "Sayyid al-Istighfar (Chief prayer for repentance): Whoever says it with certainty and dies that day enters Paradise",
            virtueFr = "Le maître des demandes de pardon : Quiconque le prononce avec foi et meurt ce jour-là entrera au Paradis",
            reference = "صحيح البخاري"
        ),
        ZikrItem(
            id = "m_3",
            category = ZikrCategory.MORNING,
            arabicText = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
            transliteration = "Bismillahilladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Alim.",
            translationEn = "In the Name of Allah, with Whose Name nothing upon earth or in the heavens can cause harm, and He is the All-Hearing, the All-Knowing.",
            translationFr = "Au nom d'Allah, tel qu'en compagnie de Son Nom rien ne peut nuire sur terre ni dans le ciel, et Il est l'Audient, l'Omniscient.",
            targetCount = 3,
            virtueAr = "لم يضره شيء حتى يمسي",
            virtueEn = "Nothing will harm the one who recites it 3 times until the evening",
            virtueFr = "Rien ne saurait lui nuire jusqu'au soir",
            reference = "سنن الترمذي وأبو داود"
        ),
        ZikrItem(
            id = "m_4",
            category = ZikrCategory.MORNING,
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
            transliteration = "Subhanallahi wa bihamdihi, 'adada khalqihi, wa rida nafsihi, wa zinata 'arshihi, wa midada kalimatih.",
            translationEn = "Glory is to Allah and praise is to Him, by the number of His creation and by His pleasure and by the weight of His Throne and by the ink of His words.",
            translationFr = "Gloire et louange à Allah, autant de fois qu'il y a de créatures, autant qu'Il Lui plaît, à la mesure du poids de Son Trône et du volume de Ses paroles.",
            targetCount = 3,
            virtueAr = "تعدل ساعات طويلة من الذكر والتسبيح",
            virtueEn = "Outweighs hours of continuous remembrance and dhikr",
            virtueFr = "Pèse plus lourd que de longues heures d'évocation",
            reference = "صحيح مسلم"
        ),


        ZikrItem(
            id = "m_5", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ.",
            transliteration = "Allahumma bika asbahna, wa bika amsayna, wa bika nahya, wa bika namutu, wa ilaykan-nushur.",
            translationEn = "O Allah, by You we enter the morning and evening, by You we live and die, and to You is the resurrection.",
            translationFr = "Ô Allah, c'est par Toi que nous entrons dans le matin et le soir, que nous vivons et mourons, et vers Toi est la résurrection.",
            targetCount = 1, virtueAr = "تجديد التوكل على الله", virtueEn = "Renews reliance upon Allah", virtueFr = "Renouvelle la confiance en Allah",
            reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_6", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ.",
            transliteration = "Allahumma inni as'alukal-'afwa wal-'afiyata fid-dunya wal-akhirah.",
            translationEn = "O Allah, I ask You for pardon and well-being in this world and the Hereafter.",
            translationFr = "Ô Allah, je Te demande le pardon et le bien-être ici-bas et dans l'au-delà.",
            targetCount = 1, virtueAr = "سؤال العفو والعافية", virtueEn = "A supplication for pardon and well-being", virtueFr = "Demande de pardon et de bien-être",
            reference = "سنن أبي داود وابن ماجه"
        ),
        ZikrItem(
            id = "m_7", category = ZikrCategory.MORNING,
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
            transliteration = "Raditu billahi Rabban, wa bil-Islami dinan, wa bi-Muhammadin nabiyyan.",
            translationEn = "I am pleased with Allah as Lord, Islam as religion, and Muhammad ﷺ as Prophet.",
            translationFr = "J'agrée Allah comme Seigneur, l'islam comme religion et Muhammad ﷺ comme Prophète.",
            targetCount = 3, virtueAr = "ورد في فضلها وعد بالرضا", virtueEn = "A morning remembrance affirming faith", virtueFr = "Un rappel matinal affirmant la foi",
            reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_8", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ عَالِمَ الْغَيْبِ وَالشَّهَادَةِ، فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا أَنْتَ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي وَمِنْ شَرِّ الشَّيْطَانِ وَشِرْكِهِ.",
            transliteration = "Allahumma 'alimal-ghaybi wash-shahadah, fatiras-samawati wal-ard, Rabba kulli shay'in wa malikah, ash-hadu an la ilaha illa Ant, a'udhu bika min sharri nafsi wa min sharrish-shaytani wa shirkih.",
            translationEn = "O Allah, Knower of the unseen and seen, Creator of the heavens and earth, Lord and Sovereign of everything, I seek refuge in You from the evil of my soul and from the evil and traps of Satan.",
            translationFr = "Ô Allah, Connaisseur de l'invisible et du visible, Créateur des cieux et de la terre, Seigneur de toute chose, je cherche refuge auprès de Toi contre le mal de mon âme et celui de Satan.",
            targetCount = 1, virtueAr = "استعاذة بالله من شر النفس والشيطان", virtueEn = "Seeks Allah's protection from personal and satanic evil", virtueFr = "Demande la protection contre le mal de l'âme et de Satan",
            reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_9", category = ZikrCategory.MORNING,
            arabicText = "يَا حَيُّ يَا قَيُّومُ، بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
            transliteration = "Ya Hayyu ya Qayyum, bi-rahmatika astaghith, aslih li sha'ni kullah, wa la takilni ila nafsi tarfata 'ayn.",
            translationEn = "O Ever-Living, O Sustainer, by Your mercy I seek help; set all my affairs right and do not leave me to myself even for the blink of an eye.",
            translationFr = "Ô Vivant, ô Celui qui subsiste par Lui-même, par Ta miséricorde je demande secours ; améliore toutes mes affaires et ne me laisse pas à moi-même, ne serait-ce qu'un instant.",
            targetCount = 1, virtueAr = "دعاء للاستعانة بالله وإصلاح الشأن", virtueEn = "Asks Allah for help and sound affairs", virtueFr = "Demande l'aide d'Allah et l'amélioration des affaires",
            reference = "المستدرك للحاكم"
        ),
        ZikrItem(
            id = "m_10", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ إِنِّي أَصْبَحْتُ أُشْهِدُكَ وَأُشْهِدُ حَمَلَةَ عَرْشِكَ وَمَلَائِكَتَكَ وَجَمِيعَ خَلْقِكَ أَنَّكَ أَنْتَ اللَّهُ لَا إِلَٰهَ إِلَّا أَنْتَ وَحْدَكَ لَا شَرِيكَ لَكَ، وَأَنَّ مُحَمَّدًا عَبْدُكَ وَرَسُولُكَ.",
            transliteration = "Allahumma inni asbahtu ush-hiduka wa ush-hidu hamalata 'arshika wa mala'ikataka wa jami'a khalqika annaka Antallahu la ilaha illa Anta wahdaka la sharika lak, wa anna Muhammadan 'abduka wa rasuluk.",
            translationEn = "O Allah, this morning I call You, the bearers of Your Throne, Your angels and all creation to witness that You are Allah, with no deity but You, alone without partner, and Muhammad is Your servant and messenger.",
            translationFr = "Ô Allah, ce matin je Te prends à témoin, ainsi que les porteurs de Ton Trône, Tes anges et toute Ta création, que Tu es Allah, Seul sans associé, et que Muhammad est Ton serviteur et messager.",
            targetCount = 4, virtueAr = "ورد في أذكار الصباح", virtueEn = "A traditional morning testimony of faith", virtueFr = "Témoignage de foi traditionnel du matin",
            reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "m_11", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ مَا أَصْبَحَ بِي مِنْ نِعْمَةٍ أَوْ بِأَحَدٍ مِنْ خَلْقِكَ فَمِنْكَ وَحْدَكَ لَا شَرِيكَ لَكَ، فَلَكَ الْحَمْدُ وَلَكَ الشُّكْرُ.",
            transliteration = "Allahumma ma asbaha bi min ni'matin aw bi-ahadin min khalqika fa-minka wahdaka la sharika lak, falakal-hamdu wa lakash-shukr.",
            translationEn = "O Allah, whatever blessing has come to me or any of Your creation this morning is from You alone, without partner; to You belongs all praise and thanks.",
            translationFr = "Ô Allah, tout bienfait reçu ce matin par moi ou par l'une de Tes créatures vient de Toi seul ; à Toi la louange et la gratitude.",
            targetCount = 1, virtueAr = "شكر الله على نعمه", virtueEn = "Expresses gratitude for Allah's blessings", virtueFr = "Exprime la gratitude pour les bienfaits d'Allah",
            reference = "سنن أبي داود"
        ),

        ZikrItem(
            id = "m_12", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ...", transliteration = "Allahu la ilaha illa Huwa al-Hayyul-Qayyum...",
            translationEn = "Allah—there is no deity except Him, the Ever-Living, the Sustainer. (Ayat al-Kursi; full verse should be displayed in the app.)", translationFr = "Allah, nulle divinité en dehors de Lui, le Vivant, Celui qui subsiste par Lui-même. (Verset du Trône.)", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "آية الكرسي من سورة البقرة"
        ),
        ZikrItem(
            id = "m_13", category = ZikrCategory.MORNING,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", transliteration = "Qul huwa Allahu ahad, Allahus-samad...",
            translationEn = "Say: He is Allah, One; Allah, the Eternal Refuge. (Surah Al-Ikhlas.)", translationFr = "Dis : Il est Allah, Unique, le Seul à être imploré. (Sourate Al-Ikhlas.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الإخلاص؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_14", category = ZikrCategory.MORNING,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ مِن شَرِّ مَا خَلَقَ...", transliteration = "Qul a'udhu bi-Rabbil-falaq...",
            translationEn = "Say: I seek refuge in the Lord of daybreak. (Surah Al-Falaq.)", translationFr = "Dis : Je cherche protection auprès du Seigneur de l’aube. (Sourate Al-Falaq.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الفلق؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_15", category = ZikrCategory.MORNING,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ مَلِكِ النَّاسِ إِلَٰهِ النَّاسِ...", transliteration = "Qul a'udhu bi-Rabbin-nas, Malikin-nas...",
            translationEn = "Say: I seek refuge in the Lord of mankind. (Surah An-Nas.)", translationFr = "Dis : Je cherche protection auprès du Seigneur des hommes. (Sourate An-Nas.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الناس؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "m_16", category = ZikrCategory.MORNING,
            arabicText = "أَصْبَحْنَا عَلَىٰ فِطْرَةِ الْإِسْلَامِ وَكَلِمَةِ الْإِخْلَاصِ وَدِينِ نَبِيِّنَا مُحَمَّدٍ ﷺ وَمِلَّةِ أَبِينَا إِبْرَاهِيمَ حَنِيفًا مُسْلِمًا وَمَا كَانَ مِنَ الْمُشْرِكِينَ", transliteration = "Asbahna 'ala fitratil-Islam...",
            translationEn = "We have entered the morning upon the natural way of Islam, the word of sincerity, the religion of our Prophet Muhammad, and the way of Abraham, upright and submitting to Allah.", translationFr = "Nous voici au matin sur la disposition naturelle de l’islam, la parole de sincérité, la religion de notre Prophète Muhammad et la voie d’Abraham.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "مسند أحمد"
        ),
        ZikrItem(
            id = "m_17", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا وَعَمَلًا مُتَقَبَّلًا", transliteration = "Allahumma inni as'aluka 'ilman nafi'an wa rizqan tayyiban wa 'amalan mutaqabbalan.",
            translationEn = "O Allah, I ask You for beneficial knowledge, good provision, and accepted deeds.", translationFr = "Ô Allah, je Te demande une science utile, une subsistance bonne et des œuvres agréées.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن ابن ماجه"
        ),
        ZikrItem(
            id = "m_18", category = ZikrCategory.MORNING,
            arabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ", transliteration = "Hasbiyallahu la ilaha illa Huwa, 'alayhi tawakkaltu...",
            translationEn = "Allah is sufficient for me; there is no deity except Him. In Him I put my trust, and He is Lord of the Mighty Throne.", translationFr = "Allah me suffit. Nulle divinité en dehors de Lui. Je place en Lui ma confiance et Il est le Seigneur du Trône immense.", targetCount = 7,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود؛ ورد في أذكار الصباح"
        ),
        ZikrItem(
            id = "m_19", category = ZikrCategory.MORNING,
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", transliteration = "Subhanallahi wa bihamdih.",
            translationEn = "Glory and praise be to Allah.", translationFr = "Gloire et louange à Allah.", targetCount = 100,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري ومسلم"
        ),
        ZikrItem(
            id = "m_20", category = ZikrCategory.MORNING,
            arabicText = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", transliteration = "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd...",
            translationEn = "None has the right to be worshipped but Allah alone, without partner; His is the dominion and praise, and He is capable of all things.", translationFr = "Nulle divinité ne mérite d’être adorée sauf Allah, Seul sans associé ; à Lui la royauté et la louange, et Il est capable de toute chose.", targetCount = 10,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري ومسلم"
        ),
        ZikrItem(
            id = "m_21", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ", transliteration = "Allahumma ajirni minan-nar.",
            translationEn = "O Allah, protect me from the Fire.", translationFr = "Ô Allah, protège-moi du Feu.", targetCount = 7,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "ورد في بعض روايات السنن؛ يلزم التحقق من درجة الحديث"
        ),
        ZikrItem(
            id = "m_22", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْجُبْنِ وَالْبُخْلِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ", transliteration = "Allahumma inni a'udhu bika minal-hammi wal-hazan...",
            translationEn = "O Allah, I seek refuge in You from anxiety and grief, incapacity and laziness, cowardice and miserliness, the burden of debt and being overpowered by people.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre l’angoisse et la tristesse, l’incapacité et la paresse, la lâcheté et l’avarice, le poids des dettes et la domination des hommes.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري"
        ),
        ZikrItem(
            id = "m_23", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ", transliteration = "Allahumma 'afini fi badani, Allahumma 'afini fi sam'i, Allahumma 'afini fi basari, la ilaha illa Ant.",
            translationEn = "O Allah, grant health to my body, hearing, and sight; there is no deity except You.", translationFr = "Ô Allah, accorde la santé à mon corps, à mon ouïe et à ma vue. Nulle divinité en dehors de Toi.", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "m_24", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، وَأَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ", transliteration = "Allahumma inni a'udhu bika minal-kufri wal-faqr...",
            translationEn = "O Allah, I seek refuge in You from disbelief and poverty, and from the punishment of the grave; there is no deity except You.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre l’incroyance, la pauvreté et le châtiment de la tombe.", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "m_25", category = ZikrCategory.MORNING,
            arabicText = "اللَّهُمَّ فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ، عَالِمَ الْغَيْبِ وَالشَّهَادَةِ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي", transliteration = "Allahumma fatiras-samawati wal-ard...",
            translationEn = "O Allah, Creator of the heavens and earth, Knower of the unseen and seen, Lord and Sovereign of all things, I seek refuge in You from the evil of my own soul.", translationFr = "Ô Allah, Créateur des cieux et de la terre, Connaisseur de l’invisible et du visible, je cherche refuge auprès de Toi contre le mal de mon âme.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود والترمذي"
        ),

        // Evening
        ZikrItem(
            id = "e_1",
            category = ZikrCategory.EVENING,
            arabicText = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ.",
            transliteration = "Amsayna wa-amsal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadir.",
            translationEn = "We have reached the evening and the kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without partner.",
            translationFr = "Nous voici au soir et la royauté appartient à Allah. Louange à Allah. Nulle divinité ne mérite d'être adorée en dehors d'Allah, Seul et sans associé.",
            targetCount = 1,
            virtueAr = "تحفظ العبد طوال ليلته",
            virtueEn = "Protects the servant through the night",
            virtueFr = "Protège le serviteur tout au long de sa nuit",
            reference = "صحيح مسلم"
        ),
        ZikrItem(
            id = "e_2",
            category = ZikrCategory.EVENING,
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
            transliteration = "A'udhu bi-kalimatil-lahit-tammati min sharri ma khalaq.",
            translationEn = "I seek refuge in the Perfect Words of Allah from the evil of what He has created.",
            translationFr = "Je cherche refuge auprès des paroles parfaites d'Allah contre le mal de ce qu'Il a créé.",
            targetCount = 3,
            virtueAr = "لم تضره حمة ولا لدغة في تلك الليلة",
            virtueEn = "No harm or sting will touch the one who says this 3 times in the evening",
            virtueFr = "Aucune créature venimeuse ni nuisance ne pourra lui faire de mal cette nuit-là",
            reference = "صحيح مسلم"
        ),


        ZikrItem(
            id = "e_3", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ.",
            transliteration = "Allahumma bika amsayna, wa bika asbahna, wa bika nahya, wa bika namutu, wa ilaykal-masir.",
            translationEn = "O Allah, by You we enter the evening and morning, by You we live and die, and to You is the final return.",
            translationFr = "Ô Allah, c'est par Toi que nous entrons dans le soir et le matin, que nous vivons et mourons, et vers Toi est le retour final.",
            targetCount = 1, virtueAr = "تجديد التوكل على الله", virtueEn = "Renews reliance upon Allah", virtueFr = "Renouvelle la confiance en Allah",
            reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_4", category = ZikrCategory.EVENING,
            arabicText = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
            transliteration = "Raditu billahi Rabban, wa bil-Islami dinan, wa bi-Muhammadin nabiyyan.",
            translationEn = "I am pleased with Allah as Lord, Islam as religion, and Muhammad ﷺ as Prophet.",
            translationFr = "J'agrée Allah comme Seigneur, l'islam comme religion et Muhammad ﷺ comme Prophète.",
            targetCount = 3, virtueAr = "ورد في فضلها وعد بالرضا", virtueEn = "An evening remembrance affirming faith", virtueFr = "Un rappel du soir affirmant la foi",
            reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_5", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَمْسَيْتُ أُشْهِدُكَ وَأُشْهِدُ حَمَلَةَ عَرْشِكَ وَمَلَائِكَتَكَ وَجَمِيعَ خَلْقِكَ أَنَّكَ أَنْتَ اللَّهُ لَا إِلَٰهَ إِلَّا أَنْتَ وَحْدَكَ لَا شَرِيكَ لَكَ، وَأَنَّ مُحَمَّدًا عَبْدُكَ وَرَسُولُكَ.",
            transliteration = "Allahumma inni amsaytu ush-hiduka wa ush-hidu hamalata 'arshika wa mala'ikataka wa jami'a khalqika annaka Antallahu la ilaha illa Anta wahdaka la sharika lak, wa anna Muhammadan 'abduka wa rasuluk.",
            translationEn = "O Allah, this evening I call You, the bearers of Your Throne, Your angels and all creation to witness that You are Allah, alone without partner, and Muhammad is Your servant and messenger.",
            translationFr = "Ô Allah, ce soir je Te prends à témoin, ainsi que les porteurs de Ton Trône, Tes anges et toute Ta création, que Tu es Allah, Seul sans associé, et que Muhammad est Ton serviteur et messager.",
            targetCount = 4, virtueAr = "ورد في أذكار المساء", virtueEn = "A traditional evening testimony of faith", virtueFr = "Témoignage de foi traditionnel du soir",
            reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "e_6", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ مَا أَمْسَى بِي مِنْ نِعْمَةٍ أَوْ بِأَحَدٍ مِنْ خَلْقِكَ فَمِنْكَ وَحْدَكَ لَا شَرِيكَ لَكَ، فَلَكَ الْحَمْدُ وَلَكَ الشُّكْرُ.",
            transliteration = "Allahumma ma amsa bi min ni'matin aw bi-ahadin min khalqika fa-minka wahdaka la sharika lak, falakal-hamdu wa lakash-shukr.",
            translationEn = "O Allah, whatever blessing has come to me or any of Your creation this evening is from You alone, without partner; to You belongs all praise and thanks.",
            translationFr = "Ô Allah, tout bienfait reçu ce soir par moi ou par l'une de Tes créatures vient de Toi seul ; à Toi la louange et la gratitude.",
            targetCount = 1, virtueAr = "شكر الله على نعمه", virtueEn = "Expresses gratitude for Allah's blessings", virtueFr = "Exprime la gratitude pour les bienfaits d'Allah",
            reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "e_7", category = ZikrCategory.EVENING,
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ غَضَبِهِ وَعِقَابِهِ، وَشَرِّ عِبَادِهِ، وَمِنْ هَمَزَاتِ الشَّيَاطِينِ وَأَنْ يَحْضُرُونِ.",
            transliteration = "A'udhu bi-kalimatillahit-tammati min ghadabihi wa 'iqabih, wa sharri 'ibadih, wa min hamazatish-shayatini wa an yahdurun.",
            translationEn = "I seek refuge in the perfect words of Allah from His anger and punishment, from the evil of His servants, and from the whispers of devils and their presence.",
            translationFr = "Je cherche refuge dans les paroles parfaites d'Allah contre Sa colère et Son châtiment, le mal de Ses serviteurs, les suggestions des démons et leur présence.",
            targetCount = 1, virtueAr = "دعاء للاستعاذة من الشر", virtueEn = "A supplication seeking refuge from harm", virtueFr = "Invocation de protection contre le mal",
            reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "e_8", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ.",
            transliteration = "Allahumma inni as'alukal-'afwa wal-'afiyata fid-dunya wal-akhirah.",
            translationEn = "O Allah, I ask You for pardon and well-being in this world and the Hereafter.",
            translationFr = "Ô Allah, je Te demande le pardon et le bien-être ici-bas et dans l'au-delà.",
            targetCount = 1, virtueAr = "سؤال العفو والعافية", virtueEn = "A supplication for pardon and well-being", virtueFr = "Demande de pardon et de bien-être",
            reference = "سنن أبي داود وابن ماجه"
        ),
        ZikrItem(
            id = "e_9", category = ZikrCategory.EVENING,
            arabicText = "يَا حَيُّ يَا قَيُّومُ، بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
            transliteration = "Ya Hayyu ya Qayyum, bi-rahmatika astaghith, aslih li sha'ni kullah, wa la takilni ila nafsi tarfata 'ayn.",
            translationEn = "O Ever-Living, O Sustainer, by Your mercy I seek help; set all my affairs right and do not leave me to myself even for the blink of an eye.",
            translationFr = "Ô Vivant, ô Celui qui subsiste par Lui-même, par Ta miséricorde je demande secours ; améliore toutes mes affaires et ne me laisse pas à moi-même, ne serait-ce qu'un instant.",
            targetCount = 1, virtueAr = "دعاء للاستعانة بالله وإصلاح الشأن", virtueEn = "Asks Allah for help and sound affairs", virtueFr = "Demande l'aide d'Allah et l'amélioration des affaires",
            reference = "المستدرك للحاكم"
        ),

        ZikrItem(
            id = "e_10", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ...", transliteration = "Allahu la ilaha illa Huwa al-Hayyul-Qayyum...",
            translationEn = "Allah—there is no deity except Him, the Ever-Living, the Sustainer. (Ayat al-Kursi; full verse should be displayed in the app.)", translationFr = "Allah, nulle divinité en dehors de Lui, le Vivant, Celui qui subsiste par Lui-même. (Verset du Trône.)", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "آية الكرسي من سورة البقرة"
        ),
        ZikrItem(
            id = "e_11", category = ZikrCategory.EVENING,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", transliteration = "Qul huwa Allahu ahad, Allahus-samad...",
            translationEn = "Say: He is Allah, One; Allah, the Eternal Refuge. (Surah Al-Ikhlas.)", translationFr = "Dis : Il est Allah, Unique, le Seul à être imploré. (Sourate Al-Ikhlas.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الإخلاص؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_12", category = ZikrCategory.EVENING,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ مِن شَرِّ مَا خَلَقَ...", transliteration = "Qul a'udhu bi-Rabbil-falaq...",
            translationEn = "Say: I seek refuge in the Lord of daybreak. (Surah Al-Falaq.)", translationFr = "Dis : Je cherche protection auprès du Seigneur de l’aube. (Sourate Al-Falaq.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الفلق؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_13", category = ZikrCategory.EVENING,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ مَلِكِ النَّاسِ إِلَٰهِ النَّاسِ...", transliteration = "Qul a'udhu bi-Rabbin-nas, Malikin-nas...",
            translationEn = "Say: I seek refuge in the Lord of mankind. (Surah An-Nas.)", translationFr = "Dis : Je cherche protection auprès du Seigneur des hommes. (Sourate An-Nas.)", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سورة الناس؛ سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_14", category = ZikrCategory.EVENING,
            arabicText = "أَمْسَيْنَا عَلَىٰ فِطْرَةِ الْإِسْلَامِ وَكَلِمَةِ الْإِخْلَاصِ وَدِينِ نَبِيِّنَا مُحَمَّدٍ ﷺ وَمِلَّةِ أَبِينَا إِبْرَاهِيمَ حَنِيفًا مُسْلِمًا وَمَا كَانَ مِنَ الْمُشْرِكِينَ", transliteration = "Amsayna 'ala fitratil-Islam...",
            translationEn = "We have entered the evening upon the natural way of Islam, the word of sincerity, the religion of our Prophet Muhammad, and the way of Abraham, upright and submitting to Allah.", translationFr = "Nous voici au soir sur la disposition naturelle de l’islam, la parole de sincérité, la religion de notre Prophète Muhammad et la voie d’Abraham.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "مسند أحمد"
        ),
        ZikrItem(
            id = "e_15", category = ZikrCategory.EVENING,
            arabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ", transliteration = "Hasbiyallahu la ilaha illa Huwa, 'alayhi tawakkaltu...",
            translationEn = "Allah is sufficient for me; there is no deity except Him. In Him I put my trust, and He is Lord of the Mighty Throne.", translationFr = "Allah me suffit. Nulle divinité en dehors de Lui. Je place en Lui ma confiance et Il est le Seigneur du Trône immense.", targetCount = 7,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود؛ ورد في أذكار المساء"
        ),
        ZikrItem(
            id = "e_16", category = ZikrCategory.EVENING,
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", transliteration = "Subhanallahi wa bihamdih.",
            translationEn = "Glory and praise be to Allah.", translationFr = "Gloire et louange à Allah.", targetCount = 100,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري ومسلم"
        ),
        ZikrItem(
            id = "e_17", category = ZikrCategory.EVENING,
            arabicText = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", transliteration = "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd...",
            translationEn = "None has the right to be worshipped but Allah alone, without partner; His is the dominion and praise, and He is capable of all things.", translationFr = "Nulle divinité ne mérite d’être adorée sauf Allah, Seul sans associé ; à Lui la royauté et la louange, et Il est capable de toute chose.", targetCount = 10,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري ومسلم"
        ),
        ZikrItem(
            id = "e_18", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ أَجِرْنِي مِنَ النَّارِ", transliteration = "Allahumma ajirni minan-nar.",
            translationEn = "O Allah, protect me from the Fire.", translationFr = "Ô Allah, protège-moi du Feu.", targetCount = 7,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "ورد في بعض روايات السنن؛ يلزم التحقق من درجة الحديث"
        ),
        ZikrItem(
            id = "e_19", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْجُبْنِ وَالْبُخْلِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ", transliteration = "Allahumma inni a'udhu bika minal-hammi wal-hazan...",
            translationEn = "O Allah, I seek refuge in You from anxiety and grief, incapacity and laziness, cowardice and miserliness, the burden of debt and being overpowered by people.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre l’angoisse et la tristesse, l’incapacité et la paresse, la lâcheté et l’avarice, le poids des dettes et la domination des hommes.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح البخاري"
        ),
        ZikrItem(
            id = "e_20", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَٰهَ إِلَّا أَنْتَ", transliteration = "Allahumma 'afini fi badani, Allahumma 'afini fi sam'i, Allahumma 'afini fi basari, la ilaha illa Ant.",
            translationEn = "O Allah, grant health to my body, hearing, and sight; there is no deity except You.", translationFr = "Ô Allah, accorde la santé à mon corps, à mon ouïe et à ma vue. Nulle divinité en dehors de Toi.", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "e_21", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْكُفْرِ وَالْفَقْرِ، وَأَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ، لَا إِلَٰهَ إِلَّا أَنْتَ", transliteration = "Allahumma inni a'udhu bika minal-kufri wal-faqr...",
            translationEn = "O Allah, I seek refuge in You from disbelief and poverty, and from the punishment of the grave; there is no deity except You.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre l’incroyance, la pauvreté et le châtiment de la tombe.", targetCount = 3,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود"
        ),
        ZikrItem(
            id = "e_22", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ فَاطِرَ السَّمَاوَاتِ وَالْأَرْضِ، عَالِمَ الْغَيْبِ وَالشَّهَادَةِ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي", transliteration = "Allahumma fatiras-samawati wal-ard...",
            translationEn = "O Allah, Creator of the heavens and earth, Knower of the unseen and seen, Lord and Sovereign of all things, I seek refuge in You from the evil of my own soul.", translationFr = "Ô Allah, Créateur des cieux et de la terre, Connaisseur de l’invisible et du visible, je cherche refuge auprès de Toi contre le mal de mon âme.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن أبي داود والترمذي"
        ),
        ZikrItem(
            id = "e_23", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا وَعَمَلًا مُتَقَبَّلًا", transliteration = "Allahumma inni as'aluka 'ilman nafi'an wa rizqan tayyiban wa 'amalan mutaqabbalan.",
            translationEn = "O Allah, I ask You for beneficial knowledge, good provision, and accepted deeds.", translationFr = "Ô Allah, je Te demande une science utile, une subsistance bonne et des œuvres agréées.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "سنن ابن ماجه"
        ),
        ZikrItem(
            id = "e_24", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ شَرِّ مَا عَمِلْتُ وَمِنْ شَرِّ مَا لَمْ أَعْمَلْ", transliteration = "Allahumma inni a'udhu bika min sharri ma 'amiltu wa min sharri ma lam a'mal.",
            translationEn = "O Allah, I seek refuge in You from the evil of what I have done and what I have not done.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre le mal de ce que j’ai fait et de ce que je n’ai pas fait.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح مسلم"
        ),
        ZikrItem(
            id = "e_25", category = ZikrCategory.EVENING,
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ زَوَالِ نِعْمَتِكَ وَتَحَوُّلِ عَافِيَتِكَ وَفُجَاءَةِ نِقْمَتِكَ وَجَمِيعِ سَخَطِكَ", transliteration = "Allahumma inni a'udhu bika min zawali ni'matik...",
            translationEn = "O Allah, I seek refuge in You from the loss of Your blessing, the change of well-being You grant, sudden punishment, and all Your displeasure.", translationFr = "Ô Allah, je cherche refuge auprès de Toi contre la disparition de Tes bienfaits, le changement du bien-être accordé, un châtiment soudain et toute Ta colère.", targetCount = 1,
            virtueAr = "من الأذكار المأثورة", virtueEn = "A transmitted remembrance and supplication", virtueFr = "Invocation transmise dans les sources islamiques", reference = "صحيح مسلم"
        ),

        // After Prayer
        ZikrItem(
            id = "ap_1",
            category = ZikrCategory.AFTER_PRAYER,
            arabicText = "أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ. اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالإِكْرَامِ.",
            transliteration = "Astaghfirullah, Astaghfirullah, Astaghfirullah. Allahumma Antas-Salamu wa minkas-salam, tabarakta ya Dhal-Jalali wal-Ikram.",
            translationEn = "I ask Allah for forgiveness (3 times). O Allah, You are Peace and from You comes peace. Blessed are You, O Owner of majesty and honor.",
            translationFr = "Je demande pardon à Allah (3 fois). Ô Allah, Tu es la Paix et la paix vient de Toi. Béni sois-Tu, Ô Possesseur de majesté et de noblesse.",
            targetCount = 1,
            virtueAr = "سنة رسول الله ﷺ عقب كل صلاة مكتوبة",
            virtueEn = "Sunnah of the Prophet ﷺ immediately upon concluding obligatory prayer",
            virtueFr = "Tradition du Prophète ﷺ après chaque prière obligatoire",
            reference = "صحيح مسلم"
        ),
        ZikrItem(
            id = "ap_2",
            category = ZikrCategory.AFTER_PRAYER,
            arabicText = "سُبْحَانَ اللَّهِ (33)، الحَمْدُ لِلَّهِ (33)، اللَّهُ أَكْبَرُ (33)، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ (1).",
            transliteration = "Subhanallah (33), Alhamdulillah (33), Allahu Akbar (33), La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadir (1).",
            translationEn = "Glory be to Allah (33), Praise be to Allah (33), Allah is the Greatest (33), None has the right to be worshipped except Allah alone (1).",
            translationFr = "Gloire à Allah (33), Louange à Allah (33), Allah est le Plus Grand (33), Il n'y a de divinité qu'Allah (1).",
            targetCount = 33,
            virtueAr = "غُفرت خطاياه وإن كانت مثل زبد البحر",
            virtueEn = "Sins will be forgiven even if they are like the foam of the sea",
            virtueFr = "Ses péchés seront effacés même s'ils étaient aussi abondants que l'écume de la mer",
            reference = "صحيح مسلم"
        ),

        // Sleep
        ZikrItem(
            id = "s_1",
            category = ZikrCategory.SLEEP,
            arabicText = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي وَبِكَ أَرْفَعُهُ، إِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
            transliteration = "Bismika Rabbi wada'tu janbi wa bika arfa'uh, in amsakta nafsi farhamha, wa in arsaltaha fahfazha bima tahfazu bihi 'ibadakas-salihin.",
            translationEn = "In Your name my Lord, I lie down and in Your name I rise. If You should take my soul, have mercy upon it, and if You should return my soul, protect it as You protect Your righteous servants.",
            translationFr = "En Ton nom mon Seigneur, j'ai posé mon flanc et par Toi je me redresse. Si Tu retiens mon âme, fais-lui miséricorde, et si Tu la renvoies, protège-la comme Tu protèges Tes serviteurs vertueux.",
            targetCount = 1,
            virtueAr = "حفظ النفس وصيانتها أثناء النوم",
            virtueEn = "Safeguarding and protection of the soul during sleep",
            virtueFr = "Protection de l'âme durant le sommeil",
            reference = "صحيح البخاري ومسلم"
        )
    )

    val DAILY_DUAS = listOf(
        DailyDua(
            titleAr = "دعاء تيسير الأمور وزوال الهم",
            titleEn = "Dua for Ease & Removing Worries",
            titleFr = "Invocation pour Faciliter les Affaires",
            arabicText = "اللَّهُمَّ لَا سَهْلَ إِلَّا مَا جَعَلْتَهُ سَهْلًا، وَأَنْتَ تَجْعَلُ الْحَزْنَ إِذَا شِئْتَ سَهْلًا.",
            transliteration = "Allahumma la sahla illa ma ja'altahu sahla, wa Anta taj'alul-hazna idha shi'ta sahla.",
            translationEn = "O Allah, there is no ease except in that which You have made easy, and You make the difficult easy if You will.",
            translationFr = "Ô Allah, rien n'est facile si ce n'est ce que Tu as rendu facile, et Tu rends la tristesse ou la difficulté facile si Tu le veux.",
            source = "صحيح ابن حبان"
        ),
        DailyDua(
            titleAr = "دعاء جامع لخير الدنيا والآخرة",
            titleEn = "Comprehensive Dua for Both Worlds",
            titleFr = "Invocation Globale pour ce Monde et l'Au-delà",
            arabicText = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ.",
            transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
            translationEn = "Our Lord, give us in this world [that which is] good and in the Hereafter [that which is] good and protect us from the punishment of the Fire.",
            translationFr = "Seigneur ! Accorde-nous belle part ici-bas, et belle part aussi dans l'au-delà ; et protège-nous du châtiment du Feu !",
            source = "سورة البقرة - 201"
        ),
        DailyDua(
            titleAr = "دعاء الثبات على الدين",
            titleEn = "Dua for Steadfastness",
            titleFr = "Invocation pour la Constance dans la Foi",
            arabicText = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَىٰ دِينِكَ.",
            transliteration = "Ya Muqallibal-qulubi thabbit qalbi 'ala dinik.",
            translationEn = "O Turner of the hearts, keep my heart firm upon Your religion.",
            translationFr = "Ô Toi qui retournes les cœurs, affermis mon cœur sur Ta religion.",
            source = "جامع الترمذي"
        )
    )

    val DAILY_REMINDERS = listOf(
        IslamicReminder(
            titleAr = "فضل الكلمة الطيبة والصدقة",
            titleEn = "Virtue of Good Words & Charity",
            titleFr = "La Bonne Parole et l'Aumône",
            arabicText = "«اتَّقُوا النَّارَ وَلَوْ بِشِقِّ تَمْرَةٍ، فَمَنْ لَمْ يَجِدْ فَبِكَلِمَةٍ طَيِّبَةٍ»",
            translationEn = "\"Protect yourselves from the Fire, even with half a date (in charity); and if one cannot find that, then with a good, kind word.\"",
            translationFr = "« Préservez-vous du Feu, ne serait-ce que par la moitié d'une datte [en aumône] ; et si vous n'en trouvez pas, alors par une bonne parole. »",
            narrator = "متفق عليه (البخاري ومسلم) عن عدي بن حاتم رضي الله عنه"
        ),
        IslamicReminder(
            titleAr = "أحب الأعمال إلى الله أدومها",
            titleEn = "Beloved Deeds are the Most Consistent",
            titleFr = "Les Œuvres les Plus Aimées sont les Plus Régulières",
            arabicText = "«أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ»",
            translationEn = "\"The deeds most loved by Allah are those done regularly, even if they are small.\"",
            translationFr = "« L'acte le plus aimé d'Allah est celui qui est accompli avec la plus grande régularité, même s'il est modeste. »",
            narrator = "صحيح البخاري عن عائشة رضي الله عنها"
        ),
        IslamicReminder(
            titleAr = "الرحمة بالخلق تجلب رحمة الخالق",
            titleEn = "Mercy to Creation Brings Divine Mercy",
            titleFr = "La Miséricorde Envers la Création",
            arabicText = "«الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمَنُ، ارْحَمُوا مَنْ فِي الأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ»",
            translationEn = "\"The merciful will be shown mercy by the Most Merciful. Be merciful to those on the earth, and the One in the heavens will have mercy upon you.\"",
            translationFr = "« Les miséricordieux recevront la miséricorde du Tout Miséricordieux. Faites preuve de miséricorde envers les êtres de la terre, et Celui qui est au ciel vous fera miséricorde. »",
            narrator = "سنن الترمذي عن عبد الله بن عمرو رضي الله عنهما"
        )
    )

    fun getDailyDua(): DailyDua {
        val day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return DAILY_DUAS[day % DAILY_DUAS.size]
    }

    fun getDailyReminder(): IslamicReminder {
        val day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return DAILY_REMINDERS[day % DAILY_REMINDERS.size]
    }
}
