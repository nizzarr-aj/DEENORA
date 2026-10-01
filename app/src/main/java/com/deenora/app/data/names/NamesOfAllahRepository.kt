package com.deenora.app.data.names

import com.deenora.app.ui.i18n.AppLanguage

data class NameOfAllah(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningFr: String,
    val explanationEn: String,
    val explanationFr: String
) {
    fun getMeaning(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> ""
        AppLanguage.ENGLISH -> meaningEn
        AppLanguage.FRENCH -> meaningFr
    }

    fun getExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> ""
        AppLanguage.ENGLISH -> explanationEn
        AppLanguage.FRENCH -> explanationFr
    }
}

object NamesOfAllahRepository {

    val NAMES: List<NameOfAllah> = listOf(
        NameOfAllah(1, "الرَّحْمَٰنُ", "Ar-Rahman", "The Entirely Merciful", "Le Tout Miséricordieux", "The One who has plenty of mercy for the believers and the blasphemers in this world.", "Celui dont la miséricorde embrasse toute chose en ce bas-monde."),
        NameOfAllah(2, "الرَّحِيمُ", "Ar-Rahim", "The Especially Merciful", "Le Très Miséricordieux", "The One who has plenty of mercy for the believers in the hereafter.", "Celui qui accorde une miséricorde exclusive aux croyants dans l'au-delà."),
        NameOfAllah(3, "الْمَلِكُ", "Al-Malik", "The Sovereign", "Le Souverain", "The King with absolute authority over all created existence.", "Le Maître absolu et Possesseur de l'univers."),
        NameOfAllah(4, "الْقُدُّوسُ", "Al-Quddus", "The Most Holy", "Le Très Saint", "The One who is pure from any imperfection and free from defect.", "Le Pur de tout défaut, exempt de toute imperfection."),
        NameOfAllah(5, "السَّلَامُ", "As-Salam", "The Source of Peace", "La Paix", "The Granter of security, safety and peace to His servants.", "La Source de sérénité et le Dispensateur de paix."),
        NameOfAllah(6, "الْمُؤْمِنُ", "Al-Mu'min", "The Inspirer of Faith", "Le Sécurisant", "The One who witnessed for Himself that no one is God but Him.", "Le Dispensateur de la foi et de la sécurité."),
        NameOfAllah(7, "الْمُهَيْمِنُ", "Al-Muhaymin", "The Overseer", "Le Témoin Supérieur", "The Witness and Watcher over the deeds and destiny of His creation.", "Celui qui observe et veille sur chaque créature."),
        NameOfAllah(8, "الْعَزِيزُ", "Al-'Aziz", "The All-Mighty", "Le Tout Puissant", "The Defeater who is not defeated and possesses unassailable dignity.", "L'Irrésistible, Celui que rien ne peut vaincre."),
        NameOfAllah(9, "الْجَبَّارُ", "Al-Jabbar", "The Restorer", "Le Réparateur", "The Compeller who mends the broken and remedies all conditions.", "Celui qui répare les cœurs brisés et dont la volonté s'impose."),
        NameOfAllah(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "The Supreme", "Le Majestueux", "The One who is supreme above all creation in majesty and sublimity.", "Le Grand en grandeur et majesté suprême."),
        NameOfAllah(11, "الْخَالِقُ", "Al-Khaliq", "The Creator", "Le Créateur", "The One who brings everything from non-existence to existence.", "Celui qui donne l'existence à partir du néant."),
        NameOfAllah(12, "الْبَارِئُ", "Al-Bari'", "The Originator", "Le Concepteur", "The Maker who fashions all creation with flawless proportions.", "Celui qui façonne les créatures avec perfection."),
        NameOfAllah(13, "الْمُصَوِّرُ", "Al-Musawwir", "The Fashioner", "Le Dessinateur", "The One who shapes every entity with distinct individuality.", "Celui qui donne à chaque être sa forme distincte."),
        NameOfAllah(14, "الْغَفَّارُ", "Al-Ghaffar", "The All-Forgiving", "Le Grand Pardonneur", "The One who forgives the sins of His servants again and again.", "Celui qui efface et pardonne inlassablement les péchés."),
        NameOfAllah(15, "الْقَهَّارُ", "Al-Qahhar", "The Subduer", "Le Dominateur", "The Subduer who has the dominance over everything in existence.", "Celui devant la grandeur duquel tout s'incline."),
        NameOfAllah(16, "الْوَهَّابُ", "Al-Wahhab", "The Bestower", "Le Donateur Gracieux", "The One who gives without expecting compensation or request.", "Celui qui comble de dons généreux sans attendre de retour."),
        NameOfAllah(17, "الرَّزَّاقُ", "Ar-Razzaq", "The Provider", "Le Dispensateur de Subsistance", "The Sustainer who grants livelihood to all living beings.", "Celui qui pourvoit aux besoins de toutes les créatures."),
        NameOfAllah(18, "الْفَتَّاحُ", "Al-Fattah", "The Opener", "Le Grand Ouvrant", "The One who opens closed doors of mercy, relief and knowledge.", "Celui qui ouvre les portes de la miséricorde et du secours."),
        NameOfAllah(19, "الْعَلِيمُ", "Al-'Alim", "The All-Knowing", "L'Omniscient", "The One who knows everything in fine detail across all time.", "Celui dont le savoir embrasse l'invisible et le visible."),
        NameOfAllah(20, "الْقَابِضُ", "Al-Qabid", "The Withholder", "Celui qui Retient", "The One who constricts sustenance or souls with divine wisdom.", "Celui qui restreint avec sagesse."),
        NameOfAllah(21, "الْبَاسِطُ", "Al-Basit", "The Expander", "Celui qui Élargit", "The One who expands sustenance and fills hearts with joy.", "Celui qui dispense largesses et sérénité."),
        NameOfAllah(22, "الْخَافِضُ", "Al-Khafid", "The Humbler", "Celui qui Abaisse", "The One who lowers the proud and arrogant tyrants.", "Celui qui humilie les orgueilleux."),
        NameOfAllah(23, "الرَّافِعُ", "Ar-Rafi'", "The Exalter", "Celui qui Élève", "The One who elevates righteous believers in rank and honor.", "Celui qui élève en honneur et en degrés Ses serviteurs pieux."),
        NameOfAllah(24, "الْمُعِزُّ", "Al-Mu'izz", "The Bestower of Honor", "Celui qui Glorifie", "The One who grants true dignity to whom He wills.", "Celui qui donne la puissance et la gloire."),
        NameOfAllah(25, "الْمُذِلُّ", "Al-Mudhill", "The Dishonorer", "Celui qui Avilit", "The One who debases tyrants and corrupt transgressors.", "Celui qui déchoit les injustes."),
        NameOfAllah(26, "السَّمِيعُ", "As-Sami'", "The All-Hearing", "L'Audient", "The One who hears all secret and public voices and cries for help.", "Celui qui perçoit toutes les paroles et invocations."),
        NameOfAllah(27, "الْبَصِيرُ", "Al-Basir", "The All-Seeing", "Le Clairvoyant", "The One who sees all actions, movements, and subtleties.", "Celui dont le regard embrasse toute chose."),
        NameOfAllah(28, "الْحَكَمُ", "Al-Hakam", "The Impartial Judge", "Le Juge Suprême", "The Supreme Arbiter whose judgment cannot be overturned.", "Le Magistrat suprême dont le verdict est sans appel."),
        NameOfAllah(29, "الْعَدْلُ", "Al-'Adl", "The Utterly Just", "Le Juste Absolu", "The One who is equitable and free of even an atom of unfairness.", "Celui qui est équitable et dont la justice est sans faille."),
        NameOfAllah(30, "اللَّطِيفُ", "Al-Latif", "The Subtle & Kind", "Le Bienveillant Subtil", "The One who is subtle, kind, and provides in unperceived ways.", "Celui qui agit avec une délicatesse et bienveillance incommensurables.")
    )
}
