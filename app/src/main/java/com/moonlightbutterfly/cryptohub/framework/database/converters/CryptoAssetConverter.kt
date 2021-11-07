package com.moonlightbutterfly.cryptohub.framework.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class CryptoAssetConverter {

    @TypeConverter
    fun fromUserSettings(asset: CryptoAsset): String {
        return Gson().toJson(asset)
    }

    @TypeConverter
    fun stringToUserSettings(serializedAsset: String): CryptoAsset {
        return Gson().fromJson(serializedAsset, CryptoAsset::class.java)
    }
}
