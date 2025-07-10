package edu.ucne.celainylopez_ap2_p2.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.celainylopez_ap2_p2.presentation.repositories.RepositoryListScreen

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navHostController,
        startDestination = Screen.RepositoryList
    ) {
        composable<Screen.RepositoryList> {
            RepositoryListScreen(
                goToRepository = { id ->
                    navHostController.navigate(Screen.Repository(null))
                },
                createRepository = {
                    navHostController.navigate(Screen.Repository(null))
                },
                drawerState = drawerState,
                scope = scope
            )
        }
    }
}