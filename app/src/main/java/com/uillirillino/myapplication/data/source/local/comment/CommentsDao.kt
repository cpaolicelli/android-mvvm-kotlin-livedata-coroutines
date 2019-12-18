/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uillirillino.myapplication.data.source.local.comment

import androidx.room.Dao
import androidx.room.Query

/**
 * Data Access Object for the comment table.
 */
@Dao
interface CommentsDao {

    /**
     * Get the number of comments by post id.
     *
     * @param postId the post id.
     * @return the number of comments in the given post
     */
    @Query("SELECT COUNT(id) FROM Comments WHERE postId = :postId")
    suspend fun getCommentsCount(postId: Int): Int?
}
