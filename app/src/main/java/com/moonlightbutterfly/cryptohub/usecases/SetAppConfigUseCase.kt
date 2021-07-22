package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import database.AppConfig
import javax.inject.Inject

class SetAppConfigUseCase @Inject constructor(private val cryptoHubInternalRepository: CryptoHubInternalRepository) {

    suspend fun updateAppConfig(appConfig: AppConfig) {
        cryptoHubInternalRepository.updateAppConfig(appConfig)
    }

}