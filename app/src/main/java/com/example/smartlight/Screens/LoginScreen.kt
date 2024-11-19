package com.example.smartlight.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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

                Button(
                    onClick = { /* Logika logowania */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Black,
                        contentColor = Colors.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zaloguj się")
                }

                TextButton(
                    onClick = { /* Resetowanie hasła */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.Gray
                    )
                ) {
                    Text("Zapomniałeś hasła?")
                }

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