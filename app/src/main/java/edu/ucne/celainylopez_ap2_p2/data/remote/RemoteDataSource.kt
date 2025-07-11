package edu.ucne.celainylopez_ap2_p2.data.remote

import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val gitHubApi: GitHubApi
) {
    suspend fun getRepositories(username: String) = gitHubApi.listRepos(username)

    suspend fun getContributors(owner: String, repo: String) = gitHubApi.listContributors(owner, repo)

}