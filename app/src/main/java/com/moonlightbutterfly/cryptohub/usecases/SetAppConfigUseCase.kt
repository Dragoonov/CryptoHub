package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import com.moonlightbutterfly.cryptohub.repository.dataobjects.AppConfig
import javax.inject.Inject

class SetAppConfigUseCase @Inject constructor(private val cryptoHubInternalRepository: CryptoHubInternalRepository) {

    suspend fun updateAppConfig(appConfig: AppConfig) {
        cryptoHubInternalRepository.updateAppConfig(appConfig)
    }
}
