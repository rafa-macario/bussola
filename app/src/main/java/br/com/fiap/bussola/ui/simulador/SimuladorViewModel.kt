package br.com.fiap.bussola.ui.simulador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.bussola.core.ui.aplicacao
import br.com.fiap.bussola.dados.repositorio.IndicadoresRepositorio
import br.com.fiap.bussola.dominio.Recurso
import br.com.fiap.bussola.dominio.modelo.ResultadoSimulacao
import br.com.fiap.bussola.dominio.uso.SimularDivida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SimuladorEstado(
    val valorDigitado: String = "1000",
    val meses: Int = 12,
    val taxaRotativoAnual: Double? = null,
    val resultado: ResultadoSimulacao? = null,
    val valorInvalido: Boolean = false,
    val carregandoTaxa: Boolean = true
)


class SimuladorViewModel(
    private val repositorio: IndicadoresRepositorio,
    private val simular: SimularDivida
) : ViewModel() {

    private val _estado = MutableStateFlow(SimuladorEstado())
    val estado: StateFlow<SimuladorEstado> = _estado.asStateFlow()

    init {
        carregarTaxa()
    }

    private fun carregarTaxa() {
        viewModelScope.launch {
            val taxa = when (val resultado = repositorio.carregar()) {
                is Recurso.Sucesso -> resultado.dado.cartaoRotativo?.valorPercentual
                is Recurso.Falha -> null
            }
            _estado.update { it.copy(taxaRotativoAnual = taxa, carregandoTaxa = false) }
            recalcular()
        }
    }

    fun aoDigitarValor(texto: String) {
        val limpo = texto.filter { it.isDigit() || it == ',' || it == '.' }
        _estado.update { it.copy(valorDigitado = limpo) }
        recalcular()
    }

    fun aoMudarPrazo(meses: Int) {
        _estado.update { it.copy(meses = meses.coerceIn(1, SimularDivida.MAX_MESES)) }
        recalcular()
    }

    private fun recalcular() {
        val atual = _estado.value
        val valor = atual.valorDigitado.replace(".", "").replace(",", ".").toDoubleOrNull()
        val taxa = atual.taxaRotativoAnual

        if (valor == null || valor <= 0.0) {
            _estado.update { it.copy(resultado = null, valorInvalido = it.valorDigitado.isNotBlank()) }
            return
        }
        if (taxa == null) {
            _estado.update { it.copy(resultado = null, valorInvalido = false) }
            return
        }

        val resultado = runCatching {
            simular(valor = valor, meses = atual.meses, taxaRotativoAnual = taxa)
        }.getOrNull()

        _estado.update { it.copy(resultado = resultado, valorInvalido = false) }
    }

    companion object {
        val Fabrica: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = aplicacao().container
                SimuladorViewModel(container.indicadoresRepositorio, container.simularDivida)
            }
        }
    }
}
