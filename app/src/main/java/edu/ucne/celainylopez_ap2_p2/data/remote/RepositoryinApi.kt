package edu.ucne.celainylopez_ap2_p2.data.remote

import edu.ucne.celainylopez_ap2_p2.data.remote.dto.RepositoryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String): List<RepositoryDto>
}