package com.moonlightbutterfly.cryptohub.usecases

import androidx.lifecycle.map
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import javax.inject.Inject

class GetAppConfigSettingsUseCase @Inject constructor(cryptoHubInternalRepository: CryptoHubInternalRepository) {

    val appConfig = cryptoHubInternalRepository.appConfig

    val isNightModeEnabled = appConfig.map { it.nightModeEnabled }

}