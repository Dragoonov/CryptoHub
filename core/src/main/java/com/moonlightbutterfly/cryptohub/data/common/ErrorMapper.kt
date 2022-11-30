package com.moonlightbutterfly.cryptohub.data.common

interface ErrorMapper {
    fun mapError(throwable: Throwable): Error
}
