package com.nikola.covernews.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nikola.covernews.R
import com.nikola.covernews.data.adapter.CATEGORY
import com.nikola.covernews.data.adapter.NewsItems
import com.nikola.covernews.data.model.Article
import com.nikola.covernews.ui.detail.DetailsActivity
import com.nikola.covernews.ui.main.viewmodel.SharedViewModel
import com.nikola.covernews.ui.main.viewmodel.SharedViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.android.synthetic.main.fragment_layout.progressBar_loading
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BusinessFragment : Fragment(), KodeinAware,
    SwipeRefreshLayout.OnRefreshListener {

    override val kodein by closestKodein()
    private val sharedViewModelFactory: SharedViewModelFactory by instance()

    private val viewModel: SharedViewModel by viewModels {
        sharedViewModelFactory
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): BusinessFragment = BusinessFragment().apply {
            arguments = bundle
        }
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
        bindUI(requireArguments().getInt(CATEGORY))
    }

    private fun bindUI(categoryPosition: Int) = lifecycleScope.launch {
        viewModel.newsCategory(categoryPosition)

        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer { topHeadlinesResponse ->
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
            layoutManager = LinearLayoutManager(this@BusinessFragment.context)
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

    override fun onRefresh() {
        bindUI(requireArguments().getInt(CATEGORY))
        hideErrorTextView()
        refreshItemsLayout.isRefreshing = false
    }

    private fun hideErrorTextView() {
        textError.visibility = View.GONE
    }
}