package br.com.fiap.bussola.dados.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.armazenamento: DataStore<Preferences> by preferencesDataStore(name = "bussola")

class PreferenciasLocais(private val contexto: Context) {

    val transacoesJson: Flow<String> =
        contexto.armazenamento.data.map { it[CHAVE_TRANSACOES] ?: "[]" }

    val modulosConcluidos: Flow<Set<String>> =
        contexto.armazenamento.data.map { it[CHAVE_MODULOS] ?: emptySet() }

    suspend fun salvarTransacoes(json: String) {
        contexto.armazenamento.edit { it[CHAVE_TRANSACOES] = json }
    }

    suspend fun salvarModulosConcluidos(ids: Set<String>) {
        contexto.armazenamento.edit { it[CHAVE_MODULOS] = ids }
    }

    private companion object {
        val CHAVE_TRANSACOES = stringPreferencesKey("transacoes")
        val CHAVE_MODULOS = stringSetPreferencesKey("modulos_concluidos")
    }
}
