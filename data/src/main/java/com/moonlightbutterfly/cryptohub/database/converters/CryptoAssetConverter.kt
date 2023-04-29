package com.moonlightbutterfly.cryptohub.database.converters

import androidx.room.TypeConverter
import com.moonlightbutterfly.cryptohub.database.dtos.CryptoAssetDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class CryptoAssetConverter {

    @TypeConverter
    fun fromCryptoAssetList(assets: List<CryptoAssetDto>): String {
        return Json.encodeToString(assets)
    }

    @TypeConverter
    fun stringToCryptoAssetList(serializedAsset: String): List<CryptoAssetDto> {
        return Json.decodeFromString(serializedAsset)
    }
}
