package br.com.fiap.bussola.dominio.modelo

import java.time.LocalDate

enum class TipoTransacao { ENTRADA, SAIDA }


data class Transacao(
    val id: String,
    val descricao: String,
    val valor: Double,
    val tipo: TipoTransacao,
    val data: LocalDate
)


data class ResumoOrcamento(
    val entradas: Double,
    val saidas: Double
) {
    val saldo: Double get() = entradas - saidas

    companion object {
        fun de(transacoes: List<Transacao>): ResumoOrcamento {
            val entradas = transacoes.filter { it.tipo == TipoTransacao.ENTRADA }.sumOf { it.valor }
            val saidas = transacoes.filter { it.tipo == TipoTransacao.SAIDA }.sumOf { it.valor }
            return ResumoOrcamento(entradas, saidas)
        }
    }
}
