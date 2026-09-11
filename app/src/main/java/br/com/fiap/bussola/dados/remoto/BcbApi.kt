package br.com.fiap.bussola.dados.remoto

import br.com.fiap.bussola.dados.remoto.dto.SerieDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BcbApi {

    @GET("bcdata.sgs.{serie}/dados/ultimos/{quantidade}")
    suspend fun ultimosValores(
        @Path("serie") serie: Int,
        @Path("quantidade") quantidade: Int = 1,
        @Query("formato") formato: String = "json"
    ): List<SerieDto>

    companion object {
        const val URL_BASE = "https://api.bcb.gov.br/dados/serie/"
    }
}
