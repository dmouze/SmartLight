package com.example.smartlight.api

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object PhilipsHueOAuth {
    internal const val CLIENT_ID = "763YWtntTO94BzQPhRJSAXbvCeD8nWgg"
    internal const val CLIENT_SECRET = "uScYDZfjs4OUD9TG"
    private const val TOKEN_URL = "https://api.meethue.com/oauth2/token"
    const val AUTH_URL = "https://api.meethue.com/oauth2/auth"

    private val client = OkHttpClient()

    fun getAccessToken(authCode: String, redirectUri: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val body = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", authCode)
            .add("redirect_uri", redirectUri)
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .build()

        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { body ->
                        val jsonObject = JSONObject(body)
                        val accessToken = jsonObject.getString("access_token")
                        onSuccess(accessToken)
                    }
                } else {
                    onError("Failed to get access token: ${response.message}")
                }
            }
        })
    }
}
