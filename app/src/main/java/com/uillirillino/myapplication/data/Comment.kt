package com.uillirillino.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Immutable model class for a Comment. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param id        unique identifier of the comment
 * @param postId    id of the post where the comment is written
 * @param name      name of the comment writer
 * @param email     email of the comment writer
 * @param body      body of the comment
 */
@Entity(tableName = "comments")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Comment @JvmOverloads constructor(
    @JsonProperty("id")
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @JsonProperty("postId")
    @ColumnInfo(name = "postId") var postId: Int = 0,
    @JsonProperty("name")
    @ColumnInfo(name = "name") var name: String = "",
    @JsonProperty("email")
    @ColumnInfo(name = "email") var email: String = "",
    @JsonProperty("body")
    @ColumnInfo(name = "body") var body: String = ""
)
