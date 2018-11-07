package kids.youtube.channels.presenter

import kids.youtube.channels.model.Channel
import kids.youtube.channels.model.ChannelDataFactory
import kids.youtube.channels.view.ChannelView

class ChannelPresenter(val view: ChannelView): Presenter{
    override fun onSetPageToken(pageToken: String) {
        view.setPageToken(pageToken)
    }

    override fun onShow(channels : List<Channel>) {
        view.showChannel(channels)
    }

    fun onSearchChannel(query: String, pageToken: String){
        ChannelDataFactory(this).getYoutubeChannels(query, pageToken)
    }
    override fun onStop() {

    }

}