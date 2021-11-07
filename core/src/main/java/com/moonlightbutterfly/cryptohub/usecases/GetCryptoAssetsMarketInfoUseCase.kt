package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository

class GetCryptoAssetsMarketInfoUseCase(private val cryptoAssetsRepository: CryptoAssetsRepository) {
    suspend operator fun invoke(symbols: List<String>) = cryptoAssetsRepository.getCryptoAssetsMarketInfo(symbols)
}
