package com.ergea.dicodingstoryapp.ui.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ergea.dicodingstoryapp.data.remote.model.story.ListStoryItem
import com.ergea.dicodingstoryapp.databinding.ItemListHomeBinding
import com.ergea.dicodingstoryapp.ui.homescreen.HomeFragmentDirections

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class StoriesAdapter : PagingDataAdapter<ListStoryItem, StoriesAdapter.ListViewHolder>(callback) {

    companion object {
        val callback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListViewHolder =
        ListViewHolder(
            ItemListHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class ListViewHolder(private val binding: ItemListHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context).load(item.photoUrl).into(photo)
                name.text = item.name
                itemView.setOnClickListener {
                    val directions =
                        HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(item.id.toString())
                    it.findNavController().navigate(directions)
                }
            }
        }
    }

}