package com.example.broadcastapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.broadcastapp.databinding.ContactsItemBinding

class ContactsAdapter : ListAdapter<ContactModel, ContactsAdapter.ViewHolder>(DiffCallback()) {
    private var lastPosition = -1

    class DiffCallback : DiffUtil.ItemCallback<ContactModel>() {
        override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        if (holder.adapterPosition > lastPosition) {
            val animation = AnimationUtils.loadAnimation(holder.context, R.anim.slide_enter_anim)
            holder.itemView.startAnimation(animation)
            lastPosition = holder.adapterPosition
        }
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(private val binding: ContactsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context : Context = binding.root.context

        fun bind(model: ContactModel) {
            binding.name.text = model.name
            if (model.photo != null)
                binding.icon.setImageBitmap(model.photo)
        }
    }
}