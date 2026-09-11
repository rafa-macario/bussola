package br.com.fiap.bussola.ui.trilha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.bussola.core.ui.aplicacao
import br.com.fiap.bussola.dados.repositorio.TrilhaRepositorio
import br.com.fiap.bussola.dominio.modelo.ModuloComProgresso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TrilhaEstado(
    val modulos: List<ModuloComProgresso> = emptyList()
) {
    val concluidos: Int get() = modulos.count { it.concluido }
    val total: Int get() = modulos.size
}

class TrilhaViewModel(repositorio: TrilhaRepositorio) : ViewModel() {

    val estado: StateFlow<TrilhaEstado> = repositorio.modulos
        .map { TrilhaEstado(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), TrilhaEstado())

    companion object {
        val Fabrica: ViewModelProvider.Factory = viewModelFactory {
            initializer { TrilhaViewModel(aplicacao().container.trilhaRepositorio) }
        }
    }
}
