package com.example.tvshowsapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapplication.databinding.ItemContainerTvShowFavoriteBinding

import com.example.tvshowsapplication.models.TvShow

class FavoriteShowAdapter(private val mOnClickItemListener: onClickItemListner) :
    RecyclerView.Adapter<FavoriteShowAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val itemContainerFavoriteShowAdapter: ItemContainerTvShowFavoriteBinding) :
        RecyclerView.ViewHolder(itemContainerFavoriteShowAdapter.root)


    interface onClickItemListner {
        fun onClickItem(position: Int)

        fun deleteItem(position: Int)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemContainerTvShowFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val tvShow = differ.currentList[position]
        holder.itemContainerFavoriteShowAdapter.tvShow = tvShow
        holder.itemContainerFavoriteShowAdapter.executePendingBindings()
        holder.itemView.setOnClickListener {
            mOnClickItemListener.onClickItem(position)
        }
        holder.itemContainerFavoriteShowAdapter.btnDelete.setOnClickListener {
            mOnClickItemListener.deleteItem(position)
        }
    }
}