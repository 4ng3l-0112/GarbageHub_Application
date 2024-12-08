package com.example.garbagehub.data.repository

import com.example.garbagehub.data.api.GarbageHubApiService
import com.example.garbagehub.data.api.dto.toDomainModel
import com.example.garbagehub.data.api.dto.toDto
import com.example.garbagehub.data.database.BinDao
import com.example.garbagehub.data.models.GarbageBin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BinRepository @Inject constructor(
    private val binDao: BinDao,
    private val apiService: GarbageHubApiService
) {
    fun getAllBins(): Flow<List<GarbageBin>> {
        return binDao.getAllBins()
    }

    suspend fun getBinById(binId: String): GarbageBin? {
        return binDao.getBinById(binId)
    }

    suspend fun refreshBins() {
        try {
            val bins = apiService.getAllBins().map { it.toDomainModel() }
            bins.forEach { bin ->
                binDao.insertBin(bin)
            }
        } catch (e: Exception) {
            // Handle error or use cached data
        }
    }

    suspend fun getNearbyBins(latitude: Double, longitude: Double, radius: Double): List<GarbageBin> {
        return try {
            apiService.getNearbyBins(latitude, longitude, radius)
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            // Return cached bins within radius as fallback
            emptyList()
        }
    }

    suspend fun updateBinFillLevel(binId: String, fillLevel: Int) {
        binDao.getBinById(binId)?.let { bin ->
            val updatedBin = bin.copy(currentFillLevel = fillLevel)
            binDao.updateBin(updatedBin)
        }
    }
}
