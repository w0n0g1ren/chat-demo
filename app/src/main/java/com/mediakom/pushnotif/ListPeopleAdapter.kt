package com.mediakom.pushnotif

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mediakom.pushnotif.databinding.ItemPeopleBinding

interface OnClick{
    fun onClick(position: Int)
}

class ListPeopleAdapter(private var senderId: String, private val listener: OnClick): ListAdapter<UserModel,
       ListPeopleAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemPeopleBinding, private val listener: OnClick) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserModel, senderId: String) {
            if (senderId == item.userId){
                binding.root.visibility = View.GONE
            }
            else{
                binding.tvName.text = item.nama
                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, MainActivity::class.java)
                    intent.putExtra("senderId",senderId)
                    intent.putExtra("destinationId",item.userId)
                    binding.root.context.startActivity(intent)
                    listener.onClick(adapterPosition)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(
                oldItem: UserModel,
                newItem: UserModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserModel, newItem:
                UserModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPeopleBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, senderId)
        }
    }
}