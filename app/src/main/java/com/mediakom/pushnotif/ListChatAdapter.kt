package com.mediakom.pushnotif

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mediakom.pushnotif.databinding.ItemChatBinding
import com.mediakom.pushnotif.databinding.ItemPeopleBinding


class ListChatAdapter(private var senderId: String): ListAdapter<ChatModel,
       ListChatAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel, senderId: String) {
            if (senderId == item.senderId){
                binding.tvChat.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            }
            else{
                binding.tvChat.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }
            binding.tvChat.text = item.text
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(
                oldItem: ChatModel,
                newItem: ChatModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatModel, newItem:
                ChatModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, senderId)
        }
    }
}