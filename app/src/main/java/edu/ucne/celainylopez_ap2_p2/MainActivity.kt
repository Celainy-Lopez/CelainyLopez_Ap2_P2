package edu.ucne.celainylopez_ap2_p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.celainylopez_ap2_p2.presentation.navigation.HomeNavHost
import edu.ucne.celainylopez_ap2_p2.ui.theme.CelainyLopez_Ap2_P2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CelainyLopez_Ap2_P2Theme {
                val navigation = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ){ innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ){
                        HomeNavHost(navigation)
                    }
                }
            }
        }
    }
}