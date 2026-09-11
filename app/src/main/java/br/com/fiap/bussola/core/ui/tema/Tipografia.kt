package br.com.fiap.bussola.core.ui.tema

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val FamiliaMostrador = FontFamily.Serif
val FamiliaTexto = FontFamily.SansSerif

private const val ESPACO_CAIXA_ALTA = 0.18

val TipografiaBussola = Typography(
    displayLarge = TextStyle(
        fontFamily = FamiliaMostrador,
        fontWeight = FontWeight.Normal,
        fontSize = 44.sp,
        lineHeight = 50.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FamiliaMostrador,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FamiliaMostrador,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FamiliaMostrador,
        fontWeight = FontWeight.Normal,
        fontSize = 21.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FamiliaMostrador,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FamiliaTexto,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FamiliaTexto,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FamiliaTexto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = ESPACO_CAIXA_ALTA.times(12).sp
    ),
    labelMedium = TextStyle(
        fontFamily = FamiliaTexto,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = ESPACO_CAIXA_ALTA.times(11).sp
    ),
    labelSmall = TextStyle(
        fontFamily = FamiliaTexto,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = ESPACO_CAIXA_ALTA.times(10).sp
    )
)
