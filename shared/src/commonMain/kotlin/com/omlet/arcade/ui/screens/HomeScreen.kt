package com.omlet.arcade.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.omlet.arcade.data.TwitchRepository
import com.omlet.arcade.data.TwitchStream
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = remember { TwitchRepository() }
        var streams by remember { mutableStateOf<List<TwitchStream>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                streams = repository.getLiveStreams()
                isLoading = false
            }
        }

        Scaffold(
            topBar = {
                WorkstationHeader()
            },
            bottomBar = {
                WorkstationBottomBar()
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, strokeWidth = 2.dp)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            CategorySection()
                        }
                        
                        item {
                            SectionHeader("LIVE NOW")
                        }

                        items(streams) { stream ->
                            ProfessionalStreamCard(stream) {
                                navigator.push(StreamScreen(stream))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkstationHeader() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "OMLET WORKSTATION",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            )
        }
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Box(modifier = Modifier.size(4.dp).background(MaterialTheme.colorScheme.primary))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

@Composable
fun CategorySection() {
    Column {
        SectionHeader("ECOSYSTEMS")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { EcosystemChip("TWITCH", true) }
            item { EcosystemChip("YOUTUBE", false) }
            item { EcosystemChip("KICK", false) }
            item { EcosystemChip("ESPORTS", false) }
        }
    }
}

@Composable
fun EcosystemChip(name: String, active: Boolean) {
    Box(
        modifier = Modifier
            .background(if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .border(1.dp, if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = if (active) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
fun ProfessionalStreamCard(stream: TwitchStream, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
            KamelImage(
                resource = asyncPainterResource(data = stream.thumbnail_url),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop
            )
            // Overlay badges
            Row(modifier = Modifier.align(Alignment.TopStart).padding(8.dp)) {
                Box(modifier = Modifier.background(Color.Red).padding(horizontal = 4.dp, vertical = 2.dp)) {
                    Text("LIVE", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("${stream.viewer_count} VIEWERS", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surface).border(1.dp, MaterialTheme.colorScheme.outline))
            Column {
                Text(text = stream.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    text = "${stream.user_name.uppercase()}  //  ${stream.game_name.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun WorkstationBottomBar() {
    Column {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem("DASHBOARD", true)
            BottomNavItem("EXPLORE", false)
            BottomNavItem("COMMUNITY", false)
            BottomNavItem("PROFILE", false)
        }
    }
}

@Composable
fun BottomNavItem(label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (active) MaterialTheme.colorScheme.primary else Color.Gray
        )
        if (active) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.size(12.dp, 2.dp).background(MaterialTheme.colorScheme.primary))
        }
    }
}
