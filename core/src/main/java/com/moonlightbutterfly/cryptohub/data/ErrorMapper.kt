package com.moonlightbutterfly.cryptohub.data

interface ErrorMapper {
    fun mapError(throwable: Throwable): Error
}