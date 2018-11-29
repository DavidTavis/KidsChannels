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
        var newMoreChannels = mutableListOf<Channel>()
    }

    var loadMoreChannels = false
    private var count = 0
    private val channelIds = mutableListOf<Pair<String, String>>()

    fun searchChannel(query: String) {
        channels.clear()
        getChannels(query, "")
    }

    fun loadMoreChannel(query: String) {
        if (!channelToken.equals("")) {
            count = 0
            newMoreChannels.clear()
            getChannels(query, channelToken)
        }
    }

    private fun getChannels(query: String, pageToken: String) {

        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("q", query)
                .addQueryParameter("maxResult", "5")
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
//                        presenter.onShowProgress()
                    }

                    override fun onComplete() {
                        if (channelIds.size > 0) {
                            getVideo(channelIds[count].first, channelIds[count].second)
                            count++
                        } else {
//                            presenter.onHideProgress()
                            presenter.onShowEmptyList()
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {

                        channelToken = if (youtubeChannelSearch.nextPageToken == null)
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
                .addQueryParameter("maxResult", "5")
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
//                            presenter.onHideProgress()
                            if (!loadMoreChannels) {
                                presenter.onShow(channels)
                            } else {
                                presenter.onShowMoreChannel(newMoreChannels)
                            }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {
                        val videos = mutableListOf<Video>()
                        val videoToken = if (youtubeChannelSearch.nextPageToken == null)
                            ""
                        else
                            youtubeChannelSearch.nextPageToken

                        for (i in youtubeChannelSearch.items.indices) {
                            videos.add(Video(youtubeChannelSearch.items.get(i).snippet.thumbnails._default.url, youtubeChannelSearch.items.get(i).snippet.title))
                        }

                        val channel = Channel(channelTitle, channelID, videos, videoToken)

                        if (loadMoreChannels) {
                            newMoreChannels.add(channel)
                        } else
                            channels.add(channel)
                    }
                })
    }

    fun getMoreVideo(position: Int) {
        val videos = mutableListOf<Video>()
        Rx2AndroidNetworking.get("https://www.googleapis.com/youtube/v3/search")
                .addQueryParameter("maxResult", "25")
                .addQueryParameter("part", "snippet")
                .addQueryParameter("type", "video")
                .addQueryParameter("key", "AIzaSyAzy4gma2A2I9iqOPwBzgkEr_3_5-5cz5I")
                .addQueryParameter("channelId", channels[position].channelId)
                .addQueryParameter("pageToken", channels[position].token)
                .build()
                .getObjectObservable(YoutubeChannelSearch::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<YoutubeChannelSearch> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onComplete() {
                        presenter.onShowMoreVideo(videos)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mytag", e.toString())
                    }

                    override fun onNext(youtubeChannelSearch: YoutubeChannelSearch) {


                        val videoToken = if (youtubeChannelSearch.nextPageToken == null)
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