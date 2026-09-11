package br.com.fiap.bussola.ui.navegacao


sealed class Rota(val caminho: String) {

    data object Abertura : Rota("abertura")
    data object Painel : Rota("painel")
    data object Simulador : Rota("simulador")
    data object Orcamento : Rota("orcamento")
    data object Trilha : Rota("trilha")

    data object Licao : Rota("licao/{moduloId}") {
        const val ARG_MODULO = "moduloId"
        fun comId(moduloId: String): String = "licao/$moduloId"
    }

    companion object {
        val comBarraInferior = setOf(
            Painel.caminho, Simulador.caminho, Orcamento.caminho, Trilha.caminho
        )
    }
}
