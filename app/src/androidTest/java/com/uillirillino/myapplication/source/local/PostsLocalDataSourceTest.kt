package com.uillirillino.myapplication.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.uillirillino.myapplication.MainCoroutineRule
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.Result
import com.uillirillino.myapplication.data.source.local.AppDatabase
import com.uillirillino.myapplication.data.source.local.post.PostsLocalDataSource
import com.uillirillino.myapplication.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the [PostsDataSource].
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class PostsLocalDataSourceTest {

    private lateinit var localDataSource: PostsLocalDataSource
    private lateinit var database: AppDatabase


    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each post synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = PostsLocalDataSource(database.postDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun savePost_retrievesPost() = runBlockingTest {
        // GIVEN - a new post saved in the database
        val newPost = Post(0, 0,"title", "description")
        localDataSource.savePost(newPost)

        // WHEN  - Post retrieved by ID
        val result = localDataSource.getPost(newPost.id)

        // THEN - Same post is returned
        assertThat(result.succeeded, `is`(true))
        result as Result.Success<Post>

        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.body, `is`("description"))
    }

    @Test
    fun deleteAllPosts_emptyListOfRetrievedPost() = runBlockingTest {
        // Given a new post in the persistent repository and a mocked callback
        val newPost = Post(0,0,"title", "description")

        localDataSource.savePost(newPost)

        // When all posts are deleted
        localDataSource.deleteAllPosts()

        // Then the retrieved posts is an empty list
        val result = localDataSource.getPosts() as Result.Success<*>
        assertThat(result.data, `is`(true))

    }

    @Test
    fun getPosts_retrieveSavedPosts() = runBlockingTest {
        // Given 2 new posts in the persistent repository
        val newPost1 = Post(0,0,"title", "description")
        val newPost2 = Post(1,0,"title2", "description2")

        localDataSource.savePost(newPost1)
        localDataSource.savePost(newPost2)
        // Then the posts can be retrieved from the persistent repository
        val results = localDataSource.getPosts() as Result.Success<List<Post>>
        val posts = results.data
        assertThat(posts.size, `is`(2))
    }
}
