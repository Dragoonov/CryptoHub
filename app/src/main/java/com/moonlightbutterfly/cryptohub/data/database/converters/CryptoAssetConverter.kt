package com.moonlightbutterfly.cryptohub.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import java.lang.reflect.Type

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class CryptoAssetConverter {

    @TypeConverter
    fun fromCryptoAssetList(assets: List<CryptoAsset>): String {
        return Gson().toJson(assets)
    }

    @TypeConverter
    fun stringToCryptoAssetList(serializedAsset: String): List<CryptoAsset> {
        val list: Type = object : TypeToken<List<CryptoAsset>>() {}.type
        return Gson().fromJson(serializedAsset, list)
    }
}
