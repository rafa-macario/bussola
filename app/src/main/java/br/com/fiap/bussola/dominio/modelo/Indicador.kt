package br.com.fiap.bussola.dominio.modelo

import java.time.LocalDate


enum class SerieBcb(val codigo: Int) {
    SELIC_META(432),
    IPCA_MENSAL(433),
    SELIC_NO_MES(4390),
    CARTAO_ROTATIVO(20749)
}

data class Indicador(
    val serie: SerieBcb,
    val valorPercentual: Double,
    val dataReferencia: LocalDate?
)

data class PainelIndicadores(
    val selicMeta: Indicador?,
    val ipcaMensal: Indicador?,
    val selicNoMes: Indicador?,
    val cartaoRotativo: Indicador?
) {
    val rotativoSobreSelic: Double?
        get() {
            val rotativo = cartaoRotativo?.valorPercentual ?: return null
            val selic = selicMeta?.valorPercentual ?: return null
            if (selic <= 0.0) return null
            return rotativo / selic
        }
}
