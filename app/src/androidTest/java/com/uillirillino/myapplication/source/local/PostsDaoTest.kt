package com.uillirillino.myapplication.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.uillirillino.myapplication.MainCoroutineRule
import com.uillirillino.myapplication.data.Post
import com.uillirillino.myapplication.data.source.local.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PostsDaoTest {

    private lateinit var database: AppDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each post synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertPostAndGetById() = runBlockingTest {
        // GIVEN - insert a post
        val post = Post()
        database.postDao().insertPost(post)

        // WHEN - Get the post by id from the database
        val loaded = database.postDao().getPostById(post.id)

        // THEN - The loaded data contains the expected values
        assertThat<Post>(loaded as Post, notNullValue())
        assertThat(loaded.id, `is`(post.id))
        assertThat(loaded.title, `is`(post.title))
        assertThat(loaded.body, `is`(post.body))
    }

    @Test
    fun insertPostReplacesOnConflict() = runBlockingTest {
        // GIVEN - insert a post
        val post = Post()
        database.postDao().insertPost(post)

        // When a post with the same id is inserted
        val newPost = Post(1,1,"title2", "description2")
        database.postDao().insertPost(newPost)

        // THEN - The loaded data contains the expected values
        val loaded = database.postDao().getPostById(post.id)
        assertThat(loaded?.id, `is`(post.id))
        assertThat(loaded?.title, `is`("title2"))
        assertThat(loaded?.body, `is`("description2"))
    }

    @Test
    fun insertPostAndGetPosts() = runBlockingTest {
        // GIVEN - insert a post
        val post = Post()
        database.postDao().insertPost(post)

        // WHEN - Get posts from the database
        val posts = database.postDao().getPosts()

        // THEN - There is only 1 post in the database, and contains the expected values
        assertThat(posts.size, `is`(1))
        assertThat(posts[0].id, `is`(post.id))
        assertThat(posts[0].title, `is`(post.title))
        assertThat(posts[0].body, `is`(post.body))
    }

    @Test
    fun updatePostAndGetById() = runBlockingTest {
        // When inserting a post
        val originalPost = Post()
        database.postDao().insertPost(originalPost)

        // When the post is updated
        val updatedPost = Post(2,1,"title3", "description3")
        database.postDao().insertPost(updatedPost)

        // THEN - The loaded data contains the expected values
        val loaded = database.postDao().getPostById(originalPost.id)
        assertThat(loaded?.id, `is`(originalPost.id))
        assertThat(loaded?.title, `is`("title3"))
        assertThat(loaded?.body, `is`("description3"))
    }

    @Test
    fun deletePostByIdAndGettingPosts() = runBlockingTest {
        // Given a post inserted
        val post = Post(0,0,"title", "description")
        database.postDao().insertPost(post)

        // When deleting a post by id
        database.postDao().deletePostById(post.id)

        // THEN - The list is empty
        val posts = database.postDao().getPosts()
        assertThat(posts.isEmpty(), `is`(true))
    }

    @Test
    fun deletePostsAndGettingPosts() = runBlockingTest {
        // Given a post inserted
        database.postDao().insertPost(Post(0,0,"title", "description"))

        // When deleting all posts
        database.postDao().deletePosts()

        // THEN - The list is empty
        val posts = database.postDao().getPosts()
        assertThat(posts.isEmpty(), `is`(true))
    }
}
