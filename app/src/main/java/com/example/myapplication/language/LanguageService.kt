package com.example.myapplication.language

/**
 * Created by FPL on 16/06/2025.
 */

//mock data for demo purposes
object LanguageService {
    fun getLanguage(): Map<String, StringApp> {
        val vi = StringApp(
            profileName = "Trang cá nhân ${System.currentTimeMillis()}",
            profileBio = "Tiểu sử cá nhân ${System.currentTimeMillis()}",
            changeLang = "Thay đổi ngôn ngữ ${System.currentTimeMillis()}",
        )
        val en = StringApp(
            profileName = "Profile Name ${System.currentTimeMillis()}",
            profileBio = "Profile Bio ${System.currentTimeMillis()}",
            changeLang = "Change Language ${System.currentTimeMillis()} ",
        )
        return mapOf<String, StringApp> (
            "vi" to vi,
            "en" to en
        )
    }
}
