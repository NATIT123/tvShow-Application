package com.example.tvshowsapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapplication.databinding.LayoutItemBottomSheetShowBinding
import com.example.tvshowsapplication.models.Episode

class BottomSheetShowAdapter :
    RecyclerView.Adapter<BottomSheetShowAdapter.BottomSheetViewHolder>() {

    inner class BottomSheetViewHolder(val layoutItemBottomSheetShowBinding: LayoutItemBottomSheetShowBinding) :
        RecyclerView.ViewHolder(layoutItemBottomSheetShowBinding.root)


    private val diffUtil = object : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.air_date == newItem.air_date
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view = LayoutItemBottomSheetShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val episode = differ.currentList[position]
        holder.layoutItemBottomSheetShowBinding.episode = episode
        holder.layoutItemBottomSheetShowBinding.title = "S${
            episode.season.toString().padStart(2, '0')
        } ${episode.episode.toString().padStart(2, '0')}"
        holder.layoutItemBottomSheetShowBinding.executePendingBindings()
    }
}