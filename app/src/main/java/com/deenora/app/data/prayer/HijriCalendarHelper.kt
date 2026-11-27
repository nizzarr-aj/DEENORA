package com.deenora.app.data.prayer

import com.deenora.app.ui.i18n.AppLanguage
import java.util.Calendar
import kotlin.math.floor

data class HijriDate(
    val day: Int,
    val month: Int, // 1 to 12
    val year: Int,
    val monthNameAr: String,
    val monthNameEn: String,
    val monthNameFr: String
) {
    fun format(language: AppLanguage): String {
        val monthName = when (language) {
            AppLanguage.ARABIC -> monthNameAr
            AppLanguage.ENGLISH -> monthNameEn
            AppLanguage.FRENCH -> monthNameFr
        }
        return when (language) {
            AppLanguage.ARABIC -> "$day $monthName $year هـ"
            AppLanguage.ENGLISH -> "$day $monthName $year AH"
            AppLanguage.FRENCH -> "$day $monthName $year H"
        }
    }
}

object HijriCalendarHelper {

    private val MONTH_NAMES_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    private val MONTH_NAMES_EN = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    private val MONTH_NAMES_FR = listOf(
        "Mouharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Joumada al-Oula", "Joumada ath-Thania", "Rajab", "Cha'bane",
        "Ramadan", "Chawwal", "Dhou al-Qi'da", "Dhou al-Hijja"
    )

    fun getHijriDate(calendar: Calendar = Calendar.getInstance(), adjustmentDays: Int = 0): HijriDate {
        val cal = Calendar.getInstance().apply {
            timeInMillis = calendar.timeInMillis
            add(Calendar.DAY_OF_YEAR, adjustmentDays)
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // Convert to Julian Day Number
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        // Islamic calendar calculation (Civil/Astronomical estimate epoch 1948439.5)
        val z = jd - 1948439.5 + 0.5
        val cyc = floor(z / 10631.0)
        val j = z - 10631.0 * cyc
        val jYear = floor((j - 1.0) / 354.366)
        val hYear = (30 * cyc + jYear).toInt()
        val dayInYear = j - floor(jYear * 354.366)
        val hMonth = (floor((dayInYear - 1.0) / 29.5) + 1).toInt().coerceIn(1, 12)
        val hDay = (dayInYear - floor((hMonth - 1) * 29.5)).toInt().coerceIn(1, 30)

        val idx = hMonth - 1
        return HijriDate(
            day = hDay,
            month = hMonth,
            year = hYear,
            monthNameAr = MONTH_NAMES_AR.getOrElse(idx) { "" },
            monthNameEn = MONTH_NAMES_EN.getOrElse(idx) { "" },
            monthNameFr = MONTH_NAMES_FR.getOrElse(idx) { "" }
        )
    }
}
