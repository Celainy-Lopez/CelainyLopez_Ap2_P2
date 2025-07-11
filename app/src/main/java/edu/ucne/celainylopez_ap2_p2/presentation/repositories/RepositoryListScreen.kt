package edu.ucne.celainylopez_ap2_p2.presentation.repositories

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListScreen(
    viewModel: RepositoryViewModel = hiltViewModel(),
    createRepository: () -> Unit,
    goToRepository: (String) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    //goBack: () -> Unit
    deleteRepository: ((RepositoryDto) -> Unit)? = null,
    goToContributors: (String, String) -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var lastRetentionCount by remember { mutableStateOf(0) }

    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        delay(190000)
        viewModel.onEvent(RepositoryEvent.GetRepositories)
    }

    LaunchedEffect(uiState.repository) {
        if (uiState.repository.size > lastRetentionCount) {
            Toast.makeText(
                context,
                "Nueva repository: ${uiState.repository.lastOrNull()?.description}",
                Toast.LENGTH_LONG
            ).show()
        }
        lastRetentionCount = uiState.repository.size
    }

    RepositoryListBodyScreen(
        uiState = uiState,
        goToRepository = { string -> goToRepository(string) },
        createRepository = createRepository,
        //onEvent = viewModel::onEvent,
        drawerState = drawerState,
        scope = scope,
        reloadRepository = { viewModel.onEvent(RepositoryEvent.GetRepositories) },
        //goBack = goBack
        deleteRepository = deleteRepository,
        query = query,
        searchResults = searchResults,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        goToContributors = goToContributors
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RepositoryListBodyScreen(
    uiState: RepositoryUiState,
    goToRepository: (String) -> Unit,
    createRepository: () -> Unit,
    //onEvent: (RepositoryEvent) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    reloadRepository: () -> Unit,
    //goBack: () -> Unit
    deleteRepository: ((RepositoryDto) -> Unit)? = null,
    query: String,
    searchResults: List<RepositoryDto>,
    onSearchQueryChanged: (String) -> Unit,
    goToContributors: (String, String) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = reloadRepository
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Repositorios",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        ),
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),

                actions = {
                    IconButton(
                        onClick = reloadRepository,
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            tint = Color.White,
                            contentDescription = "Actualizar"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { createRepository }) {
                Icon(Icons.Filled.Add, "Agregar")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.repository.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.repository.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron repositorios",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            SearchBar(
                                query = query,
                                onQueryChanged = onSearchQueryChanged,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        val reposToShow =
                            if (query.isNotBlank()) searchResults else uiState.repository

                        items(reposToShow) { repository ->
                            RepositoryRow(
                                repository = repository,
                                goToRepository = { goToRepository(repository.name) },
                                deleteRepository = deleteRepository,
                                { goToContributors("enelramon", repository.name) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            if (!uiState.errorMessage.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun RepositoryRow(
    repository: RepositoryDto,
    goToRepository: () -> Unit,
    deleteRepository: ((RepositoryDto) -> Unit)?,
    onViewContributors: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row {
                Text(text = "Nombre: ", fontWeight = FontWeight.ExtraBold)
                Text(text = repository.name)
            }

            Spacer(modifier = Modifier.weight(2f))


            Row {
                Text(text = "Descrion: ", fontWeight = FontWeight.ExtraBold)
                Text(text = repository.description ?: "Sin Descripción")
            }

            Spacer(modifier = Modifier.weight(2f))


            Row {
                Text(text = "Html URL: ", fontWeight = FontWeight.ExtraBold)
                Text(text = repository.htmlUrl ?: "No Disponible")
            }

            TextButton(
                onClick = onViewContributors,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Ver Colaboradores",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
    HorizontalDivider()
}


@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        label = { Text("Buscar Repositorio...") },
        singleLine = true
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val repositories = listOf(
        RepositoryDto(
            name = "Repositorio1",
            description = "Descripción del repositorio 1",
            htmlUrl = "https://github.com/enelramon/repositorio1"
        ),
        RepositoryDto(
            name = "Repositorio2",
            description = "Descripción del repositorio 2",
            htmlUrl = "https://github.com/enelramon/repositorio2"
        )
    )
}