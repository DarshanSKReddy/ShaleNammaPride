package com.example.shale_nammapride.view

fun localizedText(currentLanguage: String, english: String, kannada: String): String {
    return if (currentLanguage == "kn") kannada else english
}