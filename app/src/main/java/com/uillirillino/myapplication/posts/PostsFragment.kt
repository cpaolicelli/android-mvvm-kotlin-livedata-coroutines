package com.uillirillino.myapplication.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uillirillino.myapplication.EventObserver
import com.uillirillino.myapplication.databinding.PostsFragmentBinding
import com.uillirillino.myapplication.util.getViewModelFactory
import com.uillirillino.myapplication.util.setupRefreshLayout
import timber.log.Timber

/**
 * Display a list of [Post]s.
 */
class PostsFragment : Fragment() {

    private val viewModel by viewModels<PostsViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: PostsFragmentBinding

    private lateinit var listAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = PostsFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.postsList)
        setupNavigation()

        // Always reloading data for simplicity. Real apps should only do this on first load and
        // when navigating back to this destination.
        viewModel.loadPosts(true)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = PostsAdapter(viewModel)
            viewDataBinding.postsList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupNavigation() {
        viewModel.openPostEvent.observe(this, EventObserver {
            openPostDetails(it)
        })
    }

    private fun openPostDetails(postId: Int) {
        val action = PostsFragmentDirections.actionPostsFragmentToPostDetailFragment(postId)
        findNavController().navigate(action)
    }
}
