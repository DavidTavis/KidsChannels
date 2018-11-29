package kids.youtube.channels.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.androidnetworking.AndroidNetworking
import com.jakewharton.rxbinding.widget.RxTextView
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kids.youtube.channels.presenter.ChannelPresenter
import kids.youtube.channels.view.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ChannelView {

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: ChannelPresenter
    lateinit var channelAdapter: ChannelAdapter
    lateinit var etSearch: EditText
    lateinit var mPrgLoading: CircleProgressBar
    private var mLoading = true
    private var shouldInsertResults = false
    private var mChannels = mutableListOf<Channel>()
    private val mLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.initialize(getApplicationContext())

        presenter = ChannelPresenter(this)
        channelAdapter = ChannelAdapter(ArrayList(), presenter)
        initView()

    }

    private fun initView() {

        initRecycler()
        initEtSearch()

        mPrgLoading = findViewById(R.id.prgLoading)
        mPrgLoading.setColorSchemeResources(R.color.colorAccent)

    }

    private fun initEtSearch() {
        etSearch = findViewById(R.id.et_search)
        RxTextView.textChanges(etSearch)
//                .filter { s -> s.length > 2 }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { value ->
                    if(value.isEmpty()){
                        runOnUiThread { showEmptyList() }
                    }else {
                        runOnUiThread { showProgress() }
                        shouldInsertResults = false
                        presenter.searchChannel(value.toString())
                    }
                }
    }

    private fun initRecycler() {
        recyclerView = rv_channels

        recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = channelAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    loadMoreChannel()
                }
            }
        })

    }

    private fun loadMoreChannel() {
        val visibleItemCount = mLayoutManager.childCount
        val totalItemCount = mLayoutManager.itemCount
        val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()

        if (!mLoading) {
            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                showProgress()
                mLoading = true
                shouldInsertResults = true
                presenter.loadMoreChannel(etSearch.text.toString())
            }
        }
    }

    override fun showChannel(channels: List<Channel>) {
        hideProgress()
        mChannels.clear()
        mChannels.addAll(channels)
        channelAdapter.setChannelList(channels)

        mLoading = false

    }

    override fun showMoreChannel(channels: List<Channel>) {
        val startPosition = channelAdapter.channels.size
        hideProgress()
        (channelAdapter.channels as ArrayList).addAll(channels)
        channelAdapter.notifyItemRangeInserted(startPosition, channelAdapter.channels.size)

        mLoading = false
    }

    override fun showEmptyList() {
        hideProgress()
        mChannels.clear()
        channelAdapter.setChannelList(mChannels)
    }

    override fun showProgress() {
        mPrgLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        mPrgLoading.visibility = View.GONE
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
