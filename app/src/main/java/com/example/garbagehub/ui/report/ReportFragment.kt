package com.example.garbagehub.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.garbagehub.R
import com.example.garbagehub.data.models.ReportType
import com.example.garbagehub.databinding.FragmentReportIssueBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding: FragmentReportIssueBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupReportTypeDropdown()
        setupSubmitButton()
        observeViewModel()
    }

    private fun setupReportTypeDropdown() {
        val reportTypes = ReportType.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, reportTypes)
        binding.reportType.setAdapter(adapter)
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            val reportType = binding.reportType.text.toString()
            val description = binding.description.text.toString()

            if (reportType.isNotEmpty() && description.isNotEmpty()) {
                viewModel.submitReport(
                    ReportType.valueOf(reportType),
                    description
                )
            } else {
                Snackbar.make(
                    binding.root,
                    "Please fill in all fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.reportSubmitted.observe(viewLifecycleOwner) { success ->
            if (success) {
                Snackbar.make(
                    binding.root,
                    "Report submitted successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
