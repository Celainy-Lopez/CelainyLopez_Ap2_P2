package edu.ucne.celainylopez_ap2_p2.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.celainylopez_ap2_p2.presentation.repositories.RepositoryListScreen
import androidx.navigation.toRoute
import edu.ucne.celainylopez_ap2_p2.presentation.contributors.ContributorListScreen


@Composable
fun HomeNavHost(
    navHostController: NavHostController,

    ) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navHostController,
        startDestination = Screen.RepositoryList
    ) {

        composable<Screen.RepositoryList> {
            RepositoryListScreen(
                goToRepository = { repoName ->
                    navHostController.navigate(
                        Screen.ContributorList(
                            owner = "enelramon",
                            repo = repoName
                        )
                    )
                },
                createRepository = {
                    navHostController.navigate(Screen.Repository(null))
                },
                drawerState = drawerState,
                scope = scope,
                goToContributors = { owner, repoName ->
                    navHostController.navigate(Screen.ContributorList(owner, repoName))
                }
            )
        }

        composable<Screen.ContributorList> { backStackEntry ->
            val owner = backStackEntry.toRoute<Screen.ContributorList>().owner
            val repo = backStackEntry.toRoute<Screen.ContributorList>().repo

            ContributorListScreen(
                owner = owner,
                repo = repo,
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}