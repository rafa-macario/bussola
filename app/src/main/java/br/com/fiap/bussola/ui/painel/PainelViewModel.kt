package br.com.fiap.bussola.ui.painel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.bussola.core.ui.aplicacao
import br.com.fiap.bussola.dados.repositorio.IndicadoresRepositorio
import br.com.fiap.bussola.dominio.Recurso
import br.com.fiap.bussola.dominio.modelo.PainelIndicadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class PainelEstado(
    val carregando: Boolean = true,
    val indicadores: PainelIndicadores? = null,
    val houveFalha: Boolean = false
)

class PainelViewModel(
    private val repositorio: IndicadoresRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow(PainelEstado())
    val estado: StateFlow<PainelEstado> = _estado.asStateFlow()

    init {
        carregar()
    }

    fun carregar(forcarAtualizacao: Boolean = false) {
        viewModelScope.launch {
            _estado.update { it.copy(carregando = true, houveFalha = false) }
            when (val resultado = repositorio.carregar(forcarAtualizacao)) {
                is Recurso.Sucesso -> _estado.update {
                    it.copy(carregando = false, indicadores = resultado.dado, houveFalha = false)
                }

                is Recurso.Falha -> _estado.update {
                    it.copy(carregando = false, houveFalha = true)
                }
            }
        }
    }

    companion object {
        val Fabrica: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PainelViewModel(aplicacao().container.indicadoresRepositorio)
            }
        }
    }
}
