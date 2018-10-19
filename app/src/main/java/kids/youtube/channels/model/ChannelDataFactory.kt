package kids.youtube.channels.model

import android.app.Activity
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.rx2androidnetworking.Rx2AndroidNetworking
import java.util.*
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kids.youtube.channels.model.dto.YoutubeChannelSearch
import kids.youtube.channels.model.dto.YoutubeVideoData
import kids.youtube.channels.presenter.Presenter
import org.json.JSONObject
import kotlin.collections.ArrayList


class ChannelDataFactory(val presenter: Presenter) {

    private val random = Random()

    private val titles = arrayListOf("Now Playing", "Classic", "Comedy", "Thriller", "Action", "Horror", "TV Series")

    val channels = mutableListOf<Channel>()
    lateinit var channelsID: ArrayList<String>

    private fun randomTitle(): String {
        val index = random.nextInt(titles.size)
        return titles[index]
    }

    private fun randomVideo(): List<Video> {
        return VideoDataFactory.getVideo(4)
    }

    fun getANVideoDataRx(playlistId: String, pageToken: String) {
        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/playlistItems?playlistId=PLdWvFCOAvyr1qYhgPz_-wnCcxTO7VHdFo&key=AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I&part=snippet,id")
                .addQueryParameter("playlistId", playlistId)
                .addQueryParameter("pageToken", pageToken)
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("part", "snippet,id")
                .build()
                .getObjectObservable(YoutubeVideoData::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeVideoData> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("mytag", "onSubscribe")
                    }

                    override fun onComplete() {
                        Log.d("mytag", "onComplete")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeVideoData: YoutubeVideoData) {
                        Log.d("mytag", youtubeVideoData.toString())
                        Log.d("mytag", youtubeVideoData.nextPageToken)
                        Log.d("mytag", youtubeVideoData.items.toString())
                    }
                })

    }

    fun getANVideoData(playlistId: String, pageToken: String) {
        return AndroidNetworking.get("https://www.googleapis.com/youtube/v3/playlistItems?playlistId=PLdWvFCOAvyr1qYhgPz_-wnCcxTO7VHdFo&key=AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I&part=snippet,id")
                .addQueryParameter("playlistId", playlistId)
                .addQueryParameter("pageToken", pageToken)
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("part", "snippet,id")
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        Log.d("mytag", "onResponse" + response.toString())
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        Log.d("mytag", "onError" + error.toString())
                    }
                })


    }

//    fun getChannels(count: Int): List<Channel> {
//        val channels = mutableListOf<Channel>()
//        repeat(count) {
//            val channel = Channel(randomTitle(), randomVideo())
//            channels.add(channel)
//        }
//        return channels
//    }

    fun getYoutubeChannelsRX(query: String) {

        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("q", query)
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "channel")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeChannelSearch> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("mytag", "channel onSubscribe")
                    }

                    override fun onComplete() {
                        Log.d("mytag", "channel onComplete")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        Log.d("mytag", "channel onNext")
//                        val channels = mutableListOf<Channel>()
                        for (i in youtubeChannelSearch.items.indices) {

//                            val channel = Channel(youtubeChannelSearch.items.get(i).snippet.channelTitle, youtubeChannelSearch.items[i].id.channelId, randomVideo())
//                            channels.add(channel)
//                            Log.d("mytag", channel.toString())
//                            channelsID.add(youtubeChannelSearch.items[i].id.channelId)

                            getYoutubeVideoRx(youtubeChannelSearch.items.get(i).snippet.channelTitle, youtubeChannelSearch.items[i].id.channelId)

                        }

                        presenter.onShow(channels)
                    }
                })
    }

    fun getYoutubeVideoRx(channelTitle: String, channelID: String){
        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "video")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("channelId", channelID)
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeChannelSearch> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("mytag", "Video onSubscribe")
                    }

                    override fun onComplete() {
                        Log.d("mytag", "Video onComplete")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        Log.d("mytag", "Video onNext")
                        val videos = mutableListOf<Video>()

                        for (i in youtubeChannelSearch.items.indices) {
                            videos.add(Video(youtubeChannelSearch.items.get(i).snippet.thumbnails._default.url,youtubeChannelSearch.items.get(i).snippet.title))
                        }

                        val channel = Channel(channelTitle, channelID, videos)

                        channels.add(channel)
//                        Log.d("mytag", channel.toString())
                    }
                })
    }

    fun getData(query: String){
//        getChannelObservable(query)
//                .flatMap(Function {  })
    }

    fun getChannelObservable(query: String): Observable<YoutubeChannelSearch>{
        return Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("q", query)
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "channel")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
    }

    fun getVideoObservable(channelTitle: String, channelID: String): Observable<YoutubeChannelSearch>{
        return Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "video")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("channelId", channelID)
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
    }
}