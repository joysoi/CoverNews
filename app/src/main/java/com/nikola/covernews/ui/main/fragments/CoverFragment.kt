package com.nikola.covernews.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.android.synthetic.main.fragment_layout.progressBar_loading
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CoverFragment : Fragment(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {
    override val kodein by closestKodein()
    private val coverViewModelFactory: CoverViewModelFactory by instance()
    private val viewModel: CoverViewModel by viewModels { coverViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshItemsLayout.setOnRefreshListener(this)
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
                textError.text = show
            }
        })
    }

    override fun onRefresh() {
        bindUI()
        hideErrorTextView()
        refreshItemsLayout.isRefreshing = false
    }

    private fun hideErrorTextView() {
        textError.visibility = View.GONE
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

        recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(this@CoverFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? NewsItems)?.let {
                val title = item.article.title
                val webUrl = item.article.url
                val source = item.article.source.name
                Intent(activity, DetailsActivity::class.java).apply {
                    putExtra(getString(R.string.title_string_arg), title)
                    putExtra(getString(R.string.url_string_arg), webUrl)
                    putExtra(getString(R.string.source_string_arg), source)
                    startActivity(this)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query?.length!! > 1) {
                        hideErrorTextView()
                        bindSearchUI(query)
                    } else {
                        val snackbar = Snackbar.make(
                            searchView,
                            getString(R.string.invalid_search_msg),
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
                textError.text = show
            }
        })
    }
}