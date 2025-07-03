package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myapplication.language.AppLanguage
import com.example.myapplication.language.LanguageViewModel
import com.example.myapplication.language.LanguageViewModelFactory

val LocalLanguage = staticCompositionLocalOf<AppLanguage> {
    AppLanguage()
}


class MainActivity2 : ComponentActivity() {
    val languageViewModel: LanguageViewModel by viewModels<LanguageViewModel>(
        factoryProducer = { LanguageViewModelFactory(this@MainActivity2.applicationContext) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { !languageViewModel.loadedState.value }
        enableEdgeToEdge()

        setContent {
            val appString = languageViewModel.appLanguage.collectAsState()

            CompositionLocalProvider(LocalLanguage provides appString.value) {
                Scaffold { padding ->
                    Column(
                        Modifier
                            .padding(padding)
                            .padding(horizontal = 12.dp)
                    ) {
                        ProfileView(languageViewModel)
                    }
                }
            }
        }
        languageViewModel.fetchLanguage()
    }
}

@Composable
fun ProfileView(languageViewModel: LanguageViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            colorFilter = ColorFilter.tint(Color.Gray),
        )
        Row {
            Text(
                text = LocalLanguage.current.profileName,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = LocalLanguage.current.profileBio,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(onClick = {
            languageViewModel.changeLanguage()
        }) {
            Text(text = LocalLanguage.current.changeLang)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = {
                languageViewModel.changeLanguage("vi")
            }) {
                Text(text = "\uD83C\uDDFB\uD83C\uDDF3")
            }
            Button(onClick = {
                languageViewModel.changeLanguage("en")
            }) {
                Text(text = "\uD83C\uDDEC\uD83C\uDDE7")
            }
        }
    }
}