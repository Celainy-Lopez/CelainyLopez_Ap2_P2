package edu.ucne.celainylopez_ap2_p2.presentation.Repositorys

sealed interface RepositoryEvent {

    data object GetRepositories: RepositoryEvent

    data object PostRepository: RepositoryEvent

    data object PutRepositories: RepositoryEvent

    data  object DeleteRepositories: RepositoryEvent
}