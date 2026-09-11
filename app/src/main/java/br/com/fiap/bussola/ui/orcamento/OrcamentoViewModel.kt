package br.com.fiap.bussola.ui.orcamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.fiap.bussola.core.ui.aplicacao
import br.com.fiap.bussola.dados.repositorio.OrcamentoRepositorio
import br.com.fiap.bussola.dominio.modelo.ResumoOrcamento
import br.com.fiap.bussola.dominio.modelo.TipoTransacao
import br.com.fiap.bussola.dominio.modelo.Transacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrcamentoEstado(
    val transacoes: List<Transacao> = emptyList(),
    val resumo: ResumoOrcamento = ResumoOrcamento(0.0, 0.0)
)


data class FormularioLancamento(
    val visivel: Boolean = false,
    val descricao: String = "",
    val valor: String = "",
    val tipo: TipoTransacao = TipoTransacao.SAIDA,
    val erroDescricao: Boolean = false,
    val erroValor: Boolean = false
)

class OrcamentoViewModel(
    private val repositorio: OrcamentoRepositorio
) : ViewModel() {


    val estado: StateFlow<OrcamentoEstado> = repositorio.transacoes
        .map { lista ->
            OrcamentoEstado(
                transacoes = lista.sortedByDescending { it.data },
                resumo = ResumoOrcamento.de(lista)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TEMPO_ASSINATURA_MS),
            initialValue = OrcamentoEstado()
        )

    private val _formulario = MutableStateFlow(FormularioLancamento())
    val formulario: StateFlow<FormularioLancamento> = _formulario.asStateFlow()

    fun abrirFormulario() = _formulario.update { FormularioLancamento(visivel = true) }
    fun fecharFormulario() = _formulario.update { FormularioLancamento(visivel = false) }

    fun aoDigitarDescricao(texto: String) =
        _formulario.update { it.copy(descricao = texto, erroDescricao = false) }

    fun aoDigitarValor(texto: String) = _formulario.update {
        it.copy(valor = texto.filter { c -> c.isDigit() || c == ',' || c == '.' }, erroValor = false)
    }

    fun aoTrocarTipo(tipo: TipoTransacao) = _formulario.update { it.copy(tipo = tipo) }

    fun salvar() {
        val atual = _formulario.value
        val valor = atual.valor.replace(".", "").replace(",", ".").toDoubleOrNull()
        val descricaoInvalida = atual.descricao.isBlank()
        val valorInvalido = valor == null || valor <= 0.0

        if (descricaoInvalida || valorInvalido) {
            _formulario.update {
                it.copy(erroDescricao = descricaoInvalida, erroValor = valorInvalido)
            }
            return
        }

        viewModelScope.launch {
            repositorio.adicionar(atual.descricao, requireNotNull(valor), atual.tipo)
            fecharFormulario()
        }
    }

    fun remover(id: String) {
        viewModelScope.launch { repositorio.remover(id) }
    }

    companion object {
        private const val TEMPO_ASSINATURA_MS = 5_000L

        val Fabrica: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OrcamentoViewModel(aplicacao().container.orcamentoRepositorio)
            }
        }
    }
}
