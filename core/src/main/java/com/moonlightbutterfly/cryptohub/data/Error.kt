package com.moonlightbutterfly.cryptohub.data

sealed class Error(open val message: String) {

    data class Network(override val message: String) : Error(message)

    data class NotFound(override val message: String) : Error(message)

    data class AccessDenied(override val message: String) : Error(message)

    data class ServiceUnavailable(override val message: String) : Error(message)

    data class Unknown(override val message: String) : Error(message)
}
