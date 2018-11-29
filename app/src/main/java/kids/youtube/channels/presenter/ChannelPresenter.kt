package kids.youtube.channels.presenter

import kids.youtube.channels.model.Channel
import kids.youtube.channels.model.ChannelDataFactory
import kids.youtube.channels.model.Video
import kids.youtube.channels.view.ChannelView
import kids.youtube.channels.view.adapter.ChannelAdapter

class ChannelPresenter(val view: ChannelView): Presenter{

    var mLoadingVideo = true
    lateinit var adapter: ChannelAdapter

    fun searchChannel(query: String){
        var factory = ChannelDataFactory(this)
        factory.loadMoreChannels = false
        factory .searchChannel(query)
    }

    fun loadMoreChannel(query: String){
        var factory = ChannelDataFactory(this)
        factory.loadMoreChannels = true
        factory .loadMoreChannel(query)
    }

//    fun loadMoreVideo(position: Int){
//        ChannelDataFactory(this).loadMoreVideo(position)
//    }

    fun getMoreVideo(position: Int, adapter: ChannelAdapter){
        this.adapter = adapter
        ChannelDataFactory(this).getMoreVideo(position)
    }

    override fun onShowMoreVideo(videos: List<Video>) {
        adapter.insertMoreVideo(videos)
        mLoadingVideo = false
    }

    override fun onShowMoreChannel(channels: List<Channel>) {
        view.showMoreChannel(channels)
    }

    override fun onShow(channels : List<Channel>) {
        view.showChannel(channels)
        mLoadingVideo = false
    }

    override fun onShowEmptyList() {
        view.showEmptyList()
    }
    override fun onShowProgress() {
        view.showProgress()
    }

    override fun onHideProgress() {
        view.hideProgress()
    }

    override fun onStop() {

    }

}