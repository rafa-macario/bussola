package br.com.fiap.bussola.ui.abertura

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.bussola.R
import br.com.fiap.bussola.core.ui.componente.Espacos
import br.com.fiap.bussola.core.ui.componente.FioLuneta
import br.com.fiap.bussola.core.ui.componente.Rotulo
import br.com.fiap.bussola.core.ui.tema.BussolaTema
import br.com.fiap.bussola.core.ui.tema.LocalCoresBussola

@Composable
fun AberturaTela(
    aoComecar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cores = LocalCoresBussola.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Espacos.secao),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FioLuneta()
        Spacer(Modifier.height(Espacos.grande))

        Text(
            text = stringResource(R.string.app_nome).uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayLarge.copy(letterSpacing = 8.sp)
        )

        Box(
            Modifier
                .padding(vertical = Espacos.medio)
                .width(44.dp)
                .height(1.dp)
                .background(cores.ouro)
        )

        Rotulo(stringResource(R.string.abertura_assinatura))
        Spacer(Modifier.height(Espacos.grande))

        Text(
            text = stringResource(R.string.abertura_texto),
            modifier = Modifier.fillMaxWidth(),
            color = cores.textoCorpo,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(Espacos.secao))

        Button(onClick = aoComecar) {
            Text(
                text = stringResource(R.string.abertura_botao).uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Preview(name = "Abertura claro", showBackground = true)
@Composable
private fun PreviaAberturaClaro() {
    BussolaTema(escuro = false) { AberturaTela(aoComecar = {}) }
}

@Preview(name = "Abertura escuro", showBackground = true)
@Composable
private fun PreviaAberturaEscuro() {
    BussolaTema(escuro = true) { AberturaTela(aoComecar = {}) }
}
