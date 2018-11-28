package kids.youtube.channels.model

import android.util.Log
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kids.youtube.channels.model.dto.YoutubeChannelSearch
import kids.youtube.channels.presenter.Presenter


class ChannelDataFactory(val presenter: Presenter) {

    companion object {
        var channelToken = ""
        var channels = mutableListOf<Channel>()
    }
    private var count = 0
//    private var channels = mutableListOf<Channel>()
    private val channelIds = mutableListOf<Pair<String, String>>()

    fun searchChannel(query: String) {
        getChannels(query, "")
    }

    fun loadMoreChannel(query: String) {
        Log.d("myTag", "channelToken = $channelToken")
        if (!channelToken.equals(""))
            getChannels(query, channelToken)
    }

    private fun getChannels(query: String, pageToken: String) {

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
                        if (count < channelIds.size) {
                            getVideo(channelIds[count].first, channelIds[count].second)
                            count++
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {

                        channelToken = if(youtubeChannelSearch.nextPageToken == null)
                            ""
                        else
                            youtubeChannelSearch.nextPageToken

                        for (i in youtubeChannelSearch.items.indices) {
                            channelIds.add(Pair(youtubeChannelSearch.items[i].snippet.channelTitle, youtubeChannelSearch.items[i].id.channelId))
                        }
                    }
                })
    }

    private fun getVideo(channelTitle: String, channelID: String) {
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
                            getVideo(channelIds[count].first, channelIds[count].second)
                            count++
                        } else
                            presenter.onShow(channels)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        val videos = mutableListOf<Video>()
                        val videoToken = if(youtubeChannelSearch.nextPageToken == null)
                            ""
                        else
                            youtubeChannelSearch.nextPageToken

                        for (i in youtubeChannelSearch.items.indices) {
                            videos.add(Video(youtubeChannelSearch.items.get(i).snippet.thumbnails._default.url, youtubeChannelSearch.items.get(i).snippet.title))
                        }

                        val channel = Channel(channelTitle, channelID, videos, videoToken)

                        channels.add(channel)
                    }
                })
    }

    fun loadMoreVideo(position: Int) {
        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "video")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("channelId",  channels[position].channelId)
                .addQueryParameter("pageToken", channels[position].token)
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeChannelSearch> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onComplete() {
                        presenter.onShow(channels)
                        Log.d("mytag", "onComplete loadMoreVideo")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        val videos = mutableListOf<Video>()

                        val videoToken = if(youtubeChannelSearch.nextPageToken == null)
                            ""
                        else
                            youtubeChannelSearch.nextPageToken

                        for (i in youtubeChannelSearch.items.indices) {
                            videos.add(Video(youtubeChannelSearch.items.get(i).snippet.thumbnails._default.url, youtubeChannelSearch.items.get(i).snippet.title))
                        }

                        (channels[position].videos as ArrayList).addAll(videos)
                        channels[position].token = videoToken
                    }
                })
    }
}