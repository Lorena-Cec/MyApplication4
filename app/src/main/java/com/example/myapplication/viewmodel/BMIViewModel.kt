package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.myapplication.model.BMIModel

class BMIViewModel : ViewModel() {
    val bmiModel = BMIModel(
        weight = null,
        height = null,
        bmiResult = null)

    fun calculateBMI(weight: Float?, height: Float?): Float? {
        return if (weight != null && height != null && height > 0) {
            val bmi = weight / ((height / 100) * (height / 100))
            bmiModel.bmiResult = bmi
            bmi
        } else { bmiModel.bmiResult = null
            null
        }
    }
}
