package br.com.fiap.bussola.ui.painel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.formato.Formatador
import br.com.fiap.bussola.core.ui.componente.CabecalhoTela
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.MarcaDeAlerta
import br.com.fiap.bussola.core.ui.componente.MolduraOuro
import br.com.fiap.bussola.core.ui.componente.NumeroMostrador
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola
import br.com.fiap.bussola.dominio.modelo.Indicador
import br.com.fiap.bussola.dominio.modelo.PainelIndicadores


@Composable
fun PainelTela(
    modifier: Modifier = Modifier,
    viewModel: PainelViewModel = viewModel(factory = PainelViewModel.Fabrica)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    PainelConteudo(
        estado = estado,
        aoAtualizar = { viewModel.carregar(forcarAtualizacao = true) },
        modifier = modifier
    )
}

@Composable
private fun PainelConteudo(
    estado: PainelEstado,
    aoAtualizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cores = LocalCoresBussola.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Espacos.medio, vertical = Espacos.pequeno)
    ) {
        CabecalhoTela(stringResource(R.string.painel_titulo))
        Spacer(Modifier.height(Espacos.grande))

        when {
            estado.carregando && estado.indicadores == null -> CarregandoIndicadores()
            estado.houveFalha && estado.indicadores == null -> FalhaDeRede(aoAtualizar)
            estado.indicadores != null -> Indicadores(estado.indicadores)
        }

        if (estado.indicadores != null) {
            Spacer(Modifier.height(Espacos.medio))
            Text(
                text = stringResource(
                    R.string.painel_fonte,
                    Formatador.data(estado.indicadores.selicMeta?.dataReferencia)
                ),
                modifier = Modifier.fillMaxWidth(),
                color = cores.textoLegenda,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Espacos.pequeno))
            OutlinedButton(
                onClick = aoAtualizar,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    stringResource(R.string.painel_atualizar).uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(Modifier.height(Espacos.grande))
    }
}

@Composable
private fun Indicadores(dados: PainelIndicadores) {
    val cores = LocalCoresBussola.current

    MolduraOuro {
        Rotulo(
            stringResource(R.string.painel_selic_meta),
            modifier = Modifier.fillMaxWidth(),
            alinhamento = TextAlign.Center
        )
        Spacer(Modifier.height(Espacos.minimo))
        NumeroMostrador(
            texto = Formatador.numero(dados.selicMeta?.valorPercentual ?: 0.0, casas = 2),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(R.string.painel_unidade_ano),
            modifier = Modifier.fillMaxWidth(),
            color = cores.textoLegenda,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }

    Spacer(Modifier.height(Espacos.medio))

    Row(horizontalArrangement = Arrangement.spacedBy(Espacos.medio)) {
        IndicadorSecundario(
            rotulo = stringResource(R.string.painel_ipca),
            indicador = dados.ipcaMensal,
            modifier = Modifier.weight(1f)
        )
        IndicadorSecundario(
            rotulo = stringResource(R.string.painel_selic_mes),
            indicador = dados.selicNoMes,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(Modifier.height(Espacos.medio))
    CartaoRotativo(dados)
}

@Composable
private fun IndicadorSecundario(
    rotulo: String,
    indicador: Indicador?,
    modifier: Modifier = Modifier
) {
    val cores = LocalCoresBussola.current
    Column(
        modifier = modifier.padding(top = Espacos.pequeno),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Rotulo(rotulo, cor = cores.textoLegenda, alinhamento = TextAlign.Center)
        Spacer(Modifier.height(Espacos.minimo))
        Text(
            text = indicador?.let { Formatador.percentual(it.valorPercentual) } ?: "—",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun CartaoRotativo(dados: PainelIndicadores) {
    val cores = LocalCoresBussola.current
    MolduraOuro(corBorda = cores.dividaBorda, corFundo = cores.dividaFundo) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MarcaDeAlerta()
            Rotulo(
                "  " + stringResource(R.string.painel_rotativo),
                cor = cores.dividaRotulo
            )
        }
        Spacer(Modifier.height(Espacos.minimo))
        NumeroMostrador(
            texto = dados.cartaoRotativo
                ?.let { Formatador.percentual(it.valorPercentual, casas = 1) } ?: "—",
            modifier = Modifier.fillMaxWidth(),
            cor = cores.dividaTexto
        )
        dados.rotativoSobreSelic?.let { vezes ->
            Text(
                text = stringResource(
                    R.string.painel_comparacao,
                    Formatador.numero(vezes, casas = 0)
                ),
                modifier = Modifier.fillMaxWidth(),
                color = cores.dividaRotulo,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CarregandoIndicadores() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = LocalCoresBussola.current.ouro,
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun FalhaDeRede(aoTentar: () -> Unit) {
    val cores = LocalCoresBussola.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.painel_erro),
            color = cores.textoCorpo,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Espacos.medio))
        OutlinedButton(onClick = aoTentar) {
            Text(
                stringResource(R.string.painel_tentar).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Unspecified
            )
        }
    }
}
