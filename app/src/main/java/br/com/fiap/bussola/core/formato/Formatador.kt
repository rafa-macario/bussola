package br.com.fiap.bussola.core.formato

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


object Formatador {

    private val BRASIL = Locale("pt", "BR")
    private val DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy", BRASIL)

    fun moeda(valor: Double, comCentavos: Boolean = true): String {
        val formato = NumberFormat.getCurrencyInstance(BRASIL).apply {
            minimumFractionDigits = if (comCentavos) 2 else 0
            maximumFractionDigits = if (comCentavos) 2 else 0
        }
        return formato.format(valor)
    }

    fun percentual(valor: Double, casas: Int = 2): String =
        String.format(BRASIL, "%,.${casas}f%%", valor)

    fun numero(valor: Double, casas: Int = 1): String =
        String.format(BRASIL, "%,.${casas}f", valor)

    fun data(data: LocalDate?): String = data?.format(DATA).orEmpty()
}
