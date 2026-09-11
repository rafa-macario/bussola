package br.com.fiap.bussola.dados.local.dto

data class TransacaoPersistida(
    val id: String,
    val descricao: String,
    val valor: Double,
    val tipo: String,
    val data: String
)
