package br.com.fiap.bussola.dominio.modelo

data class Alternativa(val texto: String, val correta: Boolean)

data class Questao(
    val enunciado: String,
    val alternativas: List<Alternativa>,
    val explicacao: String
) {
    val indiceCorreto: Int get() = alternativas.indexOfFirst { it.correta }
}

data class Modulo(
    val id: String,
    val ordem: Int,
    val numeroRomano: String,
    val titulo: String,
    val duracaoMinutos: Int,
    val paragrafos: List<String>,
    val destaque: String,
    val questoes: List<Questao>
)

data class ModuloComProgresso(
    val modulo: Modulo,
    val concluido: Boolean,
    val liberado: Boolean
)
