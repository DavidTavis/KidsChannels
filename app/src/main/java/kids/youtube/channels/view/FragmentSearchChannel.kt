package kids.youtube.channels.view

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.jakewharton.rxbinding.widget.RxTextView
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kids.youtube.channels.presenter.ChannelPresenter
import kids.youtube.channels.view.adapter.ChannelAdapter
import java.util.concurrent.TimeUnit

class FragmentSearchChannel : Fragment(), ChannelView{

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: ChannelPresenter
    lateinit var channelAdapter: ChannelAdapter
    lateinit var etSearch: EditText
    lateinit var mPrgLoading: CircleProgressBar
    private var mLoading = true
    private var shouldInsertResults = false
    private var mChannels = mutableListOf<Channel>()
    private val mLayoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    companion object {
        fun newInstance() : FragmentSearchChannel {
            return FragmentSearchChannel()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_channel, container, false)
        presenter = ChannelPresenter(this)
        channelAdapter = ChannelAdapter(ArrayList(), presenter, activity as MainActivity)
        initView(view)
        return view
    }

    private fun initView(view: View) {

        initRecycler(view)
        initEtSearch(view)

        mPrgLoading = view.findViewById(R.id.prgLoading)
        mPrgLoading.setColorSchemeResources(R.color.colorAccent)

    }

    private fun initEtSearch(view: View) {
        etSearch = view.findViewById(R.id.et_search)
        RxTextView.textChanges(etSearch)
//                .filter { s -> s.length > 2 }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { value ->
                    if(value.isEmpty()){
                        activity!!.runOnUiThread { showEmptyList() }
                    }else {
                        activity!!.runOnUiThread { showProgress() }
                        shouldInsertResults = false
                        presenter.searchChannel(value.toString())
                    }
                }
    }

    private fun initRecycler(view: View) {
        recyclerView = view.findViewById(R.id.rv_channels)!!

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