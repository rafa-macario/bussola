package br.com.fiap.bussola.ui.licao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.bussola.core.ui.aplicacao
import br.com.fiap.bussola.dados.repositorio.TrilhaRepositorio
import br.com.fiap.bussola.dominio.modelo.Modulo
import br.com.fiap.bussola.dominio.modelo.Questao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LicaoEstado(
    val modulo: Modulo? = null,
    val indiceQuestao: Int = 0,
    val alternativaSelecionada: Int? = null,
    val confirmada: Boolean = false,
    val acertos: Int = 0,
    val erroSemSelecao: Boolean = false,
    val finalizado: Boolean = false
) {
    val questaoAtual: Questao?
        get() = modulo?.questoes?.getOrNull(indiceQuestao)

    val totalQuestoes: Int get() = modulo?.questoes?.size ?: 0

    val acertouAtual: Boolean
        get() = confirmada && alternativaSelecionada == questaoAtual?.indiceCorreto

    val ehUltimaQuestao: Boolean get() = indiceQuestao == totalQuestoes - 1
}


class LicaoViewModel(
    moduloId: String,
    private val repositorio: TrilhaRepositorio
) : ViewModel() {

    private val _estado = MutableStateFlow(LicaoEstado(modulo = repositorio.modulo(moduloId)))
    val estado: StateFlow<LicaoEstado> = _estado.asStateFlow()

    fun selecionar(indice: Int) {
        if (_estado.value.confirmada) return
        _estado.update { it.copy(alternativaSelecionada = indice, erroSemSelecao = false) }
    }

    fun confirmar() {
        val atual = _estado.value
        if (atual.alternativaSelecionada == null) {
            _estado.update { it.copy(erroSemSelecao = true) }
            return
        }
        val acertou = atual.alternativaSelecionada == atual.questaoAtual?.indiceCorreto
        _estado.update {
            it.copy(confirmada = true, acertos = it.acertos + if (acertou) 1 else 0)
        }
    }

    fun avancar() {
        val atual = _estado.value
        if (!atual.confirmada) return

        if (atual.ehUltimaQuestao) {
            concluir()
            return
        }
        _estado.update {
            it.copy(
                indiceQuestao = it.indiceQuestao + 1,
                alternativaSelecionada = null,
                confirmada = false
            )
        }
    }

    private fun concluir() {
        val modulo = _estado.value.modulo ?: return
        viewModelScope.launch {
            repositorio.marcarConcluido(modulo.id)
            _estado.update { it.copy(finalizado = true) }
        }
    }

    companion object {
        fun fabrica(moduloId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LicaoViewModel(moduloId, aplicacao().container.trilhaRepositorio)
            }
        }
    }
}
