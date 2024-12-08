package com.example.garbagehub.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garbagehub.data.models.BinReport
import com.example.garbagehub.data.models.ReportStatus
import com.example.garbagehub.data.models.ReportType
import com.example.garbagehub.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _reportSubmitted = MutableLiveData<Boolean>()
    val reportSubmitted: LiveData<Boolean> = _reportSubmitted

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun submitReport(reportType: ReportType, description: String) {
        viewModelScope.launch {
            try {
                val report = BinReport(
                    id = UUID.randomUUID().toString(),
                    binId = "", // Will be set from selected bin
                    userId = "", // Will be set from current user
                    reportType = reportType,
                    description = description,
                    status = ReportStatus.PENDING,
                    createdAt = Date(),
                    resolvedAt = null
                )
                
                reportRepository.submitReport(report)
                _reportSubmitted.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
