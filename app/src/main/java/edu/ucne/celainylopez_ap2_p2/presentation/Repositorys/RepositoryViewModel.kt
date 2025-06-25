package edu.ucne.celainylopez_ap2_p2.presentation.Repositorys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.celainylopez_ap2_p2.data.remote.Resource
import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import edu.ucne.celainylopez_ap2_p2.data.repository.RepositoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repositoryRepository: RepositoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepositoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<RepositoryDto>>(emptyList())
    val searchResults: StateFlow<List<RepositoryDto>> = _searchResults.asStateFlow()

    init {
        getRepositories("enelramon")

        viewModelScope.launch {
            _searchQuery
                .debounce(600)
                .distinctUntilChanged()
                .mapLatest { query ->
                    filterRepositories(query)
                }
                .collectLatest { filtered ->
                    _searchResults.value = filtered
                }
        }
    }


    fun onEvent(event: RepositoryEvent){
        when(event) {
            RepositoryEvent.DeleteRepository -> TODO()
            RepositoryEvent.GetRepositories -> getRepositories("enelramon")
            RepositoryEvent.PostRepository -> TODO()
            RepositoryEvent.PutRepository-> TODO()
        }
    }

    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query
    }


    private fun filterRepositories(query:String): List<RepositoryDto>{
        return if (query.isBlank()){
            _uiState.value.repository
        }else{
            _uiState.value.repository.filter {
                it.name.contains(query, ignoreCase = true) ||
                        (it.description?.contains(query, ignoreCase = true) ?: false)
            }
        }
    }

    fun getRepositories(username: String) {
        viewModelScope.launch {
            repositoryRepository.getRepositories(username).collectLatest { getting ->
                when (getting) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
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