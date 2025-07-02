package com.example.myapplication.language

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//code generated from compile time later
@JsonClass(generateAdapter = true)
data class StringApp(
    @Json(name = "profile_name") val profileName: String = "profile_name_default",
    @Json(name = "profile_bio") val profileBio: String = "profile_bio_default",
    @Json(name = "change_lang") val changeLang: String = "change_lang_default",
    @Json(name = "posts") val posts: String = "posts_default",
    @Json(name = "followers") val followers: String = "followers_default",
    @Json(name = "following") val following: String = "following_default",
    @Json(name = "edit_profile") val editProfile: String = "edit_profile_default",
    @Json(name = "male") val male: String = "male_default",
    @Json(name = "joined") val joined: String = "joined_default",
    @Json(name = "email") val email: String = "email_default",
    @Json(name = "phone_number") val phoneNumber: String = "phone_number_default",
    @Json(name = "birth_date") val birthDate: String = "birth_date_default"
)