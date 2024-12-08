package com.example.garbagehub.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.garbagehub.data.models.GarbageBin
import com.example.garbagehub.data.repository.BinRepository
import com.example.garbagehub.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val binRepository: BinRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _bins = MutableLiveData<List<GarbageBin>>()
    val bins: LiveData<List<GarbageBin>> = _bins

    private val _selectedBin = MutableLiveData<GarbageBin?>()
    val selectedBin: LiveData<GarbageBin?> = _selectedBin

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    fun loadBins() {
        viewModelScope.launch {
            binRepository.getAllBins().collectLatest { binList ->
                _bins.value = binList
            }
        }
    }

    fun onBinSelected(binId: String) {
        viewModelScope.launch {
            binRepository.getBinById(binId)?.let { bin ->
                _selectedBin.value = bin
            }
        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            locationRepository.getCurrentLocation()?.let { location ->
                _currentLocation.value = location
            }
        }
    }
}
