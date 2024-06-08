package com.example.music.mvvm.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.databinding.SongItemBinding
import com.example.music.mvvm.model.SongModel

class ListSongsAdapter(
    private val onItemClick: (SongModel) -> Unit,
    private val onMoreButtonClick: (song: SongModel, view: View) -> Unit,
): ListAdapter<SongModel, ListSongsAdapter.SongViewHolder>(ListSongsDiffUtilCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder {
        val binding = SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact, onItemClick, onMoreButtonClick)
    }

    class SongViewHolder(private val binding: SongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongModel, onItemClick: (SongModel) -> Unit, onMoreButtonClick: (song: SongModel, view: View) -> Unit) {

            binding.apply {

                if(!song.image.isNullOrEmpty()){
                    val uri = Uri.parse(song.image)
                    Glide.with(binding.root)
                        .load(uri)
                        .into(imageViewSongItem)
                }

                textViewSongName.text = song.title
                textViewArtist.text = song.artist

                val seconds = song.duration?.div(1000)
                val minutes = seconds?.div(60)
                val remainingSeconds = seconds?.rem(60)
                textViewDuration.text = String.format("%02d:%02d", minutes, remainingSeconds)

                itemView.setOnClickListener {
                    onItemClick(song)
                }
                imageButtonMenuMore.setOnClickListener {
                    onMoreButtonClick(song, it)
                }

            }

        }

    }

    class ListSongsDiffUtilCallBack : DiffUtil.ItemCallback<SongModel>() {

        override fun areContentsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(oldItem: SongModel, newItem: SongModel): Boolean {
            return oldItem.id == newItem.id
        }

    }
}