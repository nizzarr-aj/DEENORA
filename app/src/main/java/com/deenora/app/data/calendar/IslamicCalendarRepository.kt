package com.deenora.app.data.calendar

import com.deenora.app.ui.i18n.AppLanguage

data class IslamicEvent(
    val hijriDay: Int,
    val hijriMonth: Int, // 1 to 12
    val titleAr: String,
    val titleEn: String,
    val titleFr: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val descriptionFr: String
) {
    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> titleAr
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.FRENCH -> titleFr
    }

    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> descriptionAr
        AppLanguage.ENGLISH -> descriptionEn
        AppLanguage.FRENCH -> descriptionFr
    }
}

object IslamicCalendarRepository {

    val EVENTS = listOf(
        IslamicEvent(
            1, 1,
            "رأس السنة الهجرية", "Islamic New Year", "Nouvel An Hégirien",
            "فاتحة العام الهجري الجديد وذكرى هجرة المصطفى ﷺ إلى المدينة المنورة.",
            "First day of Muharram, commemorating the migration (Hijrah) of Prophet Muhammad ﷺ to Madinah.",
            "Premier jour de Mouharram, commémorant l'Hégire du Prophète ﷺ vers Médine."
        ),
        IslamicEvent(
            10, 1,
            "يوم عاشوراء", "Day of Ashura", "Jour d'Achoura",
            "يوم نجّى الله فيه موسى وقومه من فرعون، وصيامه يكفر ذنوب سنة ماضية.",
            "10th of Muharram, when Allah saved Moses and his people from Pharaoh. Fasting expiates sins of previous year.",
            "Le jour où Allah sauva Moïse et son peuple de Pharaon. Le jeûne expie les péchés de l'année écoulée."
        ),
        IslamicEvent(
            12, 3,
            "المولد النبوي الشريف", "Prophet's Birthday (Mawlid)", "Mawlid an-Nabi",
            "ذكرى ولادة سيد الخلق والمرسلين محمد صلى الله عليه وسلم رحمة للعالمين.",
            "Commemoration of the birth of the final Prophet Muhammad ﷺ, sent as a mercy to all creation.",
            "Commémoration de la naissance du Prophète Muhammad ﷺ, envoyé comme miséricorde pour les mondes."
        ),
        IslamicEvent(
            27, 7,
            "ليلة الإسراء والمعراج", "Isra and Mi'raj", "Al-Isra wal-Mi'raj",
            "معجزة رحلة النبي ﷺ من المسجد الحرام إلى المسجد الأقصى وعروجه إلى السماوات العلى.",
            "Miraculous night journey from Mecca to Jerusalem and ascension through the heavens.",
            "Voyage nocturne miraculeux de La Mecque à Jérusalem puis ascension vers les cieux élevés."
        ),
        IslamicEvent(
            1, 9,
            "غرة شهر رمضان المبارك", "First of Ramadan", "Premier jour de Ramadan",
            "بداية شهر الصيام والقرآن والرحمة والمغفرة والعتق من النيران.",
            "The beginning of the blessed holy month of fasting, intense worship, and Quranic revelation.",
            "Début du mois béni de jeûne, de recueillement et de miséricorde divine."
        ),
        IslamicEvent(
            27, 9,
            "ليلة القدر المباركة", "Laylat al-Qadr (Night of Decree)", "Laylat al-Qadr (Nuit du Destin)",
            "ليلة خير من ألف شهر، تنزل فيها الملائكة والروح بأمر ربهم.",
            "The night of supreme virtue, better than a thousand months, marked by descending angels.",
            "La nuit plus précieuse que mille mois, où descendent les anges avec les bénédictions."
        ),
        IslamicEvent(
            1, 10,
            "عيد الفطر المبارك", "Eid al-Fitr", "Aïd al-Fitr",
            "جائزة الصائمين وعيد الفرح والسرور وإفشاء السلام وصلة الأرحام.",
            "Celebration of the breaking of the fast, mutual love, charity and rejoicing.",
            "Fête célébrant la rupture du jeûne, le partage, la joie et la solidarité fraternelle."
        ),
        IslamicEvent(
            9, 12,
            "يوم عرفة العظيم", "Day of Arafah", "Jour d'Arafat",
            "أعظم أركان الحج، وصيامه لغير الحاج يكفر سنتين: ماضية ومستقبلة.",
            "Peak of Hajj on Mount Arafah; fasting it for non-pilgrims expiates sins of two years.",
            "Le pilier suprême du pèlerinage ; son jeûne pour les non-pèlerins expie deux années de péchés."
        ),
        IslamicEvent(
            10, 12,
            "عيد الأضحى المبارك", "Eid al-Adha", "Aïd al-Adha",
            "عيد النحر والتضحية اقتداءً بسيدنا إبراهيم الخليل عليه السلام.",
            "Feast of sacrifice commemorating Prophet Ibrahim's deep devotion to Allah.",
            "La grande fête du sacrifice en commémoration de la dévotion du Prophète Ibrahim."
        )
    )
}
