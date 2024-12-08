package com.example.garbagehub.ui.admin.bins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garbagehub.databinding.FragmentBinManagementBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BinManagementFragment : Fragment() {

    private var _binding: FragmentBinManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BinManagementViewModel by viewModels()
    private lateinit var binAdapter: BinAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChart()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binAdapter = BinAdapter(
            onBinClick = { bin ->
                // Navigate to bin details
            }
        )
        binding.binsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = binAdapter
        }
    }

    private fun setupChart() {
        binding.fillLevelChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
        }
    }

    private fun setupFab() {
        binding.fabAddBin.setOnClickListener {
            // Show add bin dialog
        }
    }

    private fun observeViewModel() {
        viewModel.bins.observe(viewLifecycleOwner) { bins ->
            binAdapter.submitList(bins)
            updateChart(bins.map { 
                Entry(it.lastUpdated.time.toFloat(), it.currentFillLevel.toFloat())
            })
        }
    }

    private fun updateChart(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Fill Levels").apply {
            setDrawValues(false)
            setDrawCircles(false)
            lineWidth = 2f
        }
        binding.fillLevelChart.data = LineData(dataSet)
        binding.fillLevelChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
