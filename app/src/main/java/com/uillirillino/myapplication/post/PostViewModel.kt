package com.uillirillino.myapplication.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uillirillino.myapplication.Event
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.User
import com.uillirillino.myapplication.data.source.repo.CommentsRepository
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import com.uillirillino.myapplication.data.source.repo.UsersRepository
import com.uillirillino.myapplication.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch

class PostViewModel(
    private val postsRepository: PostsRepository,
    private val commentsRepository: CommentsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _commentsCount = MutableLiveData<String>()
    val commentsCount: LiveData<String> = _commentsCount

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val _author = MutableLiveData<User>()
    val author: LiveData<User> = _author

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun start(postId: Int?) {

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                if (postId != null) {
                    postsRepository.getPost(postId, false).let { result ->
                        if (result is Result.Success) {
                            onPostLoaded(result.data)

                            usersRepository.getAuthor(postId, false).let {res ->
                                var author = User(0, "Undefined")
                                if (res is Result.Success) {
                                    author = res.data
                                }
                                setAuthor(author)
                            }

                            commentsRepository.getCommentsCount(postId, false).let { countRes ->
                                var count = 0
                                if (countRes is Result.Success) {
                                    count = countRes.data
                                }
                                setCommentsCount(count)
                            }
                        } else {
                            onDataNotAvailable()
                        }
                    }
                }
            }
        }
    }



    private fun onPostLoaded(post: Post) {
        setPost(post)
    }

    private fun setCommentsCount(count: Int) {
        _commentsCount.value = count.toString()
    }

    private fun setAuthor(author: User) {
        _author.value = author
    }

    private fun setPost(post: Post?) {
        _post.value = post
    }

    private fun onDataNotAvailable() {
        _post.value = null
    }
}