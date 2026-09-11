package br.com.fiap.bussola.dominio


sealed interface Recurso<out T> {
    data class Sucesso<T>(val dado: T) : Recurso<T>
    data class Falha(val causa: Throwable? = null) : Recurso<Nothing>
}
