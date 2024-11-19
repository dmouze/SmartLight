package com.example.smartlight.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                    text = "Zarejestruj się",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Colors.Black
                )

                Box(modifier = Modifier.padding(16.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Imię i nazwisko", color = Colors.Gray) },
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
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f),
                    )
                )

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
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f),
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
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f),
                    )
                )

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Potwierdź hasło", color = Colors.Gray) },
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
                        unfocusedIndicatorColor = Colors.White.copy(alpha = 0.3f),
                    )
                )

                Button(
                    onClick = { /* Logika rejestracji */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Colors.Black,
                        contentColor = Colors.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zarejestruj się")
                }

                TextButton(
                    onClick = {navController.navigate("login")},
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.Gray
                    )
                ) {
                    Text("Masz już konto? Zaloguj się")
                }
            }
        }
    }
}