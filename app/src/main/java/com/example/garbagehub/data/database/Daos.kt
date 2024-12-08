package com.example.garbagehub.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.garbagehub.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BinDao {
    @Query("SELECT * FROM bins")
    fun getAllBins(): Flow<List<GarbageBin>>

    @Query("SELECT * FROM bins WHERE id = :binId")
    suspend fun getBinById(binId: String): GarbageBin?

    @Query("SELECT * FROM bins WHERE currentFillLevel >= :threshold")
    fun getBinsAboveThreshold(threshold: Int): Flow<List<GarbageBin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBin(bin: GarbageBin)

    @Update
    suspend fun updateBin(bin: GarbageBin)

    @Delete
    suspend fun deleteBin(bin: GarbageBin)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports")
    fun getAllReports(): Flow<List<BinReport>>

    @Query("SELECT * FROM reports WHERE binId = :binId")
    fun getReportsForBin(binId: String): Flow<List<BinReport>>

    @Query("SELECT * FROM reports WHERE status = :status")
    fun getReportsByStatus(status: ReportStatus): Flow<List<BinReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: BinReport)

    @Update
    suspend fun updateReport(report: BinReport)

    @Delete
    suspend fun deleteReport(report: BinReport)
}

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_tasks")
    fun getAllTasks(): Flow<List<MaintenanceTask>>

    @Query("SELECT * FROM maintenance_tasks WHERE assignedTo = :userId")
    fun getTasksForUser(userId: String): Flow<List<MaintenanceTask>>

    @Query("SELECT * FROM maintenance_tasks WHERE status = :status")
    fun getTasksByStatus(status: TaskStatus): Flow<List<MaintenanceTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MaintenanceTask)

    @Update
    suspend fun updateTask(task: MaintenanceTask)

    @Delete
    suspend fun deleteTask(task: MaintenanceTask)
}
