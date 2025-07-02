package com.example.myapplication.language

/**
 * Created by FPL on 16/06/2025.
 */

//mock data for demo purposes
object LanguageService {
    fun getLanguage(): Map<String, AppLanguage> {
        val vi = AppLanguage(
            profileName = "Trang cá nhân ${System.currentTimeMillis()}",
            profileBio = "Tiểu sử cá nhân ${System.currentTimeMillis()}",
            changeLang = "Thay đổi ngôn ngữ ${System.currentTimeMillis()}",
        )
        val en = AppLanguage(
            profileName = "Profile Name ${System.currentTimeMillis()}",
            profileBio = "Profile Bio ${System.currentTimeMillis()}",
            changeLang = "Change Language ${System.currentTimeMillis()} ",
        )
        return mapOf<String, AppLanguage> (
            "vi" to vi,
            "en" to en
        )
    }
}
