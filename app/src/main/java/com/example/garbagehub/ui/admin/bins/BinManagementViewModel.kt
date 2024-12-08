package com.example.garbagehub.ui.admin.bins

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagehub.data.models.GarbageBin
import com.example.garbagehub.data.repository.BinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinManagementViewModel @Inject constructor(
    private val binRepository: BinRepository
) : ViewModel() {

    val bins: LiveData<List<GarbageBin>> = binRepository.getAllBins().asLiveData()

    init {
        refreshBins()
    }

    private fun refreshBins() {
        viewModelScope.launch {
            try {
                binRepository.refreshBins()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateBinThreshold(binId: String, threshold: Int) {
        viewModelScope.launch {
            try {
                // Implement threshold update
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addNewBin(bin: GarbageBin) {
        viewModelScope.launch {
            try {
                binRepository.addBin(bin)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
