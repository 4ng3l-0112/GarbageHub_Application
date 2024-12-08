package com.example.garbagehub.ui.admin.bins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagehub.data.models.GarbageBin
import com.example.garbagehub.databinding.ItemBinBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BinAdapter(
    private val onBinClick: (GarbageBin) -> Unit
) : ListAdapter<GarbageBin, BinAdapter.BinViewHolder>(BinDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinViewHolder {
        val binding = ItemBinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BinViewHolder(binding, onBinClick)
    }

    override fun onBindViewHolder(holder: BinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BinViewHolder(
        private val binding: ItemBinBinding,
        private val onBinClick: (GarbageBin) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

        fun bind(bin: GarbageBin) {
            binding.apply {
                binId.text = "Bin ${bin.id}"
                fillLevel.text = "${bin.currentFillLevel}% Full"
                lastUpdated.text = "Last updated: ${dateFormat.format(bin.lastUpdated)}"
                status.text = bin.status.name
                fillLevelIndicator.progress = bin.currentFillLevel

                root.setOnClickListener { onBinClick(bin) }
            }
        }
    }

    private class BinDiffCallback : DiffUtil.ItemCallback<GarbageBin>() {
        override fun areItemsTheSame(oldItem: GarbageBin, newItem: GarbageBin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GarbageBin, newItem: GarbageBin): Boolean {
            return oldItem == newItem
        }
    }
}
