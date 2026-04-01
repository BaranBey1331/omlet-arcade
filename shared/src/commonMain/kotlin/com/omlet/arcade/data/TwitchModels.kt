package com.omlet.arcade.data

import kotlinx.serialization.Serializable

@Serializable
data class TwitchStream(
    val id: String,
    val user_id: String,
    val user_name: String,
    val game_name: String,
    val title: String,
    val viewer_count: Int,
    val started_at: String,
    val thumbnail_url: String,
    val is_mature: Boolean
)

@Serializable
data class TwitchStreamsResponse(
    val data: List<TwitchStream>
)

@Serializable
data class ChatMessage(
    val username: String,
    val color: String,
    val message: String
)
