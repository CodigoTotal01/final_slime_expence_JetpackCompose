package com.nikolovlazar.goodbyemoney.features.auth.domain.models

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val roles: List<String>,
    val token: String
) {
    val isAdmin: Boolean
        get() = roles.contains("admin")
}
