package com.example.sqllite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sqllite.databinding.ItemHoardBinding
import com.example.sqllite.entity.Hoard

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class HoardsAdapter : ListAdapter<Hoard, HoardsAdapter.ViewHolder>(Util()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoardsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHoardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoardsAdapter.ViewHolder, position: Int) {
        val hoard = getItem(position)
        holder.bind(hoard)
    }

    inner class ViewHolder(private val binding: ItemHoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hoard: Hoard) = with(binding) {
            hoardName.text = hoard.hoardName
            isHoardAccessible.text = hoard.isHoardAccessible.toString()
            isGoldHoarded.text = hoard.goldHoarded.toString()
        }
    }

    class Util : DiffUtil.ItemCallback<Hoard>() {
        override fun areItemsTheSame(oldItem: Hoard, newItem: Hoard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hoard, newItem: Hoard): Boolean {
            return oldItem == newItem
        }
    }
}