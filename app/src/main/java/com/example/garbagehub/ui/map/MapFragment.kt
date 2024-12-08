package com.example.garbagehub.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.garbagehub.R
import com.example.garbagehub.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.fabReport.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_report)
        }

        setupObservers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            viewModel.getCurrentLocation()
        } else {
            requestLocationPermission()
        }

        map?.setOnMarkerClickListener { marker ->
            viewModel.onBinSelected(marker.tag as String)
            true
        }

        viewModel.loadBins()
    }

    private fun setupObservers() {
        viewModel.bins.observe(viewLifecycleOwner) { bins ->
            map?.clear()
            bins.forEach { bin ->
                val marker = map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(bin.latitude, bin.longitude))
                        .title("Bin ${bin.id}")
                        .icon(
                            when {
                                bin.currentFillLevel >= 90 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                bin.currentFillLevel >= 75 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                                else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            }
                        )
                )
                marker?.tag = bin.id
            }
        }

        viewModel.selectedBin.observe(viewLifecycleOwner) { bin ->
            if (bin != null) {
                binding.binInfoCard.visibility = View.VISIBLE
                binding.binId.text = "Bin ${bin.id}"
                binding.fillLevel.text = "${bin.currentFillLevel}% Full"
                binding.fillLevelIndicator.progress = bin.currentFillLevel
            } else {
                binding.binInfoCard.visibility = View.GONE
            }
        }

        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            location?.let {
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        15f
                    )
                )
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            onMapReady(map ?: return)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
