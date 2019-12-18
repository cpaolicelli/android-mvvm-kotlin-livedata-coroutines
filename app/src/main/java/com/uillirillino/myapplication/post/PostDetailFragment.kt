package com.uillirillino.myapplication.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.uillirillino.myapplication.R
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.databinding.PostDetailFragmentBinding
import com.uillirillino.myapplication.util.getViewModelFactory
import com.uillirillino.myapplication.util.setupRefreshLayout
import com.uillirillino.myapplication.util.setupSnackbar

class PostDetailFragment : Fragment() {

    private lateinit var viewDataBinding: PostDetailFragmentBinding

    private val args: PostDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<PostViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.post_detail_fragment, container, false)
        viewDataBinding = PostDetailFragmentBinding.bind(view).apply {
            viewmodel = viewModel
        }

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.start(args.postId)

        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}
