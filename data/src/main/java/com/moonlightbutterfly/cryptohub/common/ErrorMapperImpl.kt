package com.moonlightbutterfly.cryptohub.common

import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import retrofit2.HttpException
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor() : ErrorMapper {
    override fun mapError(throwable: Throwable): Error {
        return when (throwable) {
            is HttpException -> Error.Network(throwable.toString())
            else -> Error.Unknown(throwable.message ?: "")
        }
    }
}
