package br.com.fiap.bussola.core.ui.tema

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaEscuro = darkColorScheme(
    primary = OuroClaro,
    onPrimary = VerdeMostrador,
    secondary = OuroProfundo,
    onSecondary = Marfim,
    background = VerdeMostrador,
    onBackground = Marfim,
    surface = VerdeMostrador,
    onSurface = Marfim,
    surfaceVariant = VerdeLinha,
    onSurfaceVariant = VerdeTexto,
    outline = OuroProfundo,
    outlineVariant = VerdeLinha,
    error = BorgonhaBorda,
    onError = Marfim
)

private val EsquemaClaro = lightColorScheme(
    primary = VerdeTinta,
    onPrimary = MarfimFundo,
    secondary = OuroLinha,
    onSecondary = MarfimFundo,
    background = MarfimFundo,
    onBackground = VerdeTinta,
    surface = BrancoCarta,
    onSurface = VerdeTinta,
    surfaceVariant = LinhaClara,
    onSurfaceVariant = VerdeCorpo,
    outline = OuroLinha,
    outlineVariant = LinhaClara,
    error = BorgonhaBorda,
    onError = MarfimFundo
)


@Composable
fun BussolaTema(
    escuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val esquema = if (escuro) EsquemaEscuro else EsquemaClaro
    val coresMarca = if (escuro) CoresBussolaEscuro else CoresBussolaClaro

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val janela = (view.context as Activity).window
            janela.statusBarColor = esquema.background.toArgb()
            janela.navigationBarColor = esquema.background.toArgb()
            WindowCompat.getInsetsController(janela, view).isAppearanceLightStatusBars = !escuro
        }
    }

    CompositionLocalProvider(LocalCoresBussola provides coresMarca) {
        MaterialTheme(
            colorScheme = esquema,
            typography = TipografiaBussola,
            content = content
        )
    }
}
