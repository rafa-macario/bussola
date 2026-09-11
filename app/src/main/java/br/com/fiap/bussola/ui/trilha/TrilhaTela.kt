package br.com.fiap.bussola.ui.trilha

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.ui.componente.CabecalhoTela
import br.com.fiap.bussola.core.ui.componente.Divisoria
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola
import br.com.fiap.bussola.dominio.modelo.ModuloComProgresso

@Composable
fun TrilhaTela(
    aoAbrirModulo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrilhaViewModel = viewModel(factory = TrilhaViewModel.Fabrica)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val cores = LocalCoresBussola.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Espacos.medio, vertical = Espacos.pequeno)
    ) {
        item {
            CabecalhoTela(stringResource(R.string.trilha_titulo))
            Spacer(Modifier.height(Espacos.pequeno))
            Text(
                text = stringResource(
                    R.string.trilha_progresso,
                    estado.concluidos.toString(),
                    estado.total.toString()
                ),
                modifier = Modifier.fillMaxWidth(),
                color = cores.textoLegenda,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Espacos.grande))
        }

        items(estado.modulos, key = { it.modulo.id }) { item ->
            LinhaModulo(item = item, aoAbrir = { aoAbrirModulo(item.modulo.id) })
            if (!item.concluido) Divisoria()
        }

        item { Spacer(Modifier.height(Espacos.grande)) }
    }
}

@Composable
private fun LinhaModulo(item: ModuloComProgresso, aoAbrir: () -> Unit) {
    val cores = LocalCoresBussola.current
    val emAndamento = item.liberado && !item.concluido

    val situacao = when {
        item.concluido -> stringResource(R.string.trilha_concluido)
        item.liberado -> stringResource(R.string.trilha_continuar)
        else -> stringResource(R.string.trilha_bloqueado)
    }

    val corNumero = if (item.liberado) cores.ouroRotulo else cores.bloqueado
    val corTitulo = if (item.liberado) {
        MaterialTheme.colorScheme.onBackground
    } else {
        cores.textoLegenda
    }

    val base = Modifier
        .fillMaxWidth()
        .then(if (item.liberado) Modifier.clickable(onClick = aoAbrir) else Modifier)
        .then(if (emAndamento) Modifier.border(1.dp, cores.ouro) else Modifier)
        .padding(Espacos.pequeno)
        .defaultMinSize(minHeight = Espacos.alvoToque)

    Row(
        modifier = base,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.modulo.numeroRomano,
            modifier = Modifier.width(28.dp),
            color = corNumero,
            style = MaterialTheme.typography.titleMedium
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = item.modulo.titulo,
                color = corTitulo,
                style = MaterialTheme.typography.bodyLarge
            )
            Row {
                Rotulo(situacao, cor = if (emAndamento) cores.ouroRotulo else cores.textoLegenda)
                Text(
                    text = "  ·  " + stringResource(
                        R.string.trilha_duracao,
                        item.modulo.duracaoMinutos
                    ),
                    color = cores.textoLegenda,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Icon(
            imageVector = when {
                item.concluido -> Icons.Outlined.Check
                item.liberado -> Icons.AutoMirrored.Outlined.ArrowForward
                else -> Icons.Outlined.Lock
            },
            contentDescription = situacao,
            tint = if (item.liberado) cores.ouro else cores.bloqueado,
            modifier = Modifier.size(20.dp)
        )
    }
}

