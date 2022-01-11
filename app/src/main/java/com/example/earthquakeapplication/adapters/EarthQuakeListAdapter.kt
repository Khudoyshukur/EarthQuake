package com.example.earthquakeapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquakeapplication.databinding.ItemEarthquakeBinding
import com.example.earthquakeapplication.model.EarthQuake
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by: androdev
 * Date: 9/26/2021
 * Time: 5:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class EarthQuakeListAdapter : ListAdapter<EarthQuake, EarthQuakeListAdapter.ViewHolder>(Util()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEarthquakeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val earthQuake = getItem(position)
        holder.bindData(earthQuake)
    }

    inner class ViewHolder(private val binding: ItemEarthquakeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(earthQuake: EarthQuake) {
            binding.date.text = TIME_FORMAT.format(earthQuake.mDate)
            binding.details.text = earthQuake.toString()
            binding.magnitude.text = MAGNITUDE_FORMAT.format(earthQuake.mMagnitude)
        }
    }

    class Util : DiffUtil.ItemCallback<EarthQuake>() {
        override fun areItemsTheSame(oldItem: EarthQuake, newItem: EarthQuake) =
            oldItem.mId == newItem.mId

        override fun areContentsTheSame(oldItem: EarthQuake, newItem: EarthQuake) =
            oldItem == newItem
    }

    companion object {
        private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.US)
        private val MAGNITUDE_FORMAT: NumberFormat = DecimalFormat("0.0")
    }
}