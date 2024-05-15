package com.example.myapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R

@Composable
fun BackgroundImage(modifier: Modifier) {
    Box (modifier){ Image(
        painter = painterResource(id = R.drawable.fitness),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.1F )
    }
}