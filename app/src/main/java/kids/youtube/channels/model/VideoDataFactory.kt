package kids.youtube.channels.model

import kids.youtube.channels.R
import java.util.*

object VideoDataFactory{

    private val random = Random()

    private val titles =  arrayListOf( "Aviator", "Now you can See me", "God Father", "Mission Impossible", "3 idiots")

    private fun randomTitle() : String{
        val index = random.nextInt(titles.size)
        return titles[index]
    }

    private fun randomImage() : String{
        return ""
//        return R.drawable.aviator
    }

    fun getVideo(count : Int) : List<Video>{
        val videos = mutableListOf<Video>()
        repeat(count){
            val video = Video(randomImage(), randomTitle())
            videos.add(video)
        }
        return videos
    }


}