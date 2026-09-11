package br.com.fiap.bussola.di

import android.content.Context
import br.com.fiap.bussola.dados.local.PreferenciasLocais
import br.com.fiap.bussola.dados.remoto.FabricaDeRede
import br.com.fiap.bussola.dados.repositorio.IndicadoresRepositorio
import br.com.fiap.bussola.dados.repositorio.IndicadoresRepositorioBcb
import br.com.fiap.bussola.dados.repositorio.OrcamentoRepositorio
import br.com.fiap.bussola.dados.repositorio.OrcamentoRepositorioLocal
import br.com.fiap.bussola.dados.repositorio.TrilhaRepositorio
import br.com.fiap.bussola.dados.repositorio.TrilhaRepositorioLocal
import br.com.fiap.bussola.dominio.uso.SimularDivida


interface ContainerApp {
    val indicadoresRepositorio: IndicadoresRepositorio
    val orcamentoRepositorio: OrcamentoRepositorio
    val trilhaRepositorio: TrilhaRepositorio
    val simularDivida: SimularDivida
}

class ContainerAppPadrao(contexto: Context) : ContainerApp {

    private val preferencias by lazy { PreferenciasLocais(contexto.applicationContext) }

    override val indicadoresRepositorio: IndicadoresRepositorio by lazy {
        IndicadoresRepositorioBcb(FabricaDeRede.criarBcbApi())
    }

    override val orcamentoRepositorio: OrcamentoRepositorio by lazy {
        OrcamentoRepositorioLocal(preferencias)
    }

    override val trilhaRepositorio: TrilhaRepositorio by lazy {
        TrilhaRepositorioLocal(preferencias)
    }

    override val simularDivida: SimularDivida by lazy { SimularDivida() }
}
