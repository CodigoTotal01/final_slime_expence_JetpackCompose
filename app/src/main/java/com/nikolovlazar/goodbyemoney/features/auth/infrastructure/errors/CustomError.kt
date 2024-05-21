package com.nikolovlazar.goodbyemoney.features.auth.infrastructure.errors

class WrongCredentials : Exception()
class ConnectionError : Exception()
class InvalidToken : Exception()

class CustomError(
    override val message: String,
    val loggedRequired: Boolean = false
) : Exception(message)