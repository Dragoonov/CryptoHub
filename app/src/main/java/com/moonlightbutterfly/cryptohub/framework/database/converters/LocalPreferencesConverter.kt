package com.moonlightbutterfly.cryptohub.framework.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonlightbutterfly.cryptohub.models.LocalPreferences

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class LocalPreferencesConverter {

    @TypeConverter
    fun fromLocalPreferences(localPreferences: LocalPreferences): String {
        return Gson().toJson(localPreferences)
    }

    @TypeConverter
    fun stringToLocalPreferences(serializedPreferences: String): LocalPreferences {
        return Gson().fromJson(serializedPreferences, LocalPreferences::class.java)
    }
}
