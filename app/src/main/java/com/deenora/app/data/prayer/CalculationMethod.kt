package com.deenora.app.data.prayer

import com.batoulapps.adhan.CalculationMethod as AdhanMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Madhab

enum class CalculationMethod(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val titleFr: String
) {
    UMM_AL_QURA(
        id = "umm_al_qura",
        titleAr = "أم القرى - مكة المكرمة",
        titleEn = "Umm Al-Qura (Makkah)",
        titleFr = "Oumm Al-Qura (La Mecque)"
    ),
    MWL(
        id = "mwl",
        titleAr = "رابطة العالم الإسلامي (MWL)",
        titleEn = "Muslim World League (MWL)",
        titleFr = "Ligue Islamique Mondiale (MWL)"
    ),
    EGYPT(
        id = "egypt",
        titleAr = "الهيئة المصرية العامة للمساحة",
        titleEn = "Egyptian General Authority",
        titleFr = "Autorité Générale Égyptienne"
    ),
    ISNA(
        id = "isna",
        titleAr = "الجمعية الإسلامية لأمريكا الشمالية (ISNA)",
        titleEn = "Islamic Society of North America (ISNA)",
        titleFr = "Société Islamique d'Amérique du Nord"
    ),
    KARACHI(
        id = "karachi",
        titleAr = "جامعة العلوم الإسلامية بكراتشي",
        titleEn = "Univ. of Islamic Sciences, Karachi",
        titleFr = "Université des Sciences Islamiques, Karachi"
    ),
    DUBAI(
        id = "dubai",
        titleAr = "دبي والإمارات العربية المتحدة",
        titleEn = "Dubai & UAE",
        titleFr = "Dubaï & Émirats"
    ),
    TURKEY(
        id = "turkey",
        titleAr = "رئاسة الشؤون الدينية بتركيا (ديانت)",
        titleEn = "Turkey (Diyanet)",
        titleFr = "Turquie (Diyanet)"
    ),
    FRANCE(
        id = "france",
        titleAr = "اتحاد المنظمات الإسلامية بفرنسا (UOIF 12°)",
        titleEn = "France (UOIF 12°)",
        titleFr = "France (UOIF 12°)"
    ),
    KUWAIT(
        id = "kuwait",
        titleAr = "وزارة الأوقاف والشؤون الإسلامية بالكويت",
        titleEn = "Kuwait",
        titleFr = "Koweït"
    ),
    QATAR(
        id = "qatar",
        titleAr = "وزارة الأوقاف والشؤون الإسلامية بقطر",
        titleEn = "Qatar",
        titleFr = "Qatar"
    ),
    MOON_SIGHTING(
        id = "moon_sighting",
        titleAr = "لجنة رؤية الهلال العالمية",
        titleEn = "Moonsighting Committee",
        titleFr = "Comité d'observation lunaire"
    ),
    SINGAPORE(
        id = "singapore",
        titleAr = "مجلس أوغاما إسلام سينغابورا (MUIS)",
        titleEn = "Singapore (MUIS)",
        titleFr = "Singapour (MUIS)"
    );

    fun toAdhanParameters(madhhab: AsrJuristicMethod): CalculationParameters {
        val params = when (this) {
            UMM_AL_QURA -> AdhanMethod.UMM_AL_QURA.parameters
            MWL -> AdhanMethod.MUSLIM_WORLD_LEAGUE.parameters
            EGYPT -> AdhanMethod.EGYPTIAN.parameters
            ISNA -> AdhanMethod.NORTH_AMERICA.parameters
            KARACHI -> AdhanMethod.KARACHI.parameters
            DUBAI -> AdhanMethod.DUBAI.parameters
            TURKEY -> CalculationParameters(18.0, 17.0)
            KUWAIT -> AdhanMethod.KUWAIT.parameters
            QATAR -> AdhanMethod.QATAR.parameters
            MOON_SIGHTING -> AdhanMethod.MOON_SIGHTING_COMMITTEE.parameters
            SINGAPORE -> AdhanMethod.SINGAPORE.parameters
            FRANCE -> CalculationParameters(12.0, 12.0)
        }
        params.madhab = if (madhhab == AsrJuristicMethod.HANAFI) Madhab.HANAFI else Madhab.SHAFI
        return params
    }
}

enum class AsrJuristicMethod(
    val factor: Int,
    val titleEn: String,
    val titleAr: String,
    val titleFr: String
) {
    STANDARD(
        1,
        "Standard (Shafi'i, Maliki, Hanbali)",
        "الجمهور (شافعي، مالكي، حنبلي)",
        "Standard (Chafiite, Malikite, Hanbalite)"
    ),
    HANAFI(
        2,
        "Hanafi",
        "حنفي",
        "Hanafite"
    )
}

data class CityLocation(
    val nameEn: String,
    val nameAr: String,
    val nameFr: String,
    val countryEn: String,
    val countryAr: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double,
    val isGps: Boolean = false
)

object PredefinedCities {
    val CITIES = listOf(
        // Holy Sanctuaries
        CityLocation("Makkah", "مكة المكرمة", "La Mecque", "Saudi Arabia", "المملكة العربية السعودية", 21.4225, 39.8262, 3.0),
        CityLocation("Madinah", "المدينة المنورة", "Médine", "Saudi Arabia", "المملكة العربية السعودية", 24.5247, 39.5692, 3.0),
        CityLocation("Jerusalem", "القدس الشريف", "Jérusalem", "Palestine", "فلسطين", 31.7683, 35.2137, 3.0),

        // Middle East
        CityLocation("Riyadh", "الرياض", "Riyad", "Saudi Arabia", "المملكة العربية السعودية", 24.7136, 46.6753, 3.0),
        CityLocation("Cairo", "القاهرة", "Le Caire", "Egypt", "مصر", 30.0444, 31.2357, 2.0),
        CityLocation("Dubai", "دبي", "Dubaï", "UAE", "الإمارات العربية المتحدة", 25.2048, 55.2708, 4.0),
        CityLocation("Abu Dhabi", "أبو ظبي", "Abou Dabi", "UAE", "الإمارات العربية المتحدة", 24.4539, 54.3773, 4.0),
        CityLocation("Doha", "الدوحة", "Doha", "Qatar", "قطر", 25.2854, 51.5310, 3.0),
        CityLocation("Kuwait City", "مدينة الكويت", "Koweït", "Kuwait", "الكويت", 29.3759, 47.9774, 3.0),
        CityLocation("Muscat", "مسقط", "Mascate", "Oman", "عُمان", 23.5880, 58.3829, 4.0),
        CityLocation("Amman", "عَمّان", "Amman", "Jordan", "الأردن", 31.9454, 35.9284, 3.0),
        CityLocation("Beirut", "بيروت", "Beyrouth", "Lebanon", "لبنان", 33.8938, 35.5018, 3.0),
        CityLocation("Baghdad", "بغداد", "Bagdad", "Iraq", "العراق", 33.3152, 44.3661, 3.0),
        CityLocation("Manama", "المنامة", "Manama", "Bahrain", "البحرين", 26.2285, 50.5860, 3.0),

        // North Africa
        CityLocation("Casablanca", "الدار البيضاء", "Casablanca", "Morocco", "المغرب", 33.5731, -7.5898, 1.0),
        CityLocation("Rabat", "الرباط", "Rabat", "Morocco", "المغرب", 34.0209, -6.8416, 1.0),
        CityLocation("Marrakech", "مراكش", "Marrakech", "Morocco", "المغرب", 31.6295, -7.9811, 1.0),
        CityLocation("Algiers", "الجزائر العاصمة", "Alger", "Algeria", "الجزائر", 36.7538, 3.0588, 1.0),
        CityLocation("Oran", "وهران", "Oran", "Algeria", "الجزائر", 35.6987, -0.6349, 1.0),
        CityLocation("Tunis", "تونس", "Tunis", "Tunisia", "تونس", 36.8065, 10.1815, 1.0),
        CityLocation("Tripoli", "طرابلس", "Tripoli", "Libya", "ليبيا", 32.8872, 13.1913, 2.0),

        // Europe
        CityLocation("London", "لندن", "Londres", "United Kingdom", "المملكة المتحدة", 51.5074, -0.1278, 1.0),
        CityLocation("Paris", "باريس", "Paris", "France", "فرنسا", 48.8566, 2.3522, 2.0),
        CityLocation("Marseille", "مارسيليا", "Marseille", "France", "فرنسا", 43.2965, 5.3698, 2.0),
        CityLocation("Lyon", "ليون", "Lyon", "France", "فرنسا", 45.7640, 4.8357, 2.0),
        CityLocation("Istanbul", "إسطنبول", "Istanbul", "Turkey", "تركيا", 41.0082, 28.9784, 3.0),
        CityLocation("Ankara", "أنقرة", "Ankara", "Turkey", "تركيا", 39.9334, 32.8597, 3.0),
        CityLocation("Berlin", "برلين", "Berlin", "Germany", "ألمانيا", 52.5200, 13.4050, 2.0),
        CityLocation("Frankfurt", "فرانكفورت", "Francfort", "Germany", "ألمانيا", 50.1109, 8.6821, 2.0),
        CityLocation("Brussels", "بروكسل", "Bruxelles", "Belgium", "بلجيكا", 50.8503, 4.3517, 2.0),
        CityLocation("Amsterdam", "أمستردام", "Amsterdam", "Netherlands", "هولندا", 52.3676, 4.9041, 2.0),
        CityLocation("Sarajevo", "سراييفو", "Sarajevo", "Bosnia", "البوسنة والهرسك", 43.8563, 18.4131, 2.0),
        CityLocation("Madrid", "مدريد", "Madrid", "Spain", "إسبانيا", 40.4168, -3.7038, 2.0),
        CityLocation("Rome", "روما", "Rome", "Italy", "إيطاليا", 41.9028, 12.4964, 2.0),

        // North America
        CityLocation("New York", "نيويورك", "New York", "United States", "الولايات المتحدة", 40.7128, -74.0060, -4.0),
        CityLocation("Chicago", "شيكاغو", "Chicago", "United States", "الولايات المتحدة", 41.8781, -87.6298, -5.0),
        CityLocation("Los Angeles", "لوس أنجلوس", "Los Angeles", "United States", "الولايات المتحدة", 34.0522, -118.2437, -7.0),
        CityLocation("Houston", "هيوستن", "Houston", "United States", "الولايات المتحدة", 29.7604, -95.3698, -5.0),
        CityLocation("Toronto", "تورونتو", "Toronto", "Canada", "كندا", 43.6532, -79.3832, -4.0),
        CityLocation("Montreal", "مونتريال", "Montréal", "Canada", "كندا", 45.5017, -73.5673, -4.0),

        // Asia & Australia
        CityLocation("Jakarta", "جاكرتا", "Jakarta", "Indonesia", "إندونيسيا", -6.2088, 106.8456, 7.0),
        CityLocation("Kuala Lumpur", "كوالالمبور", "Kuala Lumpur", "Malaysia", "ماليزيا", 3.1390, 101.6869, 8.0),
        CityLocation("Singapore", "سنغافورة", "Singapour", "Singapore", "سنغافورة", 1.3521, 103.8198, 8.0),
        CityLocation("Karachi", "كراتشي", "Karachi", "Pakistan", "باكستان", 24.8607, 67.0011, 5.0),
        CityLocation("Lahore", "لاهور", "Lahore", "Pakistan", "باكستان", 31.5204, 74.3587, 5.0),
        CityLocation("Dhaka", "دكا", "Dacca", "Bangladesh", "بنغلاديش", 23.8103, 90.4125, 6.0),
        CityLocation("Sydney", "سيدني", "Sydney", "Australia", "أستراليا", -33.8688, 151.2093, 10.0)
    )

    val DEFAULT_CITY = CITIES[0] // Makkah
}
