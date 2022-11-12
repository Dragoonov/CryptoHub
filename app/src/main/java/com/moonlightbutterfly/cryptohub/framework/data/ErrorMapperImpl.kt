package com.moonlightbutterfly.cryptohub.framework.data

import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.ErrorMapper
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
