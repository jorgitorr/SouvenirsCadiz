package com.example.souvenirscadiz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.souvenirscadiz.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        color = RaisanBlack,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


/**
 * letra nueva kiwiMaru añadida
 */
val KiwiMaru = FontFamily(
    Font(R.font.kiwimarumedium, FontWeight.Medium),
    Font(R.font.kiwimarulight, FontWeight.Light),
    Font(R.font.kiwimaruregular, FontWeight.Normal)
)

val KneWave = FontFamily(
    Font(R.font.knewaveregular, FontWeight.Normal)
)

val KleeOne = FontFamily(
    Font(R.font.kleeoneregular, FontWeight.Normal),
    Font(R.font.kleeonesemibold, FontWeight.Bold)
)