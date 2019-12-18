package com.uillirillino.myapplication.util

/**
 * Extension functions for Fragment.
 */
import androidx.fragment.app.Fragment
import com.uillirillino.myapplication.PostApplication
import com.uillirillino.myapplication.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {

    val postsRepository = (requireContext().applicationContext as PostApplication).postsRepository
    val commentsRepository = (requireContext().applicationContext as PostApplication).commentsRepository
    val usersRepository = (requireContext().applicationContext as PostApplication).usersRepository

    return ViewModelFactory(
        postsRepository,
        commentsRepository,
        usersRepository
    )
}
