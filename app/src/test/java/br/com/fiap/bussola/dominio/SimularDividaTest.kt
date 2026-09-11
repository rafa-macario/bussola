package br.com.fiap.bussola.dominio

import br.com.fiap.bussola.dominio.uso.SimularDivida
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs


class SimularDividaTest {

    private val simular = SimularDivida()

    @Test
    fun `converte taxa anual em mensal equivalente`() {
        val resultado = simular(valor = 1000.0, meses = 1, taxaRotativoAnual = 449.5)

        // 449,5% ao ano equivale a aproximadamente 15,26% ao mes.
        assertTrue(abs(resultado.taxaRotativoMensal - 0.1526) < 0.001)
    }

    @Test
    fun `divida no rotativo multiplica por mais de cinco em doze meses`() {
        val resultado = simular(valor = 800.0, meses = 12, taxaRotativoAnual = 449.5)

        // Em doze meses o montante equivale ao valor inicial vezes (1 + taxa anual).
        assertTrue(abs(resultado.montanteRotativo - 4396.0) < 5.0)
    }

    @Test
    fun `parcelamento custa menos que o rotativo no mesmo prazo`() {
        val resultado = simular(valor = 800.0, meses = 12, taxaRotativoAnual = 449.5)

        assertTrue(resultado.totalAlternativa < resultado.montanteRotativo)
        assertTrue(resultado.economia > 0.0)
    }

    @Test
    fun `parcela pelo sistema Price fecha com o total dividido pelo prazo`() {
        val resultado = simular(
            valor = 800.0,
            meses = 12,
            taxaRotativoAnual = 449.5,
            taxaAlternativaMensal = 0.05
        )

        assertEquals(resultado.totalAlternativa / 12, resultado.parcelaAlternativa, 0.01)
        assertTrue(abs(resultado.parcelaAlternativa - 90.26) < 0.5)
    }

    @Test
    fun `taxa alternativa zerada divide o valor em parcelas iguais`() {
        val resultado = simular(
            valor = 1200.0,
            meses = 12,
            taxaRotativoAnual = 100.0,
            taxaAlternativaMensal = 0.0
        )

        assertEquals(100.0, resultado.parcelaAlternativa, 0.001)
        assertEquals(1200.0, resultado.totalAlternativa, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `recusa valor zerado`() {
        simular(valor = 0.0, meses = 12, taxaRotativoAnual = 449.5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `recusa prazo fora da faixa`() {
        simular(valor = 100.0, meses = 0, taxaRotativoAnual = 449.5)
    }
}
