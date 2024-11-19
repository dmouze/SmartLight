package com.example.smartlight.View

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartlight.Viewmodel.signInWithEmailAndPassword
import com.example.smartlight.Viewmodel.signInWithFacebook
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseUser

object Colors {
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF1F1F1F)
    val Gray = Color(0xFF6B6B6B)
    val Blue = Color(0xFF0000FF)
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<FirebaseUser?>(null) }

    // Facebook CallbackManager
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Funkcja logowania e-mailowego
    val loginWithEmail = {
        signInWithEmailAndPassword(email, password, navController.context) { loggedInUser ->
            if (loggedInUser != null) {
                user = loggedInUser
                Toast.makeText(navController.context, "Zalogowano: ${loggedInUser.email}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(navController.context, "Błąd logowania", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funkcja logowania przez Facebooka
    val loginWithFacebook = {
        signInWithFacebook(navController.context, callbackManager) { loggedInUser ->
            if (loggedInUser != null) {
                user = loggedInUser
                Toast.makeText(navController.context, "Zalogowano przez Facebooka: ${loggedInUser.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(navController.context, "Błąd logowania przez Facebooka", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = Colors.White,
        contentColor = Colors.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "SmartLight",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Colors.Black
                )

                Box(modifier = Modifier.padding(36.dp))

                Text(
                    text = "Zaloguj się",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Colors.Black
                )

                Box(modifier = Modifier.padding(16.dp))

                // Email input field
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail", color = Colors.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Colors.White,
                        focusedTextColor = Colors.White,
                        unfocusedTextColor = Colors.Gray,
                        focusedContainerColor = Colors.Black,
                        unfocusedContainerColor = Colors.Black,
                        focusedIndicatorColor = Colors.Blue.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f)
                    )
                )

                // Password input field
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Hasło", color = Colors.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        cursorColor = Colors.White,
                        focusedTextColor = Colors.White,
                        unfocusedTextColor = Colors.Gray,
                        focusedContainerColor = Colors.Black,
                        unfocusedContainerColor = Colors.Black,
                        focusedIndicatorColor = Colors.Blue.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f)
                    )
                )

                // Login Button (for email login)
                Button(
                    onClick = { loginWithEmail() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Black,
                        contentColor = Colors.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zaloguj się za pomocą E-mail")
                }

                // Login Button (for Facebook login)
                Button(
                    onClick = { loginWithFacebook() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Blue,
                        contentColor = Colors.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zaloguj się przez Facebooka")
                }

                // Forgot password text
                TextButton(
                    onClick = { /* Resetowanie hasła */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.Gray
                    )
                ) {
                    Text("Zapomniałeś hasła?")
                }

                // Navigate to register screen
                TextButton(
                    onClick = { navController.navigate("register") },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.Gray
                    )
                ) {
                    Text("Nie masz konta? Zarejestruj się")
                }
            }
        }
    }
}
