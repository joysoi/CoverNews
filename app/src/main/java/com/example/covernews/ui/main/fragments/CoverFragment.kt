package com.example.covernews.ui.main.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covernews.R
import com.example.covernews.data.adapter.NewsItems
import com.example.covernews.data.model.Article
import com.example.covernews.ui.base.ScopedFragment
import com.example.covernews.ui.detail.DetailsActivity
import com.example.covernews.ui.main.viewmodel.CoverViewModel
import com.example.covernews.ui.main.viewmodel.CoverViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.cover_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CoverFragment : ScopedFragment(), KodeinAware {
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
        viewModel =
            ViewModelProviders.of(this, coverViewModelFactory).get(CoverViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.news.await()
        group_loading.visibility = View.GONE
        viewModel.topHeadlinesLiveData.observe(
            viewLifecycleOwner,
            Observer { topHeadlinesResponse ->
                if (topHeadlinesResponse == null) return@Observer
                group_loading.visibility = View.GONE
                initRecyclerView(topHeadlinesResponse.articles.toArticleItems())
                Log.d("FetchingCover", topHeadlinesResponse.status)
            })
    }

    private fun List<Article>.toArticleItems(): List<NewsItems> {
        return this.map {
            NewsItems(it)
        }
    }

    private fun initRecyclerView(items: List<NewsItems>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
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
        //implement search
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Search latest news..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query?.length!! > 1) {
                        bindSearchUI(query)
                    } else {
                        Toast.makeText(
                            this@CoverFragment.context,
                            "Invalid search!",
                            Toast.LENGTH_SHORT
                        ).show()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindSearchUI(query: String) = launch {
        viewModel.searchNews(query).await()
        viewModel.topHeadlinesLiveData.observe(
            viewLifecycleOwner,
            Observer { topHeadlinesResponse ->
                if (topHeadlinesResponse == null) return@Observer
                initRecyclerView(topHeadlinesResponse.articles.toArticleItems())
                Log.d("FetchingCover", topHeadlinesResponse.status)
            })
    }
}