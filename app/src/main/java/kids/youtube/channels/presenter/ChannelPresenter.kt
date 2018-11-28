package kids.youtube.channels.presenter

import kids.youtube.channels.model.Channel
import kids.youtube.channels.model.ChannelDataFactory
import kids.youtube.channels.view.ChannelView

class ChannelPresenter(val view: ChannelView): Presenter{

    var mLoadingVideo = true

    fun searchChannel(query: String){
        ChannelDataFactory(this).searchChannel(query)
    }

    fun loadMoreChannel(query: String){
        ChannelDataFactory(this).loadMoreChannel(query)
    }

    fun loadMoreVideo(position: Int){
        ChannelDataFactory(this).loadMoreVideo(position)
    }
    override fun onShow(channels : List<Channel>) {
        view.showChannel(channels)
        mLoadingVideo = false
    }

    override fun onStop() {

    }

}