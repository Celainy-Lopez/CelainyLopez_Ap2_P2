package edu.ucne.celainylopez_ap2_p2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    data class Repository(val Id: Int?): Screen()

    @Serializable
    data object RepositoryList: Screen()

    @Serializable
    data class ContributorList(val owner: String, val repo: String) : Screen()
}