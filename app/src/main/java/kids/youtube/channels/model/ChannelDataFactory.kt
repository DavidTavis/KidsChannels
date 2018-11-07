package kids.youtube.channels.model

import android.util.Log
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kids.youtube.channels.model.dto.YoutubeChannelSearch
import kids.youtube.channels.presenter.Presenter
import java.util.*


class ChannelDataFactory(val presenter: Presenter) {

    private var nextPageToken = ""
    var count = 0

    private val titles = arrayListOf("Now Playing", "Classic", "Comedy", "Thriller", "Action", "Horror", "TV Series")

    val channels = mutableListOf<Channel>()
    val channelIds = mutableListOf<Pair<String,String>>()

    fun getYoutubeChannels(query: String, pageToken: String) {

        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("q", query)
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "channel")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("pageToken", pageToken)
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeChannelSearch> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onComplete() {
                        Log.d("myTag", "getYoutubeChannels onComplete; nextPageToken = $nextPageToken")
                        if (count < channelIds.size) {
                            getYoutubeVideo(channelIds[count].first, channelIds[count].second)
                            count++
                        }
                        presenter.onSetPageToken(nextPageToken)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        nextPageToken = youtubeChannelSearch.nextPageToken
                        for (i in youtubeChannelSearch.items.indices) {
                            channelIds.add(Pair(youtubeChannelSearch.items[i].snippet.channelTitle, youtubeChannelSearch.items[i].id.channelId))
                        }
                    }
                })
    }

    fun getYoutubeVideo(channelTitle: String, channelID: String) {
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
                    }

                    override fun onComplete() {
                        if (count < channelIds.size) {
                            getYoutubeVideo(channelIds[count].first, channelIds[count].second)
                            count++
                        } else
                            presenter.onShow(channels)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        val videos = mutableListOf<Video>()

                        for (i in youtubeChannelSearch.items.indices) {
                            videos.add(Video(youtubeChannelSearch.items.get(i).snippet.thumbnails._default.url, youtubeChannelSearch.items.get(i).snippet.title))
                        }

                        val channel = Channel(channelTitle, channelID, videos)

                        channels.add(channel)
                    }
                })
    }

}