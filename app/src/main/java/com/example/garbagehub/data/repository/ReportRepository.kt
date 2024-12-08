package com.example.garbagehub.data.repository

import com.example.garbagehub.data.api.GarbageHubApiService
import com.example.garbagehub.data.api.dto.toDomainModel
import com.example.garbagehub.data.api.dto.toDto
import com.example.garbagehub.data.database.ReportDao
import com.example.garbagehub.data.models.BinReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepository @Inject constructor(
    private val reportDao: ReportDao,
    private val apiService: GarbageHubApiService
) {
    fun getAllReports(): Flow<List<BinReport>> {
        return reportDao.getAllReports()
    }

    fun getReportsForBin(binId: String): Flow<List<BinReport>> {
        return reportDao.getReportsForBin(binId)
    }

    suspend fun submitReport(report: BinReport) {
        try {
            val submittedReport = apiService.submitReport(report.toDto()).toDomainModel()
            reportDao.insertReport(submittedReport)
        } catch (e: Exception) {
            // Store locally if API call fails
            reportDao.insertReport(report)
            throw e
        }
    }

    suspend fun refreshReports() {
        try {
            val reports = apiService.getReports().map { it.toDomainModel() }
            reports.forEach { report ->
                reportDao.insertReport(report)
            }
        } catch (e: Exception) {
            // Handle error or use cached data
        }
    }
}
