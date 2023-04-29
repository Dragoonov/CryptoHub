package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetDto(
    val name: String,
    val symbol: String,
    val logoUrl: String
)

fun CryptoAssetDto.toCryptoAsset(): CryptoAsset = CryptoAsset(name, symbol, logoUrl)
fun CryptoAsset.toCryptoAssetDto(): CryptoAssetDto = CryptoAssetDto(name, symbol, logoUrl)
