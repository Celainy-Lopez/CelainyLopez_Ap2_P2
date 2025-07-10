package edu.ucne.celainylopez_ap2_p2.data.remote

import edu.ucne.celainylopez_ap2_p2.data.remote.dto.ContribuidorDto
import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}/repos")
   suspend fun listRepos(@Path("username") username: String): List<RepositoryDto>

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun listContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<ContribuidorDto>
}