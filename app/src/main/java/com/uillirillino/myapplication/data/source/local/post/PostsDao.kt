package com.uillirillino.myapplication.data.source.local.post

import androidx.room.*
import com.uillirillino.myapplication.data.Post

/**
 * Data Access Object for the posts table.
 */
@Dao
interface PostsDao {

    /**
     * Select all posts from the posts table.
     *
     * @return all posts.
     */
    @Query("SELECT * FROM Posts")
    suspend fun getPosts(): List<Post>

    /**
     * Select a post by id.
     *
     * @param postId the post id.
     * @return the post with postId.
     */
    @Query("SELECT * FROM Posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): Post?

    /**
     * Insert a post in the database. If the post already exists, replace it.
     *
     * @param post the post to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    /**
     * Delete all posts.
     */
    @Query("DELETE FROM Posts")
    suspend fun deletePosts()

    /**
     * Delete a post by id.
     *
     * @return the number of posts deleted. This should always be 1.
     */
    @Query("DELETE FROM Posts WHERE id = :postId")
    suspend fun deletePostById(postId: Int): Int
}
