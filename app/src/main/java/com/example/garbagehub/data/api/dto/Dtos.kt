package com.example.garbagehub.data.api.dto

import com.example.garbagehub.data.models.*
import java.util.Date

data class BinDto(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val currentFillLevel: Int,
    val maxCapacity: Double,
    val lastUpdated: Date,
    val status: BinStatus,
    val sensorId: String
)

data class ReportDto(
    val id: String,
    val binId: String,
    val userId: String,
    val reportType: ReportType,
    val description: String,
    val status: ReportStatus,
    val createdAt: Date,
    val resolvedAt: Date?
)

data class MaintenanceTaskDto(
    val id: String,
    val binId: String,
    val assignedTo: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val description: String,
    val createdAt: Date,
    val dueDate: Date,
    val completedAt: Date?
)

// Extension functions to convert between DTOs and domain models
fun BinDto.toDomainModel() = GarbageBin(
    id = id,
    latitude = latitude,
    longitude = longitude,
    currentFillLevel = currentFillLevel,
    maxCapacity = maxCapacity,
    lastUpdated = lastUpdated,
    status = status,
    sensorId = sensorId
)

fun GarbageBin.toDto() = BinDto(
    id = id,
    latitude = latitude,
    longitude = longitude,
    currentFillLevel = currentFillLevel,
    maxCapacity = maxCapacity,
    lastUpdated = lastUpdated,
    status = status,
    sensorId = sensorId
)

fun ReportDto.toDomainModel() = BinReport(
    id = id,
    binId = binId,
    userId = userId,
    reportType = reportType,
    description = description,
    status = status,
    createdAt = createdAt,
    resolvedAt = resolvedAt
)

fun BinReport.toDto() = ReportDto(
    id = id,
    binId = binId,
    userId = userId,
    reportType = reportType,
    description = description,
    status = status,
    createdAt = createdAt,
    resolvedAt = resolvedAt
)

fun MaintenanceTaskDto.toDomainModel() = MaintenanceTask(
    id = id,
    binId = binId,
    assignedTo = assignedTo,
    status = status,
    priority = priority,
    description = description,
    createdAt = createdAt,
    dueDate = dueDate,
    completedAt = completedAt
)

fun MaintenanceTask.toDto() = MaintenanceTaskDto(
    id = id,
    binId = binId,
    assignedTo = assignedTo,
    status = status,
    priority = priority,
    description = description,
    createdAt = createdAt,
    dueDate = dueDate,
    completedAt = completedAt
)
