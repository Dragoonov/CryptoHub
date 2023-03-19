package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository

class GetAllCryptoAssetsMarketInfoUseCase(private val cryptoAssetsRepository: CryptoAssetsRepository) {
    operator fun invoke(page: Int) = cryptoAssetsRepository.getCryptoAssetsMarketInfo(page)
}

class GetCryptoAssetsMarketInfoUseCase(private val cryptoAssetsRepository: CryptoAssetsRepository) {
    operator fun invoke(symbols: List<String>) = cryptoAssetsRepository.getCryptoAssetsMarketInfo(symbols)
}
