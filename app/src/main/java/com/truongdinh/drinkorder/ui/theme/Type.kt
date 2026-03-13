package com.truongdinh.drinkorder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.truongdinh.drinkorder.R

val AppFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(

    labelSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
    ),

    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    ),

    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    ),

    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal
    ),

    titleSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold
    ),

    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.SemiBold
    ),

    headlineSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),

    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    )
)