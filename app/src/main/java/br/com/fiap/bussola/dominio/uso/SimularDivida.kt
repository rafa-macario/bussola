package br.com.fiap.bussola.dominio.uso

import br.com.fiap.bussola.dominio.modelo.ResultadoSimulacao
import kotlin.math.pow


class SimularDivida {

    operator fun invoke(
        valor: Double,
        meses: Int,
        taxaRotativoAnual: Double,
        taxaAlternativaMensal: Double = TAXA_ALTERNATIVA_PADRAO
    ): ResultadoSimulacao {
        require(valor > 0) { "O valor da divida precisa ser maior que zero." }
        require(meses in 1..MAX_MESES) { "O prazo precisa estar entre 1 e $MAX_MESES meses." }
        require(taxaRotativoAnual >= 0) { "A taxa anual nao pode ser negativa." }

        val mensalRotativo = aoMes(taxaRotativoAnual)
        val montante = valor * (1 + mensalRotativo).pow(meses)
        val parcela = parcelaPrice(valor, taxaAlternativaMensal, meses)

        return ResultadoSimulacao(
            valorInicial = valor,
            meses = meses,
            taxaRotativoMensal = mensalRotativo,
            montanteRotativo = montante,
            taxaAlternativaMensal = taxaAlternativaMensal,
            parcelaAlternativa = parcela,
            totalAlternativa = parcela * meses
        )
    }

    private fun aoMes(taxaAnualPercentual: Double): Double =
        (1 + taxaAnualPercentual / 100.0).pow(1.0 / 12.0) - 1

    private fun parcelaPrice(valor: Double, taxaMensal: Double, meses: Int): Double {
        if (taxaMensal <= 0.0) return valor / meses
        val fator = (1 + taxaMensal).pow(-meses)
        return valor * taxaMensal / (1 - fator)
    }

    companion object {
        const val TAXA_ALTERNATIVA_PADRAO = 0.05
        const val MAX_MESES = 24
    }
}
