package br.com.fiap.bussola.dados.repositorio

import br.com.fiap.bussola.dados.local.PreferenciasLocais
import br.com.fiap.bussola.dados.local.dto.TransacaoPersistida
import br.com.fiap.bussola.dominio.modelo.TipoTransacao
import br.com.fiap.bussola.dominio.modelo.Transacao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

interface OrcamentoRepositorio {
    val transacoes: Flow<List<Transacao>>
    suspend fun adicionar(descricao: String, valor: Double, tipo: TipoTransacao)
    suspend fun remover(id: String)
}


class OrcamentoRepositorioLocal(
    private val preferencias: PreferenciasLocais,
    private val gson: Gson = Gson()
) : OrcamentoRepositorio {

    override val transacoes: Flow<List<Transacao>> =
        preferencias.transacoesJson.map { json -> desserializar(json) }

    override suspend fun adicionar(descricao: String, valor: Double, tipo: TipoTransacao) {
        require(descricao.isNotBlank()) { "A descricao nao pode ser vazia." }
        require(valor > 0) { "O valor precisa ser maior que zero." }

        val nova = Transacao(
            id = UUID.randomUUID().toString(),
            descricao = descricao.trim(),
            valor = valor,
            tipo = tipo,
            data = LocalDate.now()
        )
        salvar(atuais() + nova)
    }

    override suspend fun remover(id: String) {
        salvar(atuais().filterNot { it.id == id })
    }

    private suspend fun atuais(): List<Transacao> =
        desserializar(preferencias.transacoesJson.first())

    private suspend fun salvar(lista: List<Transacao>) {
        val persistidas = lista.map {
            TransacaoPersistida(
                id = it.id,
                descricao = it.descricao,
                valor = it.valor,
                tipo = it.tipo.name,
                data = it.data.toString()
            )
        }
        preferencias.salvarTransacoes(gson.toJson(persistidas))
    }

    private fun desserializar(json: String): List<Transacao> = runCatching {
        val tipo = object : TypeToken<List<TransacaoPersistida>>() {}.type
        val lista: List<TransacaoPersistida> = gson.fromJson(json, tipo) ?: emptyList()
        lista.mapNotNull { item ->
            runCatching {
                Transacao(
                    id = item.id,
                    descricao = item.descricao,
                    valor = item.valor,
                    tipo = TipoTransacao.valueOf(item.tipo),
                    data = LocalDate.parse(item.data)
                )
            }.getOrNull()
        }
    }.getOrDefault(emptyList())
}
