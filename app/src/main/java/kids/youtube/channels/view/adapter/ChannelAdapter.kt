package kids.youtube.channels.view.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kotlinx.android.synthetic.main.channels.view.*

class ChannelAdapter(private var channels : List<Channel>) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>(){

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.channels, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = channels[position]
        holder.textView.text = channel.title
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayout.HORIZONTAL, false)
            adapter = VideoAdapter(channel.video,context)
            recycledViewPool = viewPool
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_video
        val textView: TextView = itemView.textView
    }

    fun setChannelList(channels: List<Channel>){
        this.channels = channels
        notifyDataSetChanged()
    }
}