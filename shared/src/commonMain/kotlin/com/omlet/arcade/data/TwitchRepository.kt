package com.omlet.arcade.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TwitchRepository {
    
    suspend fun getLiveStreams(): List<TwitchStream> {
        delay(800) // Simulate network delay
        return listOf(
            TwitchStream(
                id = "1", user_id = "123", user_name = "Ninja", game_name = "Fortnite",
                title = "LIVE: Grinding to Unreal Rank | !GFUEL", viewer_count = 45120, started_at = "2026-04-01T12:00:00Z",
                thumbnail_url = "https://static-cdn.jtvnw.net/previews-ttv/live_user_ninja-440x248.jpg", is_mature = false
            ),
            TwitchStream(
                id = "2", user_id = "456", user_name = "shroud", game_name = "VALORANT",
                title = "Ranked + VCT Watch Party", viewer_count = 32000, started_at = "2026-04-01T10:00:00Z",
                thumbnail_url = "https://static-cdn.jtvnw.net/previews-ttv/live_user_shroud-440x248.jpg", is_mature = false
            ),
            TwitchStream(
                id = "3", user_id = "789", user_name = "Tarik", game_name = "VALORANT",
                title = "VCT MASTERS TOKYO - SEN vs PRX", viewer_count = 112000, started_at = "2026-04-01T09:00:00Z",
                thumbnail_url = "https://static-cdn.jtvnw.net/previews-ttv/live_user_tarik-440x248.jpg", is_mature = false
            ),
            TwitchStream(
                id = "4", user_id = "101", user_name = "KaiCenat", game_name = "Just Chatting",
                title = "AMP HOUSE MAFIA | !sub", viewer_count = 85000, started_at = "2026-04-01T13:00:00Z",
                thumbnail_url = "https://static-cdn.jtvnw.net/previews-ttv/live_user_kaicenat-440x248.jpg", is_mature = true
            )
        )
    }

    // Mock Twitch IRC Chat Flow
    fun connectToChat(channel: String): Flow<ChatMessage> = flow {
        val usernames = listOf("GamerX", "PogChamp123", "NoobSlayer", "KappaLord", "KekwMaster", "OmletFan")
        val colors = listOf("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF")
        val messages = listOf("LULW", "Pog", "KEKW", "OMEGALUL", "RIP BOZO", "Aim assist?", "W stream", "HYPE")
        
        while(true) {
            delay((200..1200).random().toLong()) // Random, fast chat speed
            emit(
                ChatMessage(
                    username = usernames.random(),
                    color = colors.random(),
                    message = messages.random()
                )
            )
        }
    }
}
