package com.moonlightbutterfly.cryptohub.repository

import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptoassetItemOutput
import com.moonlightbutterfly.cryptohub.repository.retrofit.NomicsService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Repository returning the info about cryptoassets, using the Nomics (https://nomics.com/docs) API.
 */
class CryptoHubExternalRepositoryImpl @Inject constructor() : CryptoHubExternalRepository {
    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nomics.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(NomicsService::class.java)
    }

    override suspend fun getCryptoassetsOutput(ids: String, page: Int): List<CryptoassetItemOutput> {
        try {
            return if (ids.isEmpty()) {
                service.getCryptoassetOutputs(page = page)
            } else {
                service.getCryptoassetOutputs(page = page, ids = ids)
            }
        } catch (exception: HttpException) {
            print(exception)
        }
        //TODO Change after changing nomics
        return listOf(
            CryptoassetItemOutput("Bitcoin"),
            CryptoassetItemOutput("Szitcoin"),
            CryptoassetItemOutput("Altcoin"),
            CryptoassetItemOutput("Kucoin"))
    }

    override suspend fun getCryptoassetOutput(cryptoSymbol: String): CryptoassetItemOutput {
        try {
            return service.getCryptoassetOutputs(id = cryptoSymbol).first()
        } catch (exception: HttpException) {
            //TODO Change after changing nomics
            print(exception)
        }
        return CryptoassetItemOutput.EMPTY
    }
}
