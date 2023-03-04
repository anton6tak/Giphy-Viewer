package com.example.giphyviewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.giphyviewer.databinding.GifItemBinding
import com.example.giphyviewer.databinding.LoadingItemBinding
import com.example.giphyviewer.view.GiphyListViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

class GifsListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<GiphyListViewModel.UnitItem> = emptyList()
        set(value) {
            field = value
        }

    class GifViewHolder(binding: GifItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.titleTextView
        val gifImage: ImageView = binding.gifImage
    }

    class LoadingViewHolder(binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val indicator: CircularProgressIndicator = binding.progressIndicator
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is GiphyListViewModel.UnitItem.GifUnit) 0 else 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding =
                    GifItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                GifViewHolder(binding)
            }
            else -> {
                val binding = LoadingItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context), viewGroup, false
                )
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> {
                viewHolder as GifViewHolder
                val item: GiphyListViewModel.UnitItem.GifUnit =
                    items[position] as GiphyListViewModel.UnitItem.GifUnit

                viewHolder.title.text = item.gif.title

                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 10f
                circularProgressDrawable.centerRadius = 90f
                circularProgressDrawable.start()

                Glide.with(context).asGif().load(item.gif.url)
                    .error(R.drawable.ic_launcher_background).placeholder(circularProgressDrawable)
                    .into(viewHolder.gifImage)
            }
            1 -> {
                viewHolder as LoadingViewHolder
                viewHolder.indicator.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount() = items.size
}