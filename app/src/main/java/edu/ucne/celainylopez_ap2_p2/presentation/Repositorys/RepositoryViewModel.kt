package edu.ucne.celainylopez_ap2_p2.presentation.Repositorys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.celainylopez_ap2_p2.data.remote.Resource
import edu.ucne.celainylopez_ap2_p2.data.repository.RepositoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repositoryRepository: RepositoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepositoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getRepositories("enelramon")
    }

    fun getRepositories(username: String) {
        viewModelScope.launch {
            repositoryRepository.getRepositories(username).collectLatest { getting ->
                when (getting) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true)}
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                repository = getting.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}