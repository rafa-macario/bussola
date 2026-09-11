package br.com.fiap.bussola.ui.navegacao

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.com.fiap.bussola.ui.abertura.AberturaTela
import br.com.fiap.bussola.ui.licao.LicaoTela
import br.com.fiap.bussola.ui.orcamento.OrcamentoTela
import br.com.fiap.bussola.ui.painel.PainelTela
import br.com.fiap.bussola.ui.simulador.SimuladorTela
import br.com.fiap.bussola.ui.trilha.TrilhaTela


@Composable
fun NavGrafoBussola(
    navController: NavHostController = rememberNavController()
) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route
    val mostrarBarra = rotaAtual in Rota.comBarraInferior

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(
                    rotaAtual = rotaAtual,
                    aoSelecionar = { rota -> navController.navegarParaSecao(rota) }
                )
            }
        }
    ) { espacamento ->
        NavHost(
            navController = navController,
            startDestination = Rota.Abertura.caminho,
            modifier = Modifier.padding(espacamento)
        ) {
            composable(Rota.Abertura.caminho) {
                AberturaTela(
                    aoComecar = {
                        navController.navigate(Rota.Painel.caminho) {
                            popUpTo(Rota.Abertura.caminho) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rota.Painel.caminho) { PainelTela() }

            composable(Rota.Simulador.caminho) { SimuladorTela() }

            composable(Rota.Orcamento.caminho) { OrcamentoTela() }

            composable(Rota.Trilha.caminho) {
                TrilhaTela(
                    aoAbrirModulo = { id -> navController.navigate(Rota.Licao.comId(id)) }
                )
            }

            composable(
                route = Rota.Licao.caminho,
                arguments = listOf(navArgument(Rota.Licao.ARG_MODULO) {
                    type = NavType.StringType
                })
            ) { entrada ->
                val moduloId = entrada.arguments?.getString(Rota.Licao.ARG_MODULO).orEmpty()
                LicaoTela(
                    moduloId = moduloId,
                    aoVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun NavHostController.navegarParaSecao(rota: Rota) {
    navigate(rota.caminho) {
        popUpTo(Rota.Painel.caminho) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
