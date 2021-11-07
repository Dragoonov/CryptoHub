package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository

class GetAllCryptoAssetsMarketInfoUseCase(private val cryptoAssetsRepository: CryptoAssetsRepository) {
    suspend operator fun invoke(page: Int) = cryptoAssetsRepository.getCryptoAssetsMarketInfo(page)
}
