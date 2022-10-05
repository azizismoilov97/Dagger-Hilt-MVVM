package admiral.group.daggerhiltmvvm.ui

import admiral.group.daggerhiltmvvm.R
import admiral.group.daggerhiltmvvm.model.Blog
import admiral.group.daggerhiltmvvm.util.DataState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BlogAdapter.BlogItemListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
        }

    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Blog>> -> {
                    displayLoading(false)
                    populateRecyclerView(dataState.data)
                }
                is DataState.Loading -> {
                    displayLoading(true)
                }
                is DataState.Error -> {
                    displayLoading(false)
                    displayError(dataState.exception.message)
                }
            }
        })
    }


    private fun displayError(message: String?) {
        if (message.isNullOrEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayLoading(isLoading: Boolean) {
        swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun populateRecyclerView(blogs: List<Blog>) {
        if (blogs.isNotEmpty()) adapter.setItems(ArrayList(blogs))
    }

    private fun setupRecyclerView() {
        adapter = BlogAdapter(this)
        blog_recyclerview.layoutManager = LinearLayoutManager(this)
        blog_recyclerview.adapter = adapter
    }

    override fun onClickedBlog(blogTitle: CharSequence) {
        Toast.makeText(this, blogTitle, Toast.LENGTH_SHORT).show()
    }

}