package com.moonlightbutterfly.cryptohub.repository

import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyItemOutput
import com.moonlightbutterfly.cryptohub.repository.retrofit.NomicsService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Repository returning the info about cryptocurrencies, using the Nomics (https://nomics.com/docs) API.
 */
class CryptoHubExternalRepositoryImpl @Inject constructor() : CryptoHubExternalRepository {
    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nomics.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(NomicsService::class.java)
    }

    override suspend fun getCryptocurrenciesOutput(page: Int): List<CryptocurrencyItemOutput> {
        try {
            return service.getCryptocurrencyOutputs(page = page)
        } catch (exception: HttpException) {
            print(exception)
        }
        return emptyList()
    }

    override suspend fun getCryptocurrencyOutput(cryptoSymbol: String): CryptocurrencyItemOutput {
        try {
            return service.getCryptocurrencyOutputs(id = cryptoSymbol).first()
        } catch (exception: HttpException) {
            print(exception)
        }
        return CryptocurrencyItemOutput.EMPTY
    }
}
