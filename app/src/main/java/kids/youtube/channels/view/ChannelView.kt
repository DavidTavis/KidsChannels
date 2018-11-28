package kids.youtube.channels.view

import kids.youtube.channels.model.Channel

interface ChannelView: View {
    fun showChannel(channels: List<Channel>)
    fun showEmptyList()
}