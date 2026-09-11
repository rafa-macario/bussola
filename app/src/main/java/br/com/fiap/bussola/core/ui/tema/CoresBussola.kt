package br.com.fiap.bussola.core.ui.tema

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
data class CoresBussola(
    val ouro: Color,
    val ouroRotulo: Color,
    val linha: Color,
    val textoCorpo: Color,
    val textoLegenda: Color,
    val bloqueado: Color,
    val dividaFundo: Color,
    val dividaBorda: Color,
    val dividaTexto: Color,
    val dividaRotulo: Color,
    val superficieCartao: Color
)

val CoresBussolaEscuro = CoresBussola(
    ouro = OuroClaro,
    ouroRotulo = OuroClaro,
    linha = VerdeLinha,
    textoCorpo = VerdeTexto,
    textoLegenda = VerdeApagado,
    bloqueado = VerdeBloqueado,
    dividaFundo = BorgonhaFundoEscuro,
    dividaBorda = BorgonhaBorda,
    dividaTexto = BorgonhaTextoClaro,
    dividaRotulo = BorgonhaRotuloClaro,
    superficieCartao = Color.Transparent
)

val CoresBussolaClaro = CoresBussola(
    ouro = OuroLinha,
    ouroRotulo = OuroRotulo,
    linha = LinhaClara,
    textoCorpo = VerdeCorpo,
    textoLegenda = VerdeLegenda,
    bloqueado = BloqueadoClaro,
    dividaFundo = BorgonhaFundoClaro,
    dividaBorda = BorgonhaBorda,
    dividaTexto = BorgonhaTextoEscuro,
    dividaRotulo = BorgonhaRotuloEscuro,
    superficieCartao = BrancoCarta
)

val LocalCoresBussola = staticCompositionLocalOf<CoresBussola> {
    error("CoresBussola so existe dentro de BussolaTema.")
}
