package com.example.garbagehub.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bins")
data class GarbageBin(
    @PrimaryKey val id: String,
    val latitude: Double,
    val longitude: Double,
    val currentFillLevel: Int, // percentage
    val maxCapacity: Double, // in liters
    val lastUpdated: Date,
    val status: BinStatus,
    val sensorId: String
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val createdAt: Date
)

@Entity(tableName = "reports")
data class BinReport(
    @PrimaryKey val id: String,
    val binId: String,
    val userId: String,
    val reportType: ReportType,
    val description: String,
    val status: ReportStatus,
    val createdAt: Date,
    val resolvedAt: Date?
)

@Entity(tableName = "maintenance_tasks")
data class MaintenanceTask(
    @PrimaryKey val id: String,
    val binId: String,
    val assignedTo: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val description: String,
    val createdAt: Date,
    val dueDate: Date,
    val completedAt: Date?
)

enum class BinStatus {
    OPERATIONAL,
    MAINTENANCE_REQUIRED,
    OUT_OF_SERVICE,
    SENSOR_ERROR
}

enum class UserRole {
    USER,
    ADMIN,
    MAINTENANCE_WORKER
}

enum class ReportType {
    OVERFLOW,
    DAMAGE,
    SENSOR_ISSUE,
    OTHER
}

enum class ReportStatus {
    PENDING,
    IN_PROGRESS,
    RESOLVED,
    DISMISSED
}

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}
