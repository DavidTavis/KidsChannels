package kids.youtube.channels.model

data class Channel(
        var title: String = "",
        var channelId: String = "",
        var videos: ArrayList<Video>,
        var token: String = ""
)