package com.nikola.covernews.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.nikola.covernews.R
import com.nikola.covernews.data.adapter.NewsItems
import com.nikola.covernews.data.model.Article
import com.nikola.covernews.ui.detail.DetailsActivity
import com.nikola.covernews.ui.main.viewmodel.CoverViewModel
import com.nikola.covernews.ui.main.viewmodel.CoverViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.cover_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CoverFragment : Fragment(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {
    override val kodein by closestKodein()
    private val coverViewModelFactory: CoverViewModelFactory by instance()
    private lateinit var viewModel: CoverViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cover_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        refreshCoverItems.setOnRefreshListener(this)
        viewModel =
            ViewModelProviders.of(this, coverViewModelFactory).get(CoverViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = lifecycleScope.launch {
        viewModel.fetchNews()
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer { topHeadlinesResponse ->
            if (topHeadlinesResponse == null) return@Observer
            initRecyclerView(topHeadlinesResponse.articles.toArticleItems())
        })

        viewModel.isLoading().observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                progressBar_loading.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel.isError().observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                networkErrorCases(show)
            }
        })
    }

    override fun onRefresh() {
        bindUI()
        hideErrorTextView()
        refreshCoverItems.isRefreshing = false
    }

    private fun hideErrorTextView() {
        textErrorCover.visibility = View.GONE
    }

    private fun List<Article>.toArticleItems(): List<NewsItems> {
        return this.map {
            NewsItems(it)
        }
    }

    private fun initRecyclerView(items: List<NewsItems>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CoverFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? NewsItems)?.let {
                val title = item.article.title
                val webUrl = item.article.url
                val source = item.article.source.name
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("TitleString", title)
                intent.putExtra("UrlString", webUrl)
                intent.putExtra("SourceString", source)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Search latest news..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query?.length!! > 1) {
                        hideErrorTextView()
                        bindSearchUI(query)
                    } else {
                        val snackbar = Snackbar.make(
                            searchView,
                            "Invalid search! Please try again!",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.show()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun bindSearchUI(keyword: String) = lifecycleScope.launch {
        viewModel.searchNews(keyword)
        viewModel.newsLiveData.observe(viewLifecycleOwner, Observer { topHeadlinesResponse ->
            if (topHeadlinesResponse == null) return@Observer
            initRecyclerView(topHeadlinesResponse.articles.toArticleItems())
        })

        viewModel.isLoading().observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                progressBar_loading.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel.isError().observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                networkErrorCases(show)
            }
        })
    }

    private fun networkErrorCases(show: String) {
        when (show) {
            getString(R.string.resource_forbidden) ->
                textErrorCover.text = getString(R.string.resource_forbidden)
            getString(R.string.server_not_found) ->
                textErrorCover.text = getString(R.string.server_not_found)
            getString(R.string.internal_server_error) ->
                textErrorCover.text = getString(R.string.internal_server_error)
            getString(R.string.bad_gateway) ->
                textErrorCover.text = getString(R.string.bad_gateway)
            getString(R.string.resource_removed) ->
                textErrorCover.text = getString(R.string.resource_removed)
            getString(R.string.removed_resource_found) ->
                textErrorCover.text = getString(R.string.removed_resource_found)
            getString(R.string.network_error) ->
                textErrorCover.text = getString(R.string.network_error)
            getString(R.string.no_internet_connection) ->
                textErrorCover.text = getString(R.string.no_internet_connection)
            getString(R.string.connection_timed_out) ->
                textErrorCover.text = getString(R.string.connection_timed_out)
        }
    }
}