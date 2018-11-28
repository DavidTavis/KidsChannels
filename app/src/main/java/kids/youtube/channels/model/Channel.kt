package kids.youtube.channels.model

data class Channel(
        var title: String = "",
        var channelId: String = "",
        var videos: List<Video>,
        var token: String = ""
)