package br.com.fiap.bussola.ui.navegacao

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola

private data class ItemNavegacao(
    val rota: Rota,
    val icone: ImageVector,
    val rotuloRes: Int
)

private val itens = listOf(
    ItemNavegacao(Rota.Painel, Icons.Outlined.Home, R.string.nav_inicio),
    ItemNavegacao(Rota.Simulador, Icons.Outlined.Calculate, R.string.nav_simular),
    ItemNavegacao(Rota.Orcamento, Icons.Outlined.AccountBalanceWallet, R.string.nav_conta),
    ItemNavegacao(Rota.Trilha, Icons.Outlined.MenuBook, R.string.nav_trilha)
)

@Composable
fun BarraInferior(
    rotaAtual: String?,
    aoSelecionar: (Rota) -> Unit
) {
    val cores = LocalCoresBussola.current
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = cores.textoLegenda
    ) {
        itens.forEach { item ->
            val selecionado = rotaAtual == item.rota.caminho
            val rotulo = stringResource(item.rotuloRes)
            NavigationBarItem(
                selected = selecionado,
                onClick = { if (!selecionado) aoSelecionar(item.rota) },
                icon = { Icon(item.icone, contentDescription = rotulo) },
                label = { Text(rotulo, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = cores.ouro,
                    selectedTextColor = cores.ouro,
                    unselectedIconColor = cores.textoLegenda,
                    unselectedTextColor = cores.textoLegenda,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
