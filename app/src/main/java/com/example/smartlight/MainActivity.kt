package com.example.smartlight

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartlight.View.LoginScreen
import com.example.smartlight.View.RegisterScreen
import com.example.smartlight.View.WebViewScreen
import com.example.smartlight.View.SmartLightScreen
import com.example.smartlight.View.UserSettingsScreen
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

private lateinit var callbackManager: CallbackManager

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setClientToken(getString(R.string.facebook_auth_code))
        AppEventsLogger.activateApp(application)
        callbackManager = CallbackManager.Factory.create()
        setContent {
            AppNav()
        }
        handleOAuthCallback(intent)

    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleOAuthCallback(intent)
    }

    private fun handleOAuthCallback(intent: Intent) {
        val data: Uri? = intent.data
        if (data != null && data.toString().startsWith("myapp://callback")) {
            data.getQueryParameter("code") // Pobierz kod autoryzacyjny
        }
    }

}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("authCode")
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("smartLightScreen") { SmartLightScreen(navController) }
        composable("settings") { UserSettingsScreen(navController) }
        composable("webViewScreen/{authUrl}") { backStackEntry ->
            val authUrl = backStackEntry.arguments?.getString("authUrl") ?: ""
            WebViewScreen(navController, authUrl)
        }
    }
}



