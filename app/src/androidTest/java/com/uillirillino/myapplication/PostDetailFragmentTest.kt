package com.uillirillino.myapplication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.uillirillino.myapplication.data.source.repo.PostsRepository
import com.uillirillino.myapplication.post.PostDetailFragment
import com.uillirillino.myapplication.post.PostDetailFragmentArgs
import com.uillirillino.myapplication.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the Post Details screen.
 */
@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class PostDetailFragmentTest {

    private lateinit var repository: PostsRepository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.postsRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun postDetails_DisplayedInUi() {
        // GIVEN - Add active (incomplete) post to the DB

        // WHEN - Details fragment launched to display post
        val bundle = PostDetailFragmentArgs(0).toBundle()
        launchFragmentInContainer<PostDetailFragment>(bundle, R.style.AppTheme)

        // THEN - Post details are displayed on the screen
        onView(withId(R.id.post_title)).check(matches(isDisplayed()))
        onView(withId(R.id.post_title)).check(matches(withText("Post")))
        onView(withId(R.id.post_body)).check(matches(isDisplayed()))
        onView(withId(R.id.post_body)).check(matches(withText("body")))
        onView(withId(R.id.author_name)).check(matches(isDisplayed()))
        onView(withId(R.id.author_name)).check(matches(withText("Leanne Graham")))
        onView(withId(R.id.comment_count)).check(matches(isDisplayed()))
        onView(withId(R.id.comment_count)).check(matches(withText("5")))
    }
}
