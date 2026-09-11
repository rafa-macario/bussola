package br.com.fiap.bussola.ui.simulador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.formato.Formatador
import br.com.fiap.bussola.core.ui.componente.CabecalhoTela
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.MolduraOuro
import br.com.fiap.bussola.core.ui.componente.NumeroMostrador
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola
import br.com.fiap.bussola.dominio.modelo.ResultadoSimulacao
import br.com.fiap.bussola.dominio.uso.SimularDivida

@Composable
fun SimuladorTela(
    modifier: Modifier = Modifier,
    viewModel: SimuladorViewModel = viewModel(factory = SimuladorViewModel.Fabrica)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    SimuladorConteudo(
        estado = estado,
        aoDigitarValor = viewModel::aoDigitarValor,
        aoMudarPrazo = viewModel::aoMudarPrazo,
        modifier = modifier
    )
}

@Composable
private fun SimuladorConteudo(
    estado: SimuladorEstado,
    aoDigitarValor: (String) -> Unit,
    aoMudarPrazo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cores = LocalCoresBussola.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Espacos.medio, vertical = Espacos.pequeno)
    ) {
        CabecalhoTela(stringResource(R.string.simulador_titulo))
        Spacer(Modifier.height(Espacos.grande))

        OutlinedTextField(
            value = estado.valorDigitado,
            onValueChange = aoDigitarValor,
            label = { Text(stringResource(R.string.simulador_valor)) },
            singleLine = true,
            isError = estado.valorInvalido,
            supportingText = if (estado.valorInvalido) {
                { Text(stringResource(R.string.simulador_valor_invalido)) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Espacos.grande))

        Rotulo(stringResource(R.string.simulador_prazo, estado.meses), cor = cores.textoLegenda)
        Slider(
            value = estado.meses.toFloat(),
            onValueChange = { aoMudarPrazo(it.toInt()) },
            valueRange = 1f..SimularDivida.MAX_MESES.toFloat(),
            steps = SimularDivida.MAX_MESES - 2,
            colors = SliderDefaults.colors(
                thumbColor = cores.ouro,
                activeTrackColor = cores.ouro,
                inactiveTrackColor = cores.linha
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Espacos.medio))

        val resultado = estado.resultado
        if (resultado == null) {
            Text(
                text = stringResource(R.string.simulador_sem_taxa),
                color = cores.textoCorpo,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            ResultadoDaSimulacao(resultado)
        }
        Spacer(Modifier.height(Espacos.grande))
    }
}

@Composable
private fun ResultadoDaSimulacao(resultado: ResultadoSimulacao) {
    val cores = LocalCoresBussola.current

    MolduraOuro(corBorda = cores.dividaBorda, corFundo = cores.dividaFundo) {
        Rotulo(
            stringResource(R.string.simulador_rotativo),
            modifier = Modifier.fillMaxWidth(),
            cor = cores.dividaRotulo,
            alinhamento = TextAlign.Center
        )
        Spacer(Modifier.height(Espacos.minimo))
        NumeroMostrador(
            texto = Formatador.moeda(resultado.montanteRotativo, comCentavos = false),
            modifier = Modifier.fillMaxWidth(),
            cor = cores.dividaTexto
        )
    }

    Spacer(Modifier.height(Espacos.medio))

    MolduraOuro {
        Rotulo(
            stringResource(R.string.simulador_alternativa),
            modifier = Modifier.fillMaxWidth(),
            alinhamento = TextAlign.Center
        )
        Spacer(Modifier.height(Espacos.minimo))
        NumeroMostrador(
            texto = Formatador.moeda(resultado.totalAlternativa, comCentavos = false),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(
                R.string.simulador_parcela,
                Formatador.moeda(resultado.parcelaAlternativa)
            ),
            modifier = Modifier.fillMaxWidth(),
            color = cores.textoLegenda,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }

    Spacer(Modifier.height(Espacos.medio))

    Text(
        text = stringResource(
            R.string.simulador_diferenca,
            Formatador.moeda(resultado.economia, comCentavos = false)
        ),
        modifier = Modifier.fillMaxWidth(),
        color = cores.textoCorpo,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}
