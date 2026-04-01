package com.omlet.arcade.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.omlet.arcade.data.ChatMessage
import com.omlet.arcade.data.TwitchRepository
import com.omlet.arcade.data.TwitchStream
import com.omlet.arcade.ui.components.TwitchPlayer
import kotlinx.coroutines.launch

class StreamScreen(private val stream: TwitchStream) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = remember { TwitchRepository() }
        val chatMessages = remember { mutableStateListOf<ChatMessage>() }
        
        LaunchedEffect(stream.user_name) {
            repository.connectToChat(stream.user_name).collect { msg ->
                chatMessages.add(msg)
                if(chatMessages.size > 50) chatMessages.removeAt(0) // Keep chat buffer small
            }
        }

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            // Embedded Twitch Player
            Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.Black)) {
                TwitchPlayer(
                    channel = stream.user_name,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Back Button Overlay
                Button(
                    onClick = { navigator.pop() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.7f)),
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(0.dp) // Sharp
                ) {
                    Text("BACK", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }

            // Stream Info
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stream.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${stream.user_name.uppercase()}  //  ${stream.game_name.uppercase()}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${stream.viewer_count} VIEWERS", style = MaterialTheme.typography.bodyMedium)
            }
            
            Divider(color = MaterialTheme.colorScheme.surface)

            // Chat Area
            Text(
                text = "TWITCH CHAT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = true
            ) {
                items(chatMessages.reversed()) { msg ->
                    Text(
                        text = buildAnnotatedString {
                            val colorValue = try { Color(msg.color.replace("#", "FF").toLong(16)) } catch(e: Exception) { Color.White }
                            withStyle(style = SpanStyle(color = colorValue, fontWeight = FontWeight.Bold)) {
                                append("${msg.username}: ")
                            }
                            append(msg.message)
                        },
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
            }
            
            // Chat Input (Mock)
            Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(8.dp)) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Send a message...", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}
