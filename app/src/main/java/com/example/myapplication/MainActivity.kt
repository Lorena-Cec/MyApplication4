package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.myapplication.view.BMICalculatorScreen
import com.example.myapplication.view.BackgroundImage
import com.example.myapplication.viewmodel.BMIViewModel

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
            val currentUser = FirebaseAuth.getInstance().currentUser
            val navController = rememberNavController()

            NavHost(navController, startDestination = "main_screen") {
                composable("main_screen") {
                    if (currentUser != null) {
                        // Korisnik je prijavljen, prikaži ekran sa korisničkim podacima
                        UserInfoScreen(currentUser = currentUser, onLogout = {}, navController = navController)
                    } else {
                        // Korisnik nije prijavljen, prikaži Sign-in/Register Screen
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

        Button(onClick = { register(context, email, password) }) {
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
                // Navigiraj na UserInfoScreen nakon uspješne prijave
                navController.navigate("main_screen")
            } else {
                // Prijavljivanje neuspješno
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
}


private fun register(context: Context, email: String, password: String) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registracija uspješna
                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Registracija neuspješna
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
}

@Composable
fun UserInfoScreen(currentUser: FirebaseUser, onLogout: () -> Unit, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, ${currentUser.displayName ?: "User"}!")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email: ${currentUser.email ?: "N/A"}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            // Odjavi korisnika
            FirebaseAuth.getInstance().signOut()
            // Navigiraj na ekran za prijavu/registraciju
            navController.navigate("login_register_screen")
        }) {
            Text("Logout")
        }
    }
}

