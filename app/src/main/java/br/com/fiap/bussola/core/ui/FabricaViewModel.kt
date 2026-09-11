package br.com.fiap.bussola.core.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.fiap.bussola.BussolaApplication


fun CreationExtras.aplicacao(): BussolaApplication =
    checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) {
        "A aplicacao precisa estar disponivel nas CreationExtras."
    } as BussolaApplication
