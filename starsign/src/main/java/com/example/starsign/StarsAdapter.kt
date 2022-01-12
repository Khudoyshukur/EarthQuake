package com.example.starsign

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starsign.databinding.ItemStarBinding

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class StarsAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<String, StarsAdapter.ViewHolder>(Util()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStarBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StarsAdapter.ViewHolder, position: Int) {
        val star = getItem(position)
        holder.bindData(star)
    }

    inner class ViewHolder(private val binding: ItemStarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(star: String) {
            binding.star.text = star

            binding.root.setOnClickListener {
                onItemClicked.invoke(star)
            }
        }
    }

    class Util : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}