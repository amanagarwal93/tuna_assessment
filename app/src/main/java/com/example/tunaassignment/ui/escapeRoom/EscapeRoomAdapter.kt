package com.example.tunaassignment.ui.escapeRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tunaassignment.data.listeners.RoomClick
import com.example.tunaassignment.data.model.response.EscapeRoomsMovy
import com.example.tunaassignment.databinding.MovieItemBinding

class EscapeRoomAdapter(
    private val moviesList: List<EscapeRoomsMovy>,
    private val itemClick: RoomClick
) :
    RecyclerView.Adapter<EscapeRoomAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie: EscapeRoomsMovy = moviesList[position]
        holder.bind(movie, itemClick, position)
    }

    override fun getItemCount(): Int = moviesList.size

    class MyViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(escapeRoom: EscapeRoomsMovy, roomClick: RoomClick, position: Int) {
            binding.apply {
                textView.text = escapeRoom.Title
                imageView.load(escapeRoom.image_url)
                root.setOnClickListener { roomClick.roomClick(position) }
            }
        }
    }
}
