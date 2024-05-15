package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.BMIViewModel

@Composable
fun BMICalculatorScreen(viewModel: BMIViewModel) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<Float?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                bmiResult = viewModel.calculateBMI(weight.toFloatOrNull(), height.toFloatOrNull())
            }
        ) {
            Text("Calculate BMI")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your BMI: ${bmiResult?.toString() ?: "N/A"}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}