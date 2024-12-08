package com.example.garbagehub.data.repository

import com.example.garbagehub.data.api.GarbageHubApiService
import com.example.garbagehub.data.api.dto.toDomainModel
import com.example.garbagehub.data.api.dto.toDto
import com.example.garbagehub.data.database.MaintenanceDao
import com.example.garbagehub.data.models.MaintenanceTask
import com.example.garbagehub.data.models.TaskStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaintenanceRepository @Inject constructor(
    private val maintenanceDao: MaintenanceDao,
    private val apiService: GarbageHubApiService
) {
    fun getAllTasks(): Flow<List<MaintenanceTask>> {
        return maintenanceDao.getAllTasks()
    }

    fun getTasksForWorker(workerId: String): Flow<List<MaintenanceTask>> {
        return maintenanceDao.getTasksForUser(workerId)
    }

    suspend fun createTask(task: MaintenanceTask) {
        try {
            val createdTask = apiService.createMaintenanceTask(task.toDto()).toDomainModel()
            maintenanceDao.insertTask(createdTask)
        } catch (e: Exception) {
            // Store locally if API call fails
            maintenanceDao.insertTask(task)
            throw e
        }
    }

    suspend fun updateTaskStatus(taskId: String, status: TaskStatus) {
        maintenanceDao.getTaskById(taskId)?.let { task ->
            val updatedTask = task.copy(status = status)
            try {
                val serverTask = apiService.updateMaintenanceTask(taskId, updatedTask.toDto()).toDomainModel()
                maintenanceDao.updateTask(serverTask)
            } catch (e: Exception) {
                // Update locally if API call fails
                maintenanceDao.updateTask(updatedTask)
                throw e
            }
        }
    }

    suspend fun refreshTasks(workerId: String) {
        try {
            val tasks = apiService.getMaintenanceTasksForWorker(workerId).map { it.toDomainModel() }
            tasks.forEach { task ->
                maintenanceDao.insertTask(task)
            }
        } catch (e: Exception) {
            // Handle error or use cached data
        }
    }
}
