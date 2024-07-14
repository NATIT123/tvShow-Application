package com.example.tvshowsapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapplication.databinding.ItemContainerSliderImageBinding

class SliderImageView(private val listImage: List<String>) :
    RecyclerView.Adapter<SliderImageView.ImageViewHolder>() {
    inner class ImageViewHolder(val itemContainerSliderImageView: ItemContainerSliderImageBinding) :
        RecyclerView.ViewHolder(itemContainerSliderImageView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ItemContainerSliderImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.itemContainerSliderImageView.imageURL = listImage[position]
        holder.itemContainerSliderImageView.executePendingBindings()
    }


}