package com.moonlightbutterfly.cryptohub.database.converters

import androidx.room.TypeConverter
import com.moonlightbutterfly.cryptohub.database.dtos.LocalPreferencesDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Class used for converting objects to Room database types and vice versa.
 */
class LocalPreferencesConverter {

    @TypeConverter
    fun fromLocalPreferences(localPreferences: LocalPreferencesDto): String {
        return Json.encodeToString(localPreferences)
    }

    @TypeConverter
    fun stringToLocalPreferences(serializedPreferences: String): LocalPreferencesDto {
        return Json.decodeFromString(serializedPreferences)
    }
}
