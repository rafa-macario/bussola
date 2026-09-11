package br.com.fiap.bussola.dados.repositorio

import br.com.fiap.bussola.dados.remoto.BcbApi
import br.com.fiap.bussola.dominio.Recurso
import br.com.fiap.bussola.dominio.modelo.Indicador
import br.com.fiap.bussola.dominio.modelo.PainelIndicadores
import br.com.fiap.bussola.dominio.modelo.SerieBcb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface IndicadoresRepositorio {
    suspend fun carregar(forcarAtualizacao: Boolean = false): Recurso<PainelIndicadores>
}


class IndicadoresRepositorioBcb(
    private val api: BcbApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val agora: () -> Long = System::currentTimeMillis
) : IndicadoresRepositorio {

    private var cache: PainelIndicadores? = null
    private var cacheEm: Long = 0L

    override suspend fun carregar(forcarAtualizacao: Boolean): Recurso<PainelIndicadores> {
        val emCache = cache
        if (!forcarAtualizacao && emCache != null && agora() - cacheEm < VALIDADE_CACHE_MS) {
            return Recurso.Sucesso(emCache)
        }

        return withContext(dispatcher) {
            runCatching {
                coroutineScope {
                    val selic = async { buscar(SerieBcb.SELIC_META) }
                    val ipca = async { buscar(SerieBcb.IPCA_MENSAL) }
                    val selicMes = async { buscar(SerieBcb.SELIC_NO_MES) }
                    val rotativo = async { buscar(SerieBcb.CARTAO_ROTATIVO) }
                    PainelIndicadores(
                        selicMeta = selic.await(),
                        ipcaMensal = ipca.await(),
                        selicNoMes = selicMes.await(),
                        cartaoRotativo = rotativo.await()
                    )
                }
            }.fold(
                onSuccess = { painel ->
                    if (painel.selicMeta == null && painel.cartaoRotativo == null) {
                        Recurso.Falha()
                    } else {
                        cache = painel
                        cacheEm = agora()
                        Recurso.Sucesso(painel)
                    }
                },
                onFailure = { erro -> Recurso.Falha(erro) }
            )
        }
    }

    private suspend fun buscar(serie: SerieBcb): Indicador? = runCatching {
        val resposta = api.ultimosValores(serie = serie.codigo, quantidade = 1)
        val ultimo = resposta.lastOrNull() ?: return null
        val valor = ultimo.valor?.trim()?.replace(",", ".")?.toDoubleOrNull() ?: return null
        Indicador(
            serie = serie,
            valorPercentual = valor,
            dataReferencia = converterData(ultimo.data)
        )
    }.getOrNull()

    private fun converterData(texto: String?): LocalDate? = runCatching {
        texto?.let { LocalDate.parse(it, FORMATO_BCB) }
    }.getOrNull()

    private companion object {
        val FORMATO_BCB: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        const val VALIDADE_CACHE_MS = 15 * 60 * 1000L
    }
}
