package kids.youtube.channels.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kids.youtube.channels.R
import kids.youtube.channels.model.Video
import kotlinx.android.synthetic.main.video.view.*
import com.squareup.picasso.Picasso



class VideoAdapter (private val videos : List<Video>, private val context: Context)
    : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    val mPicasso = Picasso.with(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.video,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
//        holder.imageView.setImageResource(video.image)
        mPicasso.load(video.image).placeholder(R.drawable.aviator).error(R.drawable.aviator).into(holder.imageView)
        holder.textView.text = video.title
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textView : TextView = itemView.video_textView
        val imageView: ImageView = itemView.video_imageView

    }
}