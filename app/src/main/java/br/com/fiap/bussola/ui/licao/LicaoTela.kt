package br.com.fiap.bussola.ui.licao

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.MolduraOuro
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola
import br.com.fiap.bussola.dominio.modelo.Questao

@Composable
fun LicaoTela(
    moduloId: String,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LicaoViewModel = viewModel(
        key = moduloId,
        factory = LicaoViewModel.fabrica(moduloId)
    )
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cores = LocalCoresBussola.current
    val modulo = estado.modulo

    if (modulo == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.trilha_bloqueado),
                color = cores.textoCorpo,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Espacos.medio, vertical = Espacos.pequeno)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = aoVoltar, modifier = Modifier.size(Espacos.alvoToque)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.acao_voltar),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.width(Espacos.pequeno))
            Rotulo(stringResource(R.string.licao_modulo, modulo.numeroRomano))
        }

        Spacer(Modifier.height(Espacos.medio))
        Text(
            text = modulo.titulo,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(Espacos.medio))

        modulo.paragrafos.forEach { paragrafo ->
            Text(
                text = paragrafo,
                color = cores.textoCorpo,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(Espacos.medio))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .width(2.dp)
                    .height(56.dp)
                    .background(cores.ouro)
            )
            Spacer(Modifier.width(Espacos.medio))
            Text(
                text = modulo.destaque,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(Espacos.secao))

        if (estado.finalizado) {
            Text(
                text = stringResource(R.string.licao_final),
                color = cores.ouroRotulo,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(Espacos.medio))
            Button(onClick = aoVoltar, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.acao_voltar).uppercase())
            }
        } else {
            estado.questaoAtual?.let { questao ->
                BlocoQuestao(
                    questao = questao,
                    estado = estado,
                    aoSelecionar = viewModel::selecionar,
                    aoConfirmar = viewModel::confirmar,
                    aoAvancar = viewModel::avancar
                )
            }
        }

        Spacer(Modifier.height(Espacos.secao))
    }
}

@Composable
private fun BlocoQuestao(
    questao: Questao,
    estado: LicaoEstado,
    aoSelecionar: (Int) -> Unit,
    aoConfirmar: () -> Unit,
    aoAvancar: () -> Unit
) {
    val cores = LocalCoresBussola.current

    MolduraOuro {
        Rotulo(
            stringResource(
                R.string.licao_questao,
                estado.indiceQuestao + 1,
                estado.totalQuestoes
            )
        )
        Spacer(Modifier.height(Espacos.pequeno))
        Text(
            text = questao.enunciado,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(Espacos.medio))

        questao.alternativas.forEachIndexed { indice, alternativa ->
            val selecionada = estado.alternativaSelecionada == indice
            val revelaCorreta = estado.confirmada && alternativa.correta
            val corBorda = when {
                revelaCorreta -> cores.ouro
                estado.confirmada && selecionada -> cores.dividaBorda
                selecionada -> cores.ouro
                else -> cores.linha
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Espacos.minimo)
                    .border(1.dp, corBorda)
                    .clickable(enabled = !estado.confirmada) { aoSelecionar(indice) }
                    .padding(Espacos.medio),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alternativa.texto,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (revelaCorreta) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = stringResource(R.string.licao_acerto),
                        tint = cores.ouro,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        if (estado.erroSemSelecao) {
            Spacer(Modifier.height(Espacos.pequeno))
            Text(
                text = stringResource(R.string.licao_escolha),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (estado.confirmada) {
            Spacer(Modifier.height(Espacos.medio))
            Text(
                text = if (estado.acertouAtual) {
                    stringResource(R.string.licao_acerto)
                } else {
                    stringResource(R.string.licao_erro)
                },
                color = if (estado.acertouAtual) cores.ouroRotulo else cores.dividaBorda,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(Espacos.minimo))
            Text(
                text = questao.explicacao,
                color = cores.textoCorpo,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(Espacos.medio))
        Button(
            onClick = if (estado.confirmada) aoAvancar else aoConfirmar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when {
                    !estado.confirmada -> stringResource(R.string.licao_confirmar)
                    estado.ehUltimaQuestao -> stringResource(R.string.licao_concluir)
                    else -> stringResource(R.string.licao_proxima)
                }.uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
