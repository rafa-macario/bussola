package br.com.fiap.bussola.dominio.modelo


data class ResultadoSimulacao(
    val valorInicial: Double,
    val meses: Int,
    val taxaRotativoMensal: Double,
    val montanteRotativo: Double,
    val taxaAlternativaMensal: Double,
    val parcelaAlternativa: Double,
    val totalAlternativa: Double
) {
    val economia: Double get() = montanteRotativo - totalAlternativa
}
