package edu.ucne.celainylopez_ap2_p2.data.remote.dto

import com.squareup.moshi.Json

data class ContribuidorDto (
    @Json(name = "login") val login: String,
    @Json(name = "id") val id: Int,
    @Json(name = "contributions") val contributions: Int,
    @Json(name = "type") val type: String
)