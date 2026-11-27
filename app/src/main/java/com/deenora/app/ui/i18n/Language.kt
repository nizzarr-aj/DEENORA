package com.deenora.app.ui.i18n

enum class AppLanguage(val code: String, val titleAr: String, val titleEn: String, val titleFr: String, val nativeName: String) {
    ARABIC("ar", "العربية", "Arabic", "Arabe", "العربية"),
    ENGLISH("en", "الإنجليزية", "English", "Anglais", "English"),
    FRENCH("fr", "الفرنسية", "French", "Français", "Français");

    val isRtl: Boolean get() = this == ARABIC
}
