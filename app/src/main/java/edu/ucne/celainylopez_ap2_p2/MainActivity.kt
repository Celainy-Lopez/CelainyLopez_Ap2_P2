package edu.ucne.celainylopez_ap2_p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.celainylopez_ap2_p2.presentation.Repositorys.RepositoryListScreen
import edu.ucne.celainylopez_ap2_p2.ui.theme.CelainyLopez_Ap2_P2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CelainyLopez_Ap2_P2Theme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                RepositoryListScreen(
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    }
}