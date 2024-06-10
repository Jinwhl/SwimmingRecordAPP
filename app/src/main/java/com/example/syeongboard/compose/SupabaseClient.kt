package com.example.syeongboard.compose

import com.example.syeongboard.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.mySupabaseURL,
    supabaseKey = BuildConfig.mySupabaseAnonKey,
) { install(Postgrest) }

object SupabaseClient {
    @Serializable
    data class SwimRecord(
        val id: Int? = null, // id is now nullable to allow automatic ID generation
        val swimDate: String?,
        val startTime: String,
        val endTime: String,
        val butterflyDistance: Int?,
        val backstrokeDistance: Int?,
        val breaststrokeDistance: Int?,
        val freestyleDistance: Int?,
        var poolName: String? = null,
        var notes: String? = null,
    )

    suspend fun addDataToSupabase(
        swimDate: String,
        startTime: String,
        endTime: String,
        distances: Map<String, Int>,
        poolName: String?,
        notes: String?,
    ) {
        val swimRecord = SwimRecord(
            swimDate = swimDate,
            startTime = startTime,
            endTime = endTime,
            butterflyDistance = distances["butterfly"],
            backstrokeDistance = distances["backstroke"],
            breaststrokeDistance = distances["breaststroke"],
            freestyleDistance = distances["freestyle"],
            poolName = poolName,
            notes = notes
        )
        withContext(Dispatchers.IO) {
            supabase.from("SwimRecords").insert(swimRecord)
        }
    }
    suspend fun getSwimRecordByDate(date: String): SwimRecord? {
        return withContext(Dispatchers.IO) {
            val response = supabase.from("SwimRecords")
                .select(){
                    filter { eq("swimDate", date) }
                }
            response.decodeSingleOrNull<SwimRecord>()
        }
    }
    suspend fun getAllSwimRecords(): List<SwimRecord> {
        return withContext(Dispatchers.IO) {
            val response = supabase.from("SwimRecords")
                .select()
            response.decodeList<SwimRecord>()
        }
    }
}