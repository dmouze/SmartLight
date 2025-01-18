package com.example.smartlight.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.smartlight.api.PhilipsHueApi
import com.example.smartlight.api.PhilipsHueOAuth
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartLightScreen(navController: NavController, authCode: String?) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var accessToken by remember { mutableStateOf<String?>(null) }
    var lights by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Light Controller") },
                actions = {
                    Button(onClick = { navController.navigate("userSettings") }) {
                        Text("Ustawienia")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (authCode == null) {
                Button(onClick = {
                    val authUrl = "https://api.meethue.com/oauth2/auth" +
                            "?client_id=763YWtntTO94BzQPhRJSAXbvCeD8nWgg" +
                            "&response_type=code" +
                            "&redirect_uri=myapp://callback" +
                            "&state=randomState"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
                    context.startActivity(intent)
                }) {
                    Text("Login with Philips Hue")
                }
            } else if (accessToken == null) {
                Button(onClick = {
                    PhilipsHueOAuth.getAccessToken(
                        authCode = authCode!!,
                        redirectUri = "myapp://callback",
                        onSuccess = { token ->
                            accessToken = token
                            errorMessage = "Successfully authenticated!"
                        },
                        onError = { error ->
                            errorMessage = "Authentication failed: $error"
                        }
                    )
                }) {
                    Text("Get Access Token")
                }
            } else {
                Button(onClick = {
                    coroutineScope.launch {
                        PhilipsHueApi.getLights(
                            accessToken = accessToken!!,
                            onSuccess = { lights = it },
                            onError = { errorMessage = it }
                        )
                    }
                }) {
                    Text("Find Lights")
                }

                Spacer(modifier = Modifier.height(16.dp))

                lights.forEach { lightId ->
                    Button(onClick = { selectedLight = lightId }) {
                        Text("Select Light: $lightId")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                selectedLight?.let { lightId ->
                    Text("Selected Light: $lightId")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Toggle Light")
                        Switch(checked = isOn, onCheckedChange = {
                            isOn = it
                            coroutineScope.launch {
                                PhilipsHueApi.toggleLight(
                                    accessToken = accessToken!!,
                                    lightId = lightId,
                                    isOn = isOn,
                                    onSuccess = {},
                                    onError = { errorMessage = it }
                                )
                            }
                        })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Change Color")
                    HarmonyColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        harmonyMode = ColorHarmonyMode.COMPLEMENTARY,
                        color = HsvColor.from(selectedColor),
                        onColorChanged = { color ->
                            selectedColor = color.toColor()
                            coroutineScope.launch {
                                PhilipsHueApi.changeLightColor(
                                    accessToken = accessToken!!,
                                    lightId = lightId,
                                    color = "#${Integer.toHexString(selectedColor.toArgb())}",
                                    onSuccess = {},
                                    onError = { errorMessage = it }
                                )
                            }
                        }
                    )
                }

                errorMessage?.let {
                    Text(text = "Status: $it", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}


@Composable
fun UserSettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("User Settings", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Change password logic */ }) {
            Text("Change Password")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* Pair new devices logic */ }) {
            Text("Pair New Device")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* Disconnect devices logic */ }) {
            Text("Disconnect Device")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("main") }) {
            Text("Back to Main Screen")
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(navController: NavController, authUrl: String) {
    var authCode by remember { mutableStateOf<String?>(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        if (url.startsWith("myapp://callback")) {
                            val uri = Uri.parse(url)
                            authCode = uri.getQueryParameter("code") // Pobierz kod autoryzacji
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("authCode", authCode)
                            navController.popBackStack() // Wróć do SmartLightScreen
                            return true
                        }
                        return false
                    }
                }
                loadUrl(authUrl)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
