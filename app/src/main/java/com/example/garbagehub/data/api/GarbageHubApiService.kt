package com.example.garbagehub.data.api

import com.example.garbagehub.data.models.*
import retrofit2.http.*

interface GarbageHubApiService {
    @GET("bins")
    suspend fun getAllBins(): List<BinDto>

    @GET("bins/{id}")
    suspend fun getBinById(@Path("id") binId: String): BinDto

    @GET("bins/nearby")
    suspend fun getNearbyBins(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): List<BinDto>

    @POST("reports")
    suspend fun submitReport(@Body report: ReportDto): ReportDto

    @GET("reports")
    suspend fun getReports(): List<ReportDto>

    @GET("reports/bin/{binId}")
    suspend fun getReportsForBin(@Path("binId") binId: String): List<ReportDto>

    @POST("maintenance")
    suspend fun createMaintenanceTask(@Body task: MaintenanceTaskDto): MaintenanceTaskDto

    @GET("maintenance/worker/{workerId}")
    suspend fun getMaintenanceTasksForWorker(@Path("workerId") workerId: String): List<MaintenanceTaskDto>

    @PUT("maintenance/{taskId}")
    suspend fun updateMaintenanceTask(
        @Path("taskId") taskId: String,
        @Body task: MaintenanceTaskDto
    ): MaintenanceTaskDto
}
