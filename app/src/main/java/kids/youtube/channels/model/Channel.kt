package kids.youtube.channels.model

data class Channel(
        val title : String = "",
        val channelId : String = "",
        val video : List<Video>
)