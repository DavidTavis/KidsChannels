package kids.youtube.channels.view.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kids.youtube.channels.model.Video
import kids.youtube.channels.presenter.ChannelPresenter
import kotlinx.android.synthetic.main.channels.view.*

class ChannelAdapter(var channels: List<Channel>, private var presenter: ChannelPresenter) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    lateinit var currentVideoAdapter: VideoAdapter

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
            adapter = VideoAdapter(channel.videos, context)
            recycledViewPool = viewPool
        }
        holder.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dx > 0) {
                    //check for scroll right
                    loadMoreVideo(holder, position)
                }
            }
        })
    }

    private fun loadMoreVideo(holder: ChannelAdapter.ViewHolder, position: Int) {
        val layoutManager = holder.recyclerView.getLayoutManager() as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        currentVideoAdapter = holder.recyclerView.adapter as VideoAdapter

        if (!presenter.mLoadingVideo) {
            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                presenter.mLoadingVideo = true
                presenter.getMoreVideo(position, this)
            }
        }
    }

    fun insertMoreVideo(videos: List<Video>) {
        val startPosition = currentVideoAdapter.videos.size
        (currentVideoAdapter.videos as ArrayList).addAll(videos)
        currentVideoAdapter.notifyItemRangeInserted(startPosition, videos.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.rv_video
        val textView: TextView = itemView.textView
    }

    fun setChannelList(channels: List<Channel>) {
        this.channels = channels
        notifyDataSetChanged()
    }
}