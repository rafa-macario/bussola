package br.com.fiap.bussola.dominio

import br.com.fiap.bussola.dominio.modelo.ResumoOrcamento
import br.com.fiap.bussola.dominio.modelo.TipoTransacao
import br.com.fiap.bussola.dominio.modelo.Transacao
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ResumoOrcamentoTest {

    private fun transacao(valor: Double, tipo: TipoTransacao) = Transacao(
        id = valor.toString() + tipo.name,
        descricao = "teste",
        valor = valor,
        tipo = tipo,
        data = LocalDate.of(2026, 8, 29)
    )

    @Test
    fun `soma entradas e saidas separadamente`() {
        val resumo = ResumoOrcamento.de(
            listOf(
                transacao(1518.0, TipoTransacao.ENTRADA),
                transacao(600.0, TipoTransacao.SAIDA),
                transacao(420.0, TipoTransacao.SAIDA),
                transacao(150.0, TipoTransacao.SAIDA)
            )
        )

        assertEquals(1518.0, resumo.entradas, 0.001)
        assertEquals(1170.0, resumo.saidas, 0.001)
        assertEquals(348.0, resumo.saldo, 0.001)
    }

    @Test
    fun `lista vazia resulta em saldo zero`() {
        val resumo = ResumoOrcamento.de(emptyList())
        assertEquals(0.0, resumo.saldo, 0.001)
    }

    @Test
    fun `saldo negativo quando as saidas superam as entradas`() {
        val resumo = ResumoOrcamento.de(
            listOf(
                transacao(500.0, TipoTransacao.ENTRADA),
                transacao(800.0, TipoTransacao.SAIDA)
            )
        )
        assertEquals(-300.0, resumo.saldo, 0.001)
    }
}
