package br.com.fiap.bussola.dados.remoto.dto

import com.google.gson.annotations.SerializedName


data class SerieDto(
    @SerializedName("data") val data: String?,
    @SerializedName("valor") val valor: String?
)
