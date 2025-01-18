package com.example.smartlight

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.smartlight.View.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : ComponentActivity() {

    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(navController = rememberNavController())
        }
    }

    override fun onStart() {
        super.onStart()
        isCurrentUser()
    }

   private fun isCurrentUser() {
       fbAuth.currentUser?.let { auth ->
           val intent = Intent(applicationContext, MainActivity::class.java).apply {
               flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
           }
           startActivity(intent)
       }
   }

}
