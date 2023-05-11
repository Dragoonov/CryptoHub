package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow

fun interface GetAllCryptoAssetsMarketInfoUseCase : (Int) -> Flow<Answer<List<CryptoAssetMarketInfo>>>
fun interface GetCryptoAssetsMarketInfoUseCase : (List<String>) -> Flow<Answer<List<CryptoAssetMarketInfo>>>
