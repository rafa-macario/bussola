package br.com.fiap.bussola.core.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola


@Composable
fun FioLuneta(modifier: Modifier = Modifier) {
    val cores = LocalCoresBussola.current
    val destaque = MaterialTheme.colorScheme.onBackground
    val alturas = listOf(18.dp to cores.ouro, 26.dp to cores.ouro, 34.dp to destaque,
        26.dp to cores.ouro, 18.dp to cores.ouro)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        alturas.forEach { (altura, cor) ->
            Box(
                Modifier
                    .width(2.dp)
                    .height(altura)
                    .background(cor)
            )
        }
    }
}

@Composable
fun Rotulo(
    texto: String,
    modifier: Modifier = Modifier,
    cor: Color = LocalCoresBussola.current.ouroRotulo,
    alinhamento: TextAlign? = null
) {
    Text(
        text = texto.uppercase(),
        modifier = modifier,
        color = cor,
        style = MaterialTheme.typography.labelMedium,
        textAlign = alinhamento
    )
}

@Composable
fun CabecalhoTela(titulo: String, modifier: Modifier = Modifier) {
    val cores = LocalCoresBussola.current
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Rotulo(titulo, alinhamento = TextAlign.Center)
        Box(
            Modifier
                .padding(top = 8.dp)
                .width(32.dp)
                .height(1.dp)
                .background(cores.ouro)
        )
    }
}

@Composable
fun MolduraOuro(
    modifier: Modifier = Modifier,
    corBorda: Color = LocalCoresBussola.current.ouro,
    corFundo: Color = LocalCoresBussola.current.superficieCartao,
    conteudo: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(corFundo)
            .border(1.dp, corBorda)
            .padding(16.dp),
        content = conteudo
    )
}

@Composable
fun NumeroMostrador(
    texto: String,
    modifier: Modifier = Modifier,
    cor: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = texto,
        modifier = modifier,
        color = cor,
        style = MaterialTheme.typography.displayMedium
    )
}

@Composable
fun Divisoria(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = LocalCoresBussola.current.linha
    )
}

object Espacos {
    val minimo = 4.dp
    val pequeno = 8.dp
    val medio = 16.dp
    val grande = 24.dp
    val secao = 32.dp
    val alvoToque = 48.dp
}

@Composable
fun MarcaDeAlerta(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(6.dp)
            .background(LocalCoresBussola.current.dividaBorda)
    )
}
