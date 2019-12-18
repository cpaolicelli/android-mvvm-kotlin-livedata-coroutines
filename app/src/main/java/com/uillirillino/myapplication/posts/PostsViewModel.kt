package com.uillirillino.myapplication.posts

import androidx.lifecycle.*
import com.uillirillino.myapplication.Event
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import com.uillirillino.myapplication.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.*

/**
 * Posts ViewModel
 */
class PostsViewModel(
    private val postsRepository: PostsRepository
): ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _items = MutableLiveData<List<Post>>().apply { value = emptyList() }
    val items: LiveData<List<Post>> = _items

    private val _openPostEvent = MutableLiveData<Event<Int>>()
    val openPostEvent: LiveData<Event<Int>> = _openPostEvent

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    private val isDataLoadingError = MutableLiveData<Boolean>()

    init {
        // Set initial state
        loadPosts(true)
    }

    /**
     * Called by Data Binding.
     */
    fun openPost(postId: Int) {
        _openPostEvent.value = Event(postId)
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the [PostsDataSource]
     */
    fun loadPosts(forceUpdate: Boolean) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val postsResult = postsRepository.getPosts(forceUpdate)

                if (postsResult is Result.Success) {
                    isDataLoadingError.value = false
                    _items.value = ArrayList(postsResult.data)
                } else {
                    isDataLoadingError.value = false
                    _items.value = emptyList()
                }

                _dataLoading.value = false
            }
        }
    }

    fun refresh() {
        loadPosts(true)
    }
}