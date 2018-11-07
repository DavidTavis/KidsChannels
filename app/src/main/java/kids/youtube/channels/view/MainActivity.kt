package kids.youtube.channels.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.androidnetworking.AndroidNetworking
import com.jakewharton.rxbinding.widget.RxTextView
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kids.youtube.channels.presenter.ChannelPresenter
import kids.youtube.channels.view.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ChannelView {

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: ChannelPresenter
    lateinit var etSearch: EditText
    lateinit var query: String
    private var pageToken = ""
    private var mLoading = true
    private var shouldInsertResults = false
    private var mChannels = mutableListOf<Channel>()
    private val mLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
    var channelAdapter = ChannelAdapter(ArrayList<Channel>())

    override fun showChannel(channels: List<Channel>) {
        Log.d("mytag", "MainActivity showChannel")

        if (shouldInsertResults) {
            val startPosition = mChannels.size
            mChannels.addAll(channels)
            channelAdapter.setChannelList(mChannels)
        } else {
            mChannels.clear()
            mChannels.addAll(channels)
            channelAdapter.setChannelList(channels)
        }

        mLoading = false

    }

    override fun setPageToken(pageToken: String) {
        this.pageToken = pageToken
        Log.d("myTag", "pageToken = $pageToken")
    }

    override fun showEmptyList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.initialize(getApplicationContext())

        presenter = ChannelPresenter(this)

        initRecycler()
        etSearch = findViewById(R.id.et_search)

        RxTextView.textChanges(etSearch).filter { s -> s.length > 2 }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { value ->
                    pageToken = ""
                    shouldInsertResults = false
                    presenter.onSearchChannel(value.toString(),pageToken)
                }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    loadMoreItems()
                }

            }
        })
    }

    private fun initRecycler() {
        recyclerView = rv_channels

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = channelAdapter
        }

    }

    private fun loadMoreItems() {
//        if (mTotalPageSize > mCurrentPage) {
        val visibleItemCount = mLayoutManager.getChildCount()
        val totalItemCount = mLayoutManager.getItemCount()
        val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

        if (!mLoading) {
            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                mLoading = true
                shouldInsertResults = true
                presenter.onSearchChannel(etSearch.text.toString(),pageToken)

            }
        }
//        }
    }
}
