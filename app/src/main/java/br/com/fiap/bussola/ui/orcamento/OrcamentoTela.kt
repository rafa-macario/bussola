package br.com.fiap.bussola.ui.orcamento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.formato.Formatador
import br.com.fiap.bussola.core.ui.componente.CabecalhoTela
import br.com.fiap.bussola.core.ui.componente.Divisoria
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola
import br.com.fiap.bussola.dominio.modelo.ResumoOrcamento
import br.com.fiap.bussola.dominio.modelo.TipoTransacao
import br.com.fiap.bussola.dominio.modelo.Transacao

@Composable
fun OrcamentoTela(
    modifier: Modifier = Modifier,
    viewModel: OrcamentoViewModel = viewModel(factory = OrcamentoViewModel.Fabrica)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()

    Box(modifier.fillMaxSize()) {
        OrcamentoConteudo(
            estado = estado,
            aoAbrirFormulario = viewModel::abrirFormulario,
            aoRemover = viewModel::remover
        )

        if (formulario.visivel) {
            DialogoLancamento(
                formulario = formulario,
                aoDigitarDescricao = viewModel::aoDigitarDescricao,
                aoDigitarValor = viewModel::aoDigitarValor,
                aoTrocarTipo = viewModel::aoTrocarTipo,
                aoSalvar = viewModel::salvar,
                aoCancelar = viewModel::fecharFormulario
            )
        }
    }
}

@Composable
private fun OrcamentoConteudo(
    estado: OrcamentoEstado,
    aoAbrirFormulario: () -> Unit,
    aoRemover: (String) -> Unit
) {
    val cores = LocalCoresBussola.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Espacos.medio, vertical = Espacos.pequeno)
    ) {
        item {
            CabecalhoTela(stringResource(R.string.conta_titulo))
            Spacer(Modifier.height(Espacos.grande))
            CartaoSaldo(estado.resumo)
            if (estado.resumo.saldo > 0) {
                Spacer(Modifier.height(Espacos.medio))
                Text(
                    text = stringResource(
                        R.string.conta_reserva,
                        Formatador.moeda(estado.resumo.saldo, comCentavos = false),
                        Formatador.moeda(estado.resumo.saldo * MESES_DE_RESERVA, comCentavos = false)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    color = cores.textoCorpo,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(Espacos.grande))
        }

        if (estado.transacoes.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.conta_vazio),
                    modifier = Modifier.fillMaxWidth(),
                    color = cores.textoCorpo,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(estado.transacoes, key = { it.id }) { transacao ->
                LinhaTransacao(transacao = transacao, aoRemover = { aoRemover(transacao.id) })
                Divisoria()
            }
        }

        item {
            Spacer(Modifier.height(Espacos.grande))
            OutlinedButton(
                onClick = aoAbrirFormulario,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.conta_registrar).uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(Modifier.height(Espacos.grande))
        }
    }
}

private const val MESES_DE_RESERVA = 6

@Composable
private fun CartaoSaldo(resumo: ResumoOrcamento) {
    val cores = LocalCoresBussola.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = Espacos.grande, horizontal = Espacos.medio),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Rotulo(stringResource(R.string.conta_saldo), cor = cores.ouro)
        Spacer(Modifier.height(Espacos.pequeno))
        Text(
            text = Formatador.moeda(resumo.saldo, comCentavos = false),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(Modifier.height(Espacos.pequeno))
        Text(
            text = stringResource(
                R.string.conta_resumo,
                Formatador.moeda(resumo.entradas, comCentavos = false),
                Formatador.moeda(resumo.saidas, comCentavos = false)
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun LinhaTransacao(transacao: Transacao, aoRemover: () -> Unit) {
    val cores = LocalCoresBussola.current
    val entrada = transacao.tipo == TipoTransacao.ENTRADA
    val sinal = if (entrada) "+ " else "− "
    val cor = if (entrada) cores.ouroRotulo else cores.dividaBorda

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Espacos.pequeno),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = transacao.descricao,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = Formatador.data(transacao.data),
                color = cores.textoLegenda,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Text(
            text = sinal + Formatador.moeda(transacao.valor),
            color = cor,
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            onClick = aoRemover,
            modifier = Modifier.size(Espacos.alvoToque)
        ) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = stringResource(R.string.conta_remover),
                tint = cores.textoLegenda,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DialogoLancamento(
    formulario: FormularioLancamento,
    aoDigitarDescricao: (String) -> Unit,
    aoDigitarValor: (String) -> Unit,
    aoTrocarTipo: (TipoTransacao) -> Unit,
    aoSalvar: () -> Unit,
    aoCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text(stringResource(R.string.conta_registrar)) },
        text = {
            Column {
                OutlinedTextField(
                    value = formulario.descricao,
                    onValueChange = aoDigitarDescricao,
                    label = { Text(stringResource(R.string.conta_descricao)) },
                    singleLine = true,
                    isError = formulario.erroDescricao,
                    supportingText = if (formulario.erroDescricao) {
                        { Text(stringResource(R.string.conta_erro_descricao)) }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(Espacos.medio))
                OutlinedTextField(
                    value = formulario.valor,
                    onValueChange = aoDigitarValor,
                    label = { Text(stringResource(R.string.conta_valor)) },
                    singleLine = true,
                    isError = formulario.erroValor,
                    supportingText = if (formulario.erroValor) {
                        { Text(stringResource(R.string.conta_erro_valor)) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(Espacos.medio))
                Row(horizontalArrangement = Arrangement.spacedBy(Espacos.pequeno)) {
                    FilterChip(
                        selected = formulario.tipo == TipoTransacao.ENTRADA,
                        onClick = { aoTrocarTipo(TipoTransacao.ENTRADA) },
                        label = { Text(stringResource(R.string.conta_entrada)) }
                    )
                    FilterChip(
                        selected = formulario.tipo == TipoTransacao.SAIDA,
                        onClick = { aoTrocarTipo(TipoTransacao.SAIDA) },
                        label = { Text(stringResource(R.string.conta_saida)) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = aoSalvar) { Text(stringResource(R.string.conta_salvar)) }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar) { Text(stringResource(R.string.conta_cancelar)) }
        }
    )
}
