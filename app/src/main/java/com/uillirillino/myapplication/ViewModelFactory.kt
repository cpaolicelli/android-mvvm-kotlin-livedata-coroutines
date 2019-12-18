package com.uillirillino.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uillirillino.myapplication.data.source.repo.CommentsRepository
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import com.uillirillino.myapplication.data.source.repo.UsersRepository
import com.uillirillino.myapplication.post.PostViewModel
import com.uillirillino.myapplication.posts.PostsViewModel

/**
 * Factory for all ViewModels
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val postsRepository: PostsRepository,
    private val commentsRepository: CommentsRepository,
    private val usersRepository: UsersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(PostsViewModel::class.java) ->
                    PostsViewModel(postsRepository)
                isAssignableFrom(PostViewModel::class.java) ->
                    PostViewModel(
                        postsRepository,
                        commentsRepository,
                        usersRepository
                    )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
