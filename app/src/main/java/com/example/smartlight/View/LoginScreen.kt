package com.example.smartlight.View

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartlight.Viewmodel.SmartLightViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: SmartLightViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage
    val context = navController.context

    Scaffold(
        containerColor = Color.White,
        contentColor = Color.Black
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
                Text("Zaloguj się", style = MaterialTheme.typography.headlineLarge, color = Color.Black)

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Hasło") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                                navController.navigate("smartLightScreen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zaloguj się")
                }

                Button(
                    onClick = {
                        viewModel.signInWithFacebook(context, viewModel.callbackManager) { user ->
                            if (user != null) {
                                Toast.makeText(context, "Zalogowano przez Facebooka: ${user.displayName}", Toast.LENGTH_SHORT).show()
                                navController.navigate("smartLightScreen")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zaloguj się przez Facebooka")
                }

                TextButton(
                    onClick = { navController.navigate("register") },
                ) {
                    Text("Nie masz konta? Zarejestruj się")
                }

                errorMessage?.let {
                    Text("Błąd: $it", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
