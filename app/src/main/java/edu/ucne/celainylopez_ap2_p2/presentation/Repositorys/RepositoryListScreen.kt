package edu.ucne.celainylopez_ap2_p2.presentation.Repositorys

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import edu.ucne.celainylopez_ap2_p2.ui.theme.CelainyLopez_Ap2_P2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryListScreen(
    viewModel: RepositoryViewModel = hiltViewModel(),
    // createRepository: () -> Unit
    //goToSistema: (Int) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    //goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var lastRetentionCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getRepositories("enelramon")
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
        //goToRepository = { id -> goToRepository(id) },
        // createRepository = createRepository
        //onEvent = viewModel::onEvent,
        drawerState = drawerState,
        scope = scope,
        reloadRepository = { viewModel.getRepositories("enelramon") },
        //goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RepositoryListBodyScreen(
    uiState: RepositoryUiState,
    //goToRepository: (Int) -> Unit,
    //createRepository: () -> Unit,
    //onEvent: (RepositoryEvent) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    reloadRepository: () -> Unit,
    //goBack: () -> Unit
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
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

            if (uiState.repository.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay repositorios registrados",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    items(uiState.repository) { repository ->
                        RepositoryRow(repository)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            if (!uiState.errorMessage.isNullOrEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
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
fun RepositoryRow(repository: RepositoryDto) {
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
        }
    }
    HorizontalDivider()
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