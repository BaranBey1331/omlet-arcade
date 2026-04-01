package com.omlet.arcade.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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

class StreamScreen(private val stream: TwitchStream) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = remember { TwitchRepository() }
        val chatMessages = remember { mutableStateListOf<ChatMessage>() }
        
        LaunchedEffect(stream.user_name) {
            repository.connectToChat(stream.user_name.lowercase()).collect { msg ->
                chatMessages.add(msg)
                if(chatMessages.size > 100) chatMessages.removeAt(0)
            }
        }

        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            // Real Twitch Player
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f).background(Color.Black)) {
                TwitchPlayer(
                    channel = stream.user_name,
                    modifier = Modifier.fillMaxSize()
                )
                
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { navigator.pop() }
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.dp, Color.Gray)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("← BACK", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }

            // Stream Meta
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stream.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color.Red))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${stream.user_name.uppercase()}  //  ${stream.viewer_count} VIEWERS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

            // Real-time Chat
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    reverseLayout = true
                ) {
                    items(chatMessages.reversed()) { msg ->
                        ChatLine(msg)
                    }
                }
            }
            
            // Input Area
            WorkstationChatInput()
        }
    }
}

@Composable
fun ChatLine(msg: ChatMessage) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFFADADB8), fontWeight = FontWeight.Bold)) {
                append("${msg.username.uppercase()}: ")
            }
            append(msg.message)
        },
        modifier = Modifier.padding(vertical = 4.dp),
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
    )
}

@Composable
fun WorkstationChatInput() {
    Column {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text("SEND MESSAGE...", color = Color.DarkGray, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text("CHAT", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
