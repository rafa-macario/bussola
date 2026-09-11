package br.com.fiap.bussola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.fiap.bussola.core.ui.tema.BussolaTema
import br.com.fiap.bussola.ui.navegacao.NavGrafoBussola


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BussolaTema {
                NavGrafoBussola()
            }
        }
    }
}
