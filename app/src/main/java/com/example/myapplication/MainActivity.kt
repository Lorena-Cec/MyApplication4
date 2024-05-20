package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentUser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
            val navController = rememberNavController()

            FirebaseAuth.getInstance().addAuthStateListener { auth ->
                currentUser.value = auth.currentUser
            }

            NavHost(navController, startDestination = "main_screen") {
                composable("main_screen") {
                    if (currentUser.value != null) {
                        UserInfoScreen(currentUser = currentUser.value!!, onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("login_register_screen")
                        })
                    } else {
                        LoginRegisterScreen(navController = navController)
                    }
                }
                composable("login_register_screen") {
                    LoginRegisterScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun LoginRegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { signIn(context, email, password, navController) }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { register(context, email, password, navController) }) {
            Text("Register")
        }
    }
}

private fun signIn(context: Context, email: String, password: String, navController: NavController) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Prijavljeno uspješno
                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen")
            } else {
                // Prijavljivanje neuspješno
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun register(context: Context, email: String, password: String, navController: NavController) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registracija uspješna
                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen")
            } else {
                // Registracija neuspješna
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
}

@Composable
fun UserInfoScreen(currentUser: FirebaseUser, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome!")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email: ${currentUser.email ?: "N/A"}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            onLogout()
        }) {
            Text("Logout")
        }
    }
}
