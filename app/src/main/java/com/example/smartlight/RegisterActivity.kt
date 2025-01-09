package com.example.smartlight

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartlight.View.PhilipsHueOAuth
import com.example.smartlight.View.SmartLightScreen

class YourActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obsługa przekierowania z URI w `onCreate`
        intent?.data?.let { uri ->
            handleRedirectUri(uri)
        }

        setContent {
            SmartLightScreen()
        }
    }

    private fun handleRedirectUri(uri: Uri) {
        val authCode = uri.getQueryParameter("code")
        if (authCode != null) {
            PhilipsHueOAuth.getAccessToken(
                authCode = authCode,
                redirectUri = "myapp://callback",
                onSuccess = { token ->
                    saveAccessToken(token)
                },
                onError = { error ->
                    showError(error)
                }
            )
        }
    }

    private fun saveAccessToken(token: String) {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    private fun showError(error: String) {
        runOnUiThread {
            Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
        }
    }
}
