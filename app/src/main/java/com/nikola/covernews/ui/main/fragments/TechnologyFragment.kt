package com.nikola.covernews.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nikola.covernews.R
import com.nikola.covernews.data.adapter.NewsItems
import com.nikola.covernews.data.model.Article
import com.nikola.covernews.ui.detail.DetailsActivity
import com.nikola.covernews.ui.main.viewmodel.SharedViewModel
import com.nikola.covernews.ui.main.viewmodel.SharedViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.technology_fragment.*
import kotlinx.android.synthetic.main.technology_fragment.progressBar_loading
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TechnologyFragment : Fragment(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {

    override val kodein by closestKodein()
    private val sharedViewModelFactory: SharedViewModelFactory by instance()

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.technology_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        refreshTechnologyItems.setOnRefreshListener(this)
        viewModel =
            ViewModelProviders.of(this, sharedViewModelFactory).get(SharedViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = lifecycleScope.launch {
        viewModel.technologyCategory()
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
                when (show) {
                    getString(R.string.resource_forbidden) ->
                        textErrorTechnology.text = getString(R.string.resource_forbidden)
                    getString(R.string.server_not_found) ->
                        textErrorTechnology.text = getString(R.string.server_not_found)
                    getString(R.string.internal_server_error) ->
                        textErrorTechnology.text = getString(R.string.internal_server_error)
                    getString(R.string.bad_gateway) ->
                        textErrorTechnology.text = getString(R.string.bad_gateway)
                    getString(R.string.resource_removed) ->
                        textErrorTechnology.text = getString(R.string.resource_removed)
                    getString(R.string.removed_resource_found) ->
                        textErrorTechnology.text = getString(R.string.removed_resource_found)
                    getString(R.string.network_error) ->
                        textErrorTechnology.text = getString(R.string.network_error)
                    getString(R.string.no_internet_connection) ->
                        textErrorTechnology.text = getString(R.string.no_internet_connection)
                    getString(R.string.connection_timed_out) ->
                        textErrorTechnology.text = getString(R.string.connection_timed_out)
                }
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

        recyclerViewTechnology.apply {
            layoutManager = LinearLayoutManager(this@TechnologyFragment.context)
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

    override fun onRefresh() {
        bindUI()
        hideErrorTextView()
        refreshTechnologyItems.isRefreshing = false
    }

    private fun hideErrorTextView() {
        textErrorTechnology.visibility = View.GONE
    }
}