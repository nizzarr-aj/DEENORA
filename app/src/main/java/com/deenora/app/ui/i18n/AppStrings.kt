package com.deenora.app.ui.i18n

object AppStrings {
    fun appName(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "دينورا"
        AppLanguage.ENGLISH, AppLanguage.FRENCH -> "DEENORA"
    }

    // Navigation
    fun navHome(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الرئيسية"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.FRENCH -> "Accueil"
    }
    fun navQuran(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "القرآن"
        AppLanguage.ENGLISH -> "Quran"
        AppLanguage.FRENCH -> "Coran"
    }
    fun navAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الأذكار"
        AppLanguage.ENGLISH -> "Azkar"
        AppLanguage.FRENCH -> "Invocations"
    }
    fun navDiscover(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "استكشف"
        AppLanguage.ENGLISH -> "Discover"
        AppLanguage.FRENCH -> "Découvrir"
    }
    fun navProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "حسابي"
        AppLanguage.ENGLISH -> "Profile"
        AppLanguage.FRENCH -> "Profil"
    }

    // Prayers
    fun prayerFajr(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الفجر"
        AppLanguage.ENGLISH -> "Fajr"
        AppLanguage.FRENCH -> "Fajr"
    }
    fun prayerSunrise(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الشروق"
        AppLanguage.ENGLISH -> "Sunrise"
        AppLanguage.FRENCH -> "Chourouk"
    }
    fun prayerDhuhr(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الظهر"
        AppLanguage.ENGLISH -> "Dhuhr"
        AppLanguage.FRENCH -> "Dhuhr"
    }
    fun prayerAsr(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "العصر"
        AppLanguage.ENGLISH -> "Asr"
        AppLanguage.FRENCH -> "Asr"
    }
    fun prayerMaghrib(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المغرب"
        AppLanguage.ENGLISH -> "Maghrib"
        AppLanguage.FRENCH -> "Maghrib"
    }
    fun prayerIsha(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "العشاء"
        AppLanguage.ENGLISH -> "Isha"
        AppLanguage.FRENCH -> "Icha"
    }

    // Home
    fun greeting(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "السلام عليكم ورحمة الله"
        AppLanguage.ENGLISH -> "Assalamu Alaikum"
        AppLanguage.FRENCH -> "Assalamu Alaykoum"
    }
    fun nextPrayer(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الصلاة القادمة"
        AppLanguage.ENGLISH -> "Next Prayer"
        AppLanguage.FRENCH -> "Prochaine Prière"
    }
    fun timeRemaining(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الوقت المتبقي"
        AppLanguage.ENGLISH -> "Time Remaining"
        AppLanguage.FRENCH -> "Temps Restant"
    }
    fun todayPrayers(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مواقيت صلاة اليوم"
        AppLanguage.ENGLISH -> "Today's Prayer Times"
        AppLanguage.FRENCH -> "Horaires des Prières"
    }
    fun quickAccess(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الوصول السريع"
        AppLanguage.ENGLISH -> "Quick Access"
        AppLanguage.FRENCH -> "Accès Rapide"
    }
    fun qibla(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "القبلة"
        AppLanguage.ENGLISH -> "Qibla"
        AppLanguage.FRENCH -> "Qibla"
    }
    fun tasbih(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "السبحة"
        AppLanguage.ENGLISH -> "Tasbih"
        AppLanguage.FRENCH -> "Chapelet"
    }
    fun dailyDua(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "دعاء اليوم"
        AppLanguage.ENGLISH -> "Daily Dua"
        AppLanguage.FRENCH -> "Invocation du Jour"
    }
    fun dailyVerse(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "آية اليوم"
        AppLanguage.ENGLISH -> "Daily Verse"
        AppLanguage.FRENCH -> "Verset du Jour"
    }
    fun islamicReminder(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إشراقة إيمانية"
        AppLanguage.ENGLISH -> "Daily Reminder"
        AppLanguage.FRENCH -> "Rappel Quotidien"
    }
    fun copy(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "نسخ"
        AppLanguage.ENGLISH -> "Copy"
        AppLanguage.FRENCH -> "Copier"
    }
    fun share(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مشاركة"
        AppLanguage.ENGLISH -> "Share"
        AppLanguage.FRENCH -> "Partager"
    }
    fun copiedToast(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تم النسخ بنجاح"
        AppLanguage.ENGLISH -> "Copied to clipboard"
        AppLanguage.FRENCH -> "Copié dans le presse-papiers"
    }

    // Quran
    fun searchSurah(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "ابحث عن سورة..."
        AppLanguage.ENGLISH -> "Search Surah..."
        AppLanguage.FRENCH -> "Rechercher une sourate..."
    }
    fun surahsTab(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "السور"
        AppLanguage.ENGLISH -> "Surahs"
        AppLanguage.FRENCH -> "Sourates"
    }
    fun juzTab(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الأجزاء"
        AppLanguage.ENGLISH -> "Juz"
        AppLanguage.FRENCH -> "Djouz"
    }
    fun bookmarksTab(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المحفوظات"
        AppLanguage.ENGLISH -> "Bookmarks"
        AppLanguage.FRENCH -> "Favoris"
    }
    fun versesCount(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.ARABIC -> "$count آيات"
        AppLanguage.ENGLISH -> "$count Verses"
        AppLanguage.FRENCH -> "$count Versets"
    }
    fun makki(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مكية"
        AppLanguage.ENGLISH -> "Meccan"
        AppLanguage.FRENCH -> "Mecquoise"
    }
    fun madani(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "مدنية"
        AppLanguage.ENGLISH -> "Medinan"
        AppLanguage.FRENCH -> "Médinoise"
    }
    fun bismillah(lang: AppLanguage) = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
    fun fontSize(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "حجم الخط"
        AppLanguage.ENGLISH -> "Font Size"
        AppLanguage.FRENCH -> "Taille du texte"
    }

    // Azkar
    fun morningAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذكار الصباح"
        AppLanguage.ENGLISH -> "Morning Azkar"
        AppLanguage.FRENCH -> "Invocations du Matin"
    }
    fun eveningAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذكار المساء"
        AppLanguage.ENGLISH -> "Evening Azkar"
        AppLanguage.FRENCH -> "Invocations du Soir"
    }
    fun afterPrayerAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذكار بعد الصلاة"
        AppLanguage.ENGLISH -> "After Prayer"
        AppLanguage.FRENCH -> "Après la Prière"
    }
    fun sleepAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذكار النوم"
        AppLanguage.ENGLISH -> "Sleep Azkar"
        AppLanguage.FRENCH -> "Invocations du Sommeil"
    }
    fun wakeUpAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أذكار الاستيقاظ"
        AppLanguage.ENGLISH -> "Waking Up"
        AppLanguage.FRENCH -> "Au Réveil"
    }
    fun travelAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أدعية السفر"
        AppLanguage.ENGLISH -> "Travel Duas"
        AppLanguage.FRENCH -> "Voyage"
    }
    fun protectionAzkar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أدعية التحصين"
        AppLanguage.ENGLISH -> "Protection & Ruqyah"
        AppLanguage.FRENCH -> "Protection"
    }
    fun reset(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "إعادة"
        AppLanguage.ENGLISH -> "Reset"
        AppLanguage.FRENCH -> "Réinitialiser"
    }
    fun completed(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تم الإكمال ✓"
        AppLanguage.ENGLISH -> "Completed ✓"
        AppLanguage.FRENCH -> "Terminé ✓"
    }

    // Discover
    fun namesOfAllah(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "أسماء الله الحسنى"
        AppLanguage.ENGLISH -> "99 Names of Allah"
        AppLanguage.FRENCH -> "99 Noms d'Allah"
    }
    fun islamicCalendar(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "التقويم الهجري والمناسبات"
        AppLanguage.ENGLISH -> "Hijri Calendar & Events"
        AppLanguage.FRENCH -> "Calendrier Hégirien"
    }
    fun qiblaCompass(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "بوصلة القبلة"
        AppLanguage.ENGLISH -> "Qibla Compass"
        AppLanguage.FRENCH -> "Boussole de la Qibla"
    }
    fun digitalTasbih(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "السبحة الإلكترونية"
        AppLanguage.ENGLISH -> "Digital Tasbih"
        AppLanguage.FRENCH -> "Chapelet Électronique"
    }
    fun alignQiblaTip(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "وجّه هاتفك نحو الكعبة المشرفة حتى يضيء المؤشر بالأخضر"
        AppLanguage.ENGLISH -> "Point your device towards the Kaaba until the needle turns green"
        AppLanguage.FRENCH -> "Dirigez votre appareil vers la Kaaba jusqu'à ce que l'aiguille devienne verte"
    }
    fun distanceToKaaba(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المسافة إلى مكة المكرمة"
        AppLanguage.ENGLISH -> "Distance to Makkah"
        AppLanguage.FRENCH -> "Distance à La Mecque"
    }
    fun degrees(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "درجة"
        AppLanguage.ENGLISH -> "degrees"
        AppLanguage.FRENCH -> "degrés"
    }

    // Settings
    fun settings(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الإعدادات"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.FRENCH -> "Paramètres"
    }
    fun language(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "اللغة"
        AppLanguage.ENGLISH -> "Language"
        AppLanguage.FRENCH -> "Langue"
    }
    fun calculationMethod(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "طريقة حساب المواقيت"
        AppLanguage.ENGLISH -> "Calculation Method"
        AppLanguage.FRENCH -> "Méthode de Calcul"
    }
    fun asrMethod(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "طريقة حساب العصر"
        AppLanguage.ENGLISH -> "Asr Calculation"
        AppLanguage.FRENCH -> "Calcul du Asr"
    }
    fun standardShafii(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الجمهور (شافعي، مالكي، حنبلي)"
        AppLanguage.ENGLISH -> "Standard (Shafi'i, Maliki, Hanbali)"
        AppLanguage.FRENCH -> "Standard (Chafiite, Malikite, Hanbalite)"
    }
    fun hanafi(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "حنفي"
        AppLanguage.ENGLISH -> "Hanafi"
        AppLanguage.FRENCH -> "Hanafite"
    }
    fun locationCity(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "المدينة والموقع"
        AppLanguage.ENGLISH -> "Location & City"
        AppLanguage.FRENCH -> "Ville & Emplacement"
    }
    fun detectLocation(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تحديد الموقع التلقائي (GPS)"
        AppLanguage.ENGLISH -> "Auto-detect location (GPS)"
        AppLanguage.FRENCH -> "Détecter l'emplacement (GPS)"
    }
    fun hijriAdjustment(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تعديل التاريخ الهجري"
        AppLanguage.ENGLISH -> "Hijri Date Adjustment"
        AppLanguage.FRENCH -> "Ajustement Date Hégirienne"
    }
    fun days(lang: AppLanguage, count: Int): String {
        return if (count == 0) "0" else (if (count > 0) "+$count" else "$count")
    }
    fun darkMode(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الوضع الليلي"
        AppLanguage.ENGLISH -> "Dark Mode"
        AppLanguage.FRENCH -> "Mode Sombre"
    }
    fun notifications(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "تنبيهات الأذان"
        AppLanguage.ENGLISH -> "Prayer Notifications"
        AppLanguage.FRENCH -> "Notifications de Prière"
    }
    fun vibrationFeedback(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "الاهتزاز اللمسي"
        AppLanguage.ENGLISH -> "Haptic Feedback"
        AppLanguage.FRENCH -> "Retour Haptique"
    }
    fun aboutApp(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> "عن تطبيق دينورا"
        AppLanguage.ENGLISH -> "About DEENORA"
        AppLanguage.FRENCH -> "À propos de DEENORA"
    }
    fun appVersion(lang: AppLanguage) = "v1.0.0"
}
