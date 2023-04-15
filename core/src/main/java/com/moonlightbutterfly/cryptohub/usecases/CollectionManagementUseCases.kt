package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

fun interface AddAssetToCollectionUseCase : suspend (CryptoAsset, String) -> Answer<Unit>
fun interface AddFavouriteUseCase : suspend (CryptoAsset) -> Answer<Unit>
fun interface AddRecentUseCase : suspend (CryptoAsset) -> Answer<Unit>
fun interface CreateCollectionUseCase : suspend (String) -> Answer<Unit>
fun interface GetCollectionUseCase : (String) -> Flow<Answer<CryptoCollection>>
fun interface GetFavouritesUseCase : () -> Flow<Answer<CryptoCollection>>
fun interface GetRecentsUseCase : () -> Flow<Answer<CryptoCollection>>
fun interface RemoveCollectionUseCase : suspend (String) -> Answer<Unit>
fun interface RemoveFavouriteUseCase : suspend (CryptoAsset) -> Answer<Unit>
fun interface ClearRecentsUseCase : suspend () -> Answer<Unit>
fun interface RemoveRecentUseCase : suspend (CryptoAsset) -> Answer<Unit>
fun interface RemoveAssetFromCollectionUseCase : suspend (CryptoAsset, String) -> Answer<Unit>
