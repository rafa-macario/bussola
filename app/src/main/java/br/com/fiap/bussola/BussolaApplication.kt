package br.com.fiap.bussola

import android.app.Application
import br.com.fiap.bussola.di.ContainerApp
import br.com.fiap.bussola.di.ContainerAppPadrao


class BussolaApplication : Application() {

    lateinit var container: ContainerApp
        private set

    override fun onCreate() {
        super.onCreate()
        container = ContainerAppPadrao(this)
    }
}
