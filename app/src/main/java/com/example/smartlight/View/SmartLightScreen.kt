package com.example.smartlight.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartLightScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var lights by remember { mutableStateOf(listOf("Light 1", "Light 2", "Light 3")) }
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Smart Light Controller", style = MaterialTheme.typography.headlineSmall) })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController.navigate("settings") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Button(
                onClick = { lights = listOf("Living Room", "Bedroom", "Kitchen") },
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) {
                Text("Find Lights")
            }

            Spacer(modifier = Modifier.height(16.dp))

            lights.forEach { lightId ->
                Button(
                    onClick = { selectedLight = lightId },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                ) {
                    Text("Select Light: $lightId")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedLight?.let { lightId ->
                Text("Selected Light: $lightId", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Toggle Light", modifier = Modifier.padding(end = 10.dp))
                    Switch(
                        checked = isOn,
                        onCheckedChange = {
                            isOn = it
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Change Color", style = MaterialTheme.typography.titleMedium)
                HarmonyColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    harmonyMode = ColorHarmonyMode.COMPLEMENTARY,
                    color = HsvColor.from(selectedColor),
                    onColorChanged = { color ->
                        selectedColor = color.toColor()
                    }
                )
            }

            errorMessage?.let {
                Text(text = "Status: $it", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
