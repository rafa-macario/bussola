package br.com.fiap.bussola.dados

import br.com.fiap.bussola.dados.remoto.BcbApi
import br.com.fiap.bussola.dados.remoto.dto.SerieDto
import br.com.fiap.bussola.dados.repositorio.IndicadoresRepositorioBcb
import br.com.fiap.bussola.dominio.Recurso
import br.com.fiap.bussola.dominio.modelo.SerieBcb
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.time.LocalDate


class IndicadoresRepositorioTest {

    private class ApiFalsa(
        private val respostas: Map<Int, List<SerieDto>> = emptyMap(),
        private val falharPara: Set<Int> = emptySet()
    ) : BcbApi {
        var chamadas = 0
            private set

        override suspend fun ultimosValores(
            serie: Int,
            quantidade: Int,
            formato: String
        ): List<SerieDto> {
            chamadas++
            if (serie in falharPara) throw IOException("falha simulada")
            return respostas[serie] ?: emptyList()
        }
    }

    private fun respostaPadrao() = mapOf(
        SerieBcb.SELIC_META.codigo to listOf(SerieDto("29/08/2026", "15.00")),
        SerieBcb.IPCA_MENSAL.codigo to listOf(SerieDto("01/08/2026", "0.26")),
        SerieBcb.SELIC_NO_MES.codigo to listOf(SerieDto("01/08/2026", "1.18")),
        SerieBcb.CARTAO_ROTATIVO.codigo to listOf(SerieDto("01/08/2026", "449.50"))
    )

    @Test
    fun `converte texto e data da API para o modelo de dominio`() = runTest {
        val repositorio = IndicadoresRepositorioBcb(
            api = ApiFalsa(respostaPadrao()),
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        val resultado = repositorio.carregar()

        assertTrue(resultado is Recurso.Sucesso)
        val painel = (resultado as Recurso.Sucesso).dado
        assertEquals(15.0, painel.selicMeta?.valorPercentual ?: 0.0, 0.001)
        assertEquals(LocalDate.of(2026, 8, 29), painel.selicMeta?.dataReferencia)
        assertEquals(449.5, painel.cartaoRotativo?.valorPercentual ?: 0.0, 0.001)
    }

    @Test
    fun `calcula quantas vezes o rotativo supera a Selic`() = runTest {
        val repositorio = IndicadoresRepositorioBcb(
            api = ApiFalsa(respostaPadrao()),
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        val painel = (repositorio.carregar() as Recurso.Sucesso).dado
        assertEquals(29.96, painel.rotativoSobreSelic ?: 0.0, 0.01)
    }

    @Test
    fun `falha de uma serie nao derruba as demais`() = runTest {
        val repositorio = IndicadoresRepositorioBcb(
            api = ApiFalsa(respostaPadrao(), falharPara = setOf(SerieBcb.IPCA_MENSAL.codigo)),
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        val painel = (repositorio.carregar() as Recurso.Sucesso).dado
        assertNull(painel.ipcaMensal)
        assertNotNull(painel.selicMeta)
    }

    @Test
    fun `devolve falha quando as series essenciais nao chegam`() = runTest {
        val repositorio = IndicadoresRepositorioBcb(
            api = ApiFalsa(
                respostaPadrao(),
                falharPara = setOf(
                    SerieBcb.SELIC_META.codigo,
                    SerieBcb.CARTAO_ROTATIVO.codigo
                )
            ),
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        assertTrue(repositorio.carregar() is Recurso.Falha)
    }

    @Test
    fun `usa cache dentro da janela de validade`() = runTest {
        val api = ApiFalsa(respostaPadrao())
        val repositorio = IndicadoresRepositorioBcb(
            api = api,
            dispatcher = UnconfinedTestDispatcher(testScheduler),
            agora = { 0L }
        )

        repositorio.carregar()
        repositorio.carregar()

        assertEquals(4, api.chamadas)
    }

    @Test
    fun `forcar atualizacao ignora o cache`() = runTest {
        val api = ApiFalsa(respostaPadrao())
        val repositorio = IndicadoresRepositorioBcb(
            api = api,
            dispatcher = UnconfinedTestDispatcher(testScheduler),
            agora = { 0L }
        )

        repositorio.carregar()
        repositorio.carregar(forcarAtualizacao = true)

        assertEquals(8, api.chamadas)
    }
}
