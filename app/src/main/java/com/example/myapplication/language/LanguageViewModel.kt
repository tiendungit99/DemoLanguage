package com.example.myapplication.language

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.seconds

/**
 * Created by FPL on 16/06/2025.
 */

class LanguageViewModelFactory(private val context: Context) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LanguageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@SuppressLint("StaticFieldLeak")
annotation class ApplicationContext

class LanguageViewModel(@ApplicationContext val context: Context) : ViewModel() {
    val currentLanguage: MutableStateFlow<String> = MutableStateFlow("vi")
    var appLanguage: MutableStateFlow<AppLanguage> = MutableStateFlow(AppLanguage()) //default

    private val sharedPreferences = context.getSharedPreferences("lang", Context.MODE_PRIVATE)

    init {
        val lang = sharedPreferences?.getString("lang", "vi") ?: "vi"
        currentLanguage.update { lang }
        val file = File(context.filesDir, "$lang.json")
        //read local 1st
        if (file.exists()) {
            viewModelScope.launch {
                readStringAppFromJsonFile(context, "$lang.json")
            }
        }
    }

    fun fetchLanguage() {
        viewModelScope.launch {
            delay(3.seconds)
            val lang = LanguageService.getLanguage()
            lang.forEach { (key, value) ->
                //save to file
                saveLanguageToFile("$key.json", value)
                if (key == currentLanguage.value) {
                    appLanguage.update { value }
                }
            }
        }
    }

    fun changeLanguage(specificLang: String? = null) {
        viewModelScope.launch {
            var languageObject: AppLanguage
            val newLang = specificLang
                ?: when (currentLanguage.value) {
                    "vi" -> "en"
                    else -> "vi"
                }

            currentLanguage.update {
                newLang
            }
            sharedPreferences.edit { putString("lang", newLang) }
            languageObject = readStringAppFromJsonFile(context, "${newLang}.json")
            appLanguage.update { languageObject }
        }
    }


    private fun saveLanguageToFile(fileName: String, stringApp: AppLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(AppLanguage::class.java)

            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output.write(adapter.toJson(stringApp).toByteArray())
            }
        }
    }

    suspend fun readStringAppFromJsonFile(context: Context, fileName: String): AppLanguage {
        var stringApp: AppLanguage
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                //default to empty StringApp if file does not exist
                stringApp = AppLanguage()
            }

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(AppLanguage::class.java)

            val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            stringApp = adapter.fromJson(jsonString) ?: AppLanguage()
        }
        return stringApp
    }
}