package com.example.smartlight.Viewmodel

import android.content.Context
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun signInWithEmailAndPassword(email: String, password: String, context: Context, onResult: (FirebaseUser?) -> Unit) {
    // Sprawdzamy, czy dane wejściowe są poprawne
    if (email.isBlank() || password.isBlank()) {
        Toast.makeText(context, "E-mail i hasło nie mogą być puste", Toast.LENGTH_SHORT).show()
        return
    }

    // Jeśli dane są poprawne, wykonujemy logowanie
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Logowanie zakończone sukcesem
                val user = FirebaseAuth.getInstance().currentUser
                onResult(user)
            } else {
                // Błąd logowania
                Toast.makeText(context, "Błąd logowania: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                onResult(null)
            }
        }
}

fun signInWithFacebook(context: Context, callbackManager: CallbackManager, onResult: (FirebaseUser?) -> Unit) {
    // Rozpoczynamy logowanie przez Facebooka
    LoginManager.getInstance().logInWithReadPermissions(
        context as androidx.activity.ComponentActivity,
        listOf("email")
    )

    LoginManager.getInstance().registerCallback(
        callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result?.accessToken
                token?.let {
                    val credential = FacebookAuthProvider.getCredential(it.token)
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                onResult(user)
                            } else {
                                onResult(null)
                                Toast.makeText(context, "Błąd logowania z Facebookiem: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

            override fun onCancel() {
                // Obsługa anulowania logowania
                Toast.makeText(context, "Logowanie anulowane", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                // Obsługa błędów logowania
                Toast.makeText(context, "Błąd logowania: ${error?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
