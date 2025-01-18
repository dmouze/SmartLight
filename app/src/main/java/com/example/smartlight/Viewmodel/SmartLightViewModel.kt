package com.example.smartlight.Viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class SmartLightViewModel : ViewModel() {

    val lights = mutableStateOf<List<String>>(emptyList())
    val errorMessage = mutableStateOf<String?>(null)
    val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val accessToken = mutableStateOf<String?>(null)

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        context: Context,
        onResult: (Boolean) -> Unit
    ) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage.value = "Wszystkie pola muszą być wypełnione"
            onResult(false)
            return
        }

        if (password != confirmPassword) {
            errorMessage.value = "Hasła nie są zgodne"
            onResult(false)
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Zarejestrowano: ${FirebaseAuth.getInstance().currentUser?.email}", Toast.LENGTH_SHORT).show()
                    errorMessage.value = null
                    onResult(true)
                } else {
                    errorMessage.value = task.exception?.message ?: "Błąd rejestracji"
                    Toast.makeText(context, "Błąd rejestracji: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    onResult(false)
                }
            }
    }

    fun getLights(onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        accessToken.value?.let { token ->
            val request = Request.Builder()
                .url("https://api.meethue.com/bridge/resource")
                .addHeader("Authorization", "Bearer $token")
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    errorMessage.value = e.message
                    onError()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        response.body?.string()?.let { body ->
                            val jsonObject = JSONObject(body)
                            val newLights = mutableListOf<String>()
                            jsonObject.getJSONArray("data").let { array ->
                                for (i in 0 until array.length()) {
                                    val lightId = array.getJSONObject(i).getString("id")
                                    newLights.add(lightId)
                                }
                            }
                            lights.value = newLights
                            onSuccess()
                        }
                    } else {
                        errorMessage.value = "Error: ${response.message}"
                        onError()
                    }
                }
            })
        } ?: run {
            errorMessage.value = "No access token available."
            onError()
        }
    }

    fun toggleLight(lightId: String, isOn: Boolean, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        accessToken.value?.let { token ->
            val body = JSONObject(mapOf("on" to isOn)).toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("https://api.meethue.com/bridge/resource/$lightId")
                .addHeader("Authorization", "Bearer $token")
                .put(body)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    errorMessage.value = e.message
                    onError()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        errorMessage.value = "Error: ${response.message}"
                        onError()
                    }
                }
            })
        } ?: run {
            errorMessage.value = "No access token available."
            onError()
        }
    }

    fun changeLightColor(lightId: String, color: Color, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        accessToken.value?.let { token ->
            val body = JSONObject(mapOf("color" to color.toString())).toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("https://api.meethue.com/bridge/resource/$lightId")
                .addHeader("Authorization", "Bearer $token")
                .put(body)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    errorMessage.value = e.message
                    onError()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        errorMessage.value = "Error: ${response.message}"
                        onError()
                    }
                }
            })
        } ?: run {
            errorMessage.value = "No access token available."
            onError()
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, context: Context, onResult: (FirebaseUser?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "E-mail i hasło nie mogą być puste", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(FirebaseAuth.getInstance().currentUser)
                } else {
                    Toast.makeText(context, "Błąd logowania: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    onResult(null)
                }
            }
    }

    fun signInWithFacebook(context: Context, callbackManager: CallbackManager, onResult: (FirebaseUser?) -> Unit) {
        LoginManager.getInstance().logInWithReadPermissions(
            context as androidx.activity.ComponentActivity,
            listOf("email")
        )

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken
                    val credential = FacebookAuthProvider.getCredential(token.token)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onResult(FirebaseAuth.getInstance().currentUser)
                            } else {
                                Toast.makeText(context, "Błąd logowania z Facebookiem: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                onResult(null)
                            }
                        }
                }

                override fun onCancel() {
                    Toast.makeText(context, "Logowanie anulowane", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(context, "Błąd logowania: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}