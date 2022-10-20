package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset

class AddAssetToCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(asset: CryptoAsset, collectionName: String): Result<Unit> =
        userCollectionsRepository.addToCollection(asset, collectionName)
}

class AddFavouriteUseCase(private val addAssetToCollectionUseCase: AddAssetToCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = addAssetToCollectionUseCase.invoke(
        asset,
        UserCollectionsRepository.FAVOURITES_COLLECTION_NAME
    )
}

class AddRecentUseCase(private val addAssetToCollectionUseCase: AddAssetToCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) =
        addAssetToCollectionUseCase.invoke(asset, UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}

class CreateCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(name: String) = userCollectionsRepository.createCollection(name)
}

class GetAllCollectionNamesUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    operator fun invoke() = userCollectionsRepository.getAllCollectionNames()
}

class GetCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    operator fun invoke(name: String) = userCollectionsRepository.getCollection(name)
}

class GetFavouritesUseCase(private val getCollectionUseCase: GetCollectionUseCase) {
    operator fun invoke() =
        getCollectionUseCase.invoke(UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
}

class GetRecentsUseCase(private val getCollectionUseCase: GetCollectionUseCase) {
    operator fun invoke() =
        getCollectionUseCase.invoke(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}

class RemoveCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(name: String) = userCollectionsRepository.removeCollection(name)
}

class RemoveFavouriteUseCase(private val removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = removeAssetFromCollectionUseCase.invoke(
        asset,
        UserCollectionsRepository.FAVOURITES_COLLECTION_NAME
    )
}

class ClearRecentsUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke() =
        userCollectionsRepository.clearCollection(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
}

class RemoveRecentUseCase(private val removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase) {
    suspend operator fun invoke(asset: CryptoAsset) = removeAssetFromCollectionUseCase.invoke(
        asset,
        UserCollectionsRepository.RECENTS_COLLECTION_NAME
    )
}

class RemoveAssetFromCollectionUseCase(private val userCollectionsRepository: UserCollectionsRepository) {
    suspend operator fun invoke(asset: CryptoAsset, collectionName: String) =
        userCollectionsRepository.removeFromCollection(asset, collectionName)
}

