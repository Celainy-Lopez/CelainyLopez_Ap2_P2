package edu.ucne.celainylopez_ap2_p2.data.remote.dto

import com.squareup.moshi.Json

data class RepositoryDto(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "html_url") val htmlUrl:String
)