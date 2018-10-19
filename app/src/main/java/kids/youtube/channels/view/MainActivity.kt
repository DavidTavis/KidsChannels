package kids.youtube.channels.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import com.androidnetworking.AndroidNetworking
import com.jakewharton.rxbinding.widget.RxTextView
import kids.youtube.channels.R
import kids.youtube.channels.model.Channel
import kids.youtube.channels.presenter.ChannelPresenter
import kids.youtube.channels.view.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import android.icu.util.ULocale.getCountry




class MainActivity : AppCompatActivity(), ChannelView {

    lateinit var recyclerView: RecyclerView
    lateinit var presenter: ChannelPresenter
    lateinit var etSearch: EditText
    var channelAdapter = ChannelAdapter(ArrayList<Channel>())

    override fun showChannel(channels: List<Channel>) {
        Log.d("mytag", "MainActivity showChannel")
        channelAdapter.setChannelList(channels)
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

//        recyclerView = rv_channels
        initRecycler()
        etSearch = findViewById(R.id.et_search)

        val obs = RxTextView.textChanges(etSearch)
                .filter { charSequence -> charSequence.length > 3 }
                .debounce(300, TimeUnit.MILLISECONDS)
                .map<String> { charSequence -> charSequence.toString() }

        obs.subscribe { string ->
            Log.d("mytag", "debounced $string")
            presenter.onSearchChannel(string)
        }
//        presenter.onSearchChannel("android")

    }

    private fun initRecycler(){
        recyclerView = rv_channels

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.VERTICAL, false)
            adapter = channelAdapter
        }

    }

}
