package com.omlet.arcade.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

class TwitchRepository {
    
    // Prototip için varsayılan (genel) Client-ID. Gerçek kullanımda kullanıcı kendi ID'sini eklemelidir.
    private val clientId = "gp762nuuoqcoxypju8c569th9wz7q5" 
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(WebSockets)
    }

    suspend fun getLiveStreams(): List<TwitchStream> {
        return try {
            // Not: Helix API artık App Access Token gerektiriyor. 
            // Bu örnekte mock veriye dönüyoruz ama yapı Ktor ile hazır.
            // Gerçek API için: client.get("https://api.twitch.tv/helix/streams") { header("Client-ID", clientId) }
            
            delay(500)
            listOf(
                TwitchStream("1", "1", "Ninja", "Fortnite", "RANKED GRIND", 45000, "", "https://static-cdn.jtvnw.net/previews-ttv/live_user_ninja-440x248.jpg", false),
                TwitchStream("2", "2", "shroud", "VALORANT", "PRO PLAY", 32000, "", "https://static-cdn.jtvnw.net/previews-ttv/live_user_shroud-440x248.jpg", false),
                TwitchStream("3", "3", "Tarik", "VALORANT", "VCT WATCH PARTY", 110000, "", "https://static-cdn.jtvnw.net/previews-ttv/live_user_tarik-440x248.jpg", false),
                TwitchStream("4", "4", "KaiCenat", "Just Chatting", "MAFIA III", 85000, "", "https://static-cdn.jtvnw.net/previews-ttv/live_user_kaicenat-440x248.jpg", true)
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun connectToChat(channel: String): Flow<ChatMessage> = flow {
        client.webSocket(method = HttpMethod.Get, host = "irc-ws.chat.twitch.tv", port = 443, path = "/", request = {
            url { protocol = URLProtocol.WSS }
        }) {
            send("PASS oauth:dummy_token") // Anonymous login
            send("NICK justinfan${(10000..99999).random()}")
            send("JOIN #$channel")
            
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    if (text.startsWith("PING")) {
                        send("PONG :tmi.twitch.tv")
                    } else if (text.contains("PRIVMSG")) {
                        val message = parseIrcMessage(text)
                        if (message != null) emit(message)
                    }
                }
            }
        }
    }.catch { e ->
        println("Chat Error: ${e.message}")
    }.flowOn(Dispatchers.Default)

    private fun parseIrcMessage(raw: String): ChatMessage? {
        // Örnek: :user!user@user.tmi.twitch.tv PRIVMSG #channel :message
        return try {
            val userPart = raw.substringBefore("!").removePrefix(":")
            val messagePart = raw.substringAfter("PRIVMSG #$raw").substringAfter(":") // Basitleştirilmiş parse
            val actualMessage = raw.split("PRIVMSG")[1].split(" :")[1]
            ChatMessage(
                username = userPart,
                color = "#${(100000..999999).random()}", // Twitch renkleri için normalde tag okumak gerekir
                message = actualMessage.trim()
            )
        } catch (e: Exception) {
            null
        }
    }
}
