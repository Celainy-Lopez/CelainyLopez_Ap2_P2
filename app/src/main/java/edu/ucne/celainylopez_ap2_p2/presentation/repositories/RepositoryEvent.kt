package edu.ucne.celainylopez_ap2_p2.presentation.repositories

sealed interface RepositoryEvent {

    data object GetRepositories: RepositoryEvent

    data object PostRepository: RepositoryEvent

    data object PutRepository: RepositoryEvent

    data  object DeleteRepository: RepositoryEvent
}