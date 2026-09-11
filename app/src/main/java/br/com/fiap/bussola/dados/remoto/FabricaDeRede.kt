package br.com.fiap.bussola.dados.remoto

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object FabricaDeRede {

    fun criarBcbApi(): BcbApi =
        Retrofit.Builder()
            .baseUrl(BcbApi.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BcbApi::class.java)
}
