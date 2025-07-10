package edu.ucne.celainylopez_ap2_p2.presentation.contributors

import edu.ucne.celainylopez_ap2_p2.data.remote.dto.ContribuidorDto

data class ContributorUiState(
    val login: String = "",
    val id: Int = 0,
    val contribution: Int = 0,
    val type: String = "",
    val isLoading: Boolean = false,
    val contributor: List<ContribuidorDto> = emptyList(),
    val errorMessage: String? = null,
)