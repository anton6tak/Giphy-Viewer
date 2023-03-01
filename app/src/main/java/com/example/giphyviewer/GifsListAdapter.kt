package com.example.giphyviewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giphyviewer.models.Giph

class GifsListAdapter(private val context: Context) :
    RecyclerView.Adapter<GifsListAdapter.ViewHolder>() {

    var items: List<Giph> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_text_view)
        val gif: ImageView = view.findViewById(R.id.gif)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.gif_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = items[position].title

        Glide
            .with(context)
            .asGif()// replace with 'this' if it's in activity
            .load(items[position].imageData.placeholderImage.url)
            .load(items[position].imageData.gif.url)
            .error(R.drawable.ic_launcher_background) // show error drawable if the image is not a gif
            .into(viewHolder.gif)
    }

    override fun getItemCount() = items.size
}