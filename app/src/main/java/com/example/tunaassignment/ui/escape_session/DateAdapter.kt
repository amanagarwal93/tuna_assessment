package com.example.tunaassignment.ui.escape_session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tunaassignment.R
import com.example.tunaassignment.data.listeners.RoomClick
import com.example.tunaassignment.data.model.Month
import com.example.tunaassignment.databinding.DateItemBinding

class DateAdapter(private val months: List<Month>, private val itemClick: RoomClick) :
    RecyclerView.Adapter<DateAdapter.MyViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val month: Month = months[position]
        if (position == selectedItemPosition) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.shade_green
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_gray
                )
            )
        }
        holder.bind(month, itemClick, position)
    }

    override fun getItemCount(): Int = months.size

    class MyViewHolder(private val binding: DateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(month: Month, roomClick: RoomClick, position: Int) {
            binding.apply {
                day.text = month.date
                date.text = "${month.month} ${month.day}"
                root.setOnClickListener {
                    roomClick.roomClick(position)
                }


            }
        }
    }

    fun setSelectedItem(position: Int) {
        val previousSelectedPosition = selectedItemPosition
        selectedItemPosition = position

        // Notify the item change for both the previously selected and newly selected items
        notifyItemChanged(previousSelectedPosition)
        notifyItemChanged(position)
    }
}
