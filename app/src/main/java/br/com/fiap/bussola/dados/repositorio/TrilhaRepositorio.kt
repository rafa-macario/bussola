package br.com.fiap.bussola.dados.repositorio

import br.com.fiap.bussola.dados.local.ConteudoTrilha
import br.com.fiap.bussola.dados.local.PreferenciasLocais
import br.com.fiap.bussola.dominio.modelo.Modulo
import br.com.fiap.bussola.dominio.modelo.ModuloComProgresso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface TrilhaRepositorio {
    val modulos: Flow<List<ModuloComProgresso>>
    fun modulo(id: String): Modulo?
    suspend fun marcarConcluido(id: String)
}


class TrilhaRepositorioLocal(
    private val preferencias: PreferenciasLocais
) : TrilhaRepositorio {

    override val modulos: Flow<List<ModuloComProgresso>> =
        preferencias.modulosConcluidos.map { concluidos ->
            val ordenados = ConteudoTrilha.modulos.sortedBy { it.ordem }
            ordenados.mapIndexed { indice, modulo ->
                val anterior = ordenados.getOrNull(indice - 1)
                ModuloComProgresso(
                    modulo = modulo,
                    concluido = modulo.id in concluidos,
                    liberado = anterior == null || anterior.id in concluidos
                )
            }
        }

    override fun modulo(id: String): Modulo? =
        ConteudoTrilha.modulos.firstOrNull { it.id == id }

    override suspend fun marcarConcluido(id: String) {
        val atuais = preferencias.modulosConcluidos.first()
        preferencias.salvarModulosConcluidos(atuais + id)
    }
}
