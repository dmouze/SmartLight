package com.example.smartlight.api

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

object PhilipsHueApi {
    private const val BASE_URL = "https://api.meethue.com/bridge" // Zastąp odpowiednią wartością
    private val client = OkHttpClient()

    fun getLights(accessToken: String, onSuccess: (List<String>) -> Unit, onError: (String) -> Unit) {
        val request = Request.Builder()
            .url("$BASE_URL/resource")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { body ->
                        val lights = mutableListOf<String>()
                        val jsonObject = JSONObject(body)
                        jsonObject.getJSONArray("data").let { array ->
                            for (i in 0 until array.length()) {
                                val lightId = array.getJSONObject(i).getString("id")
                                lights.add(lightId)
                            }
                        }
                        onSuccess(lights)
                    }
                } else {
                    onError("Failed to fetch lights: ${response.message}")
                }
            }
        })
    }

    fun toggleLight(accessToken: String, lightId: String, isOn: Boolean, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            JSONObject(mapOf("on" to isOn)).toString()
        )

        val request = Request.Builder()
            .url("$BASE_URL/resource/$lightId")
            .addHeader("Authorization", "Bearer $accessToken")
            .put(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to toggle light: ${response.message}")
                }
            }
        })
    }

    fun changeLightColor(accessToken: String, lightId: String, color: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            JSONObject(mapOf("color" to color)).toString()
        )

        val request = Request.Builder()
            .url("$BASE_URL/resource/$lightId")
            .addHeader("Authorization", "Bearer $accessToken")
            .put(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to change light color: ${response.message}")
                }
            }
        })
    }
}
