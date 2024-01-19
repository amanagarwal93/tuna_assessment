package com.example.tunaassignment.ui.escape_session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tunaassignment.data.model.response.Session
import com.example.tunaassignment.databinding.TimeItemBinding

class TimeAdapter(private var sessionList: List<String>) :
    RecyclerView.Adapter<TimeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            TimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = sessionList
        holder.bind(sessionList[position])
    }

    override fun getItemCount(): Int = sessionList.size
    fun updateData(data: List<String>) {
        sessionList = data
        notifyDataSetChanged()
    }

    class MyViewHolder(private val binding: TimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(showTime: String) {
            binding.apply {
                timeText.text = showTime
            }
        }
    }
}

