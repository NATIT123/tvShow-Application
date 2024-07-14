package com.example.tvshowsapplication.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.databinding.ItemContainerTvShowBinding

class ShowAdapter(private val mOnClickItemListener: onClickItemListner) :
    RecyclerView.Adapter<ShowAdapter.ShowViewHolder>() {

    inner class ShowViewHolder(val itemContainerTvShowBinding: ItemContainerTvShowBinding) :
        RecyclerView.ViewHolder(itemContainerTvShowBinding.root)


    interface onClickItemListner {
        fun onClickItem(position: Int)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view =
            ItemContainerTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.itemContainerTvShowBinding.tvShow = differ.currentList[position]
        holder.itemContainerTvShowBinding.executePendingBindings()
        holder.itemView.setOnClickListener {
            mOnClickItemListener.onClickItem(position)
        }
    }


}