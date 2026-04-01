import React from 'react';
import { View, StyleSheet, Text, TouchableOpacity, SafeAreaView, Platform } from 'react-native';
import { WebView } from 'react-native-webview';
import { Theme, Typography } from '../theme/colors';

export const StreamScreen = ({ route, navigation }: any) => {
  const { channel, stream } = route.params;

  // The official Twitch Embed URLs require a parent domain. For Expo apps, 'localhost' or 'omlet.arcade' is used as a placeholder.
  const parentDomain = 'localhost';
  const playerUrl = `https://player.twitch.tv/?channel=${channel}&parent=${parentDomain}&autoplay=true`;
  const chatUrl = `https://www.twitch.tv/embed/${channel}/chat?parent=${parentDomain}&darkpopout`;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.playerContainer}>
        <WebView 
          source={{ uri: playerUrl }}
          style={styles.playerWebview}
          allowsInlineMediaPlayback
          mediaPlaybackRequiresUserAction={false}
          javaScriptEnabled
        />
        
        {/* Custom Overlay Back Button */}
        <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
          <Text style={styles.backText}>← BACK</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.metaContainer}>
        <Text style={styles.title} numberOfLines={1}>{stream.title}</Text>
        <Text style={styles.subtitle}>{channel.toUpperCase()}  //  {stream.viewer_count} VIEWERS</Text>
      </View>

      <View style={styles.divider} />
      
      <View style={styles.chatContainer}>
        <View style={styles.chatHeader}>
          <Text style={styles.chatTitle}>OFFICIAL TWITCH CHAT</Text>
        </View>
        <WebView 
          source={{ uri: chatUrl }}
          style={styles.chatWebview}
          javaScriptEnabled
        />
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.background,
  },
  playerContainer: {
    width: '100%',
    aspectRatio: 16 / 9,
    backgroundColor: '#000',
    position: 'relative',
    borderBottomWidth: 1,
    borderBottomColor: Theme.border
  },
  playerWebview: {
    flex: 1,
    backgroundColor: '#000',
  },
  backButton: {
    position: 'absolute',
    top: Platform.OS === 'ios' ? 16 : 12,
    left: 12,
    backgroundColor: 'rgba(0,0,0,0.6)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderWidth: 1,
    borderColor: '#444'
  },
  backText: {
    ...Typography.label,
  },
  metaContainer: {
    padding: 16,
    backgroundColor: Theme.surface
  },
  title: {
    ...Typography.subtitle,
    marginBottom: 4
  },
  subtitle: {
    ...Typography.label,
    color: Theme.primary
  },
  divider: {
    height: 1,
    backgroundColor: Theme.border
  },
  chatContainer: {
    flex: 1,
    backgroundColor: Theme.surface
  },
  chatHeader: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    backgroundColor: Theme.background,
    borderBottomWidth: 1,
    borderBottomColor: Theme.border
  },
  chatTitle: {
    ...Typography.label,
    color: Theme.textSecondary
  },
  chatWebview: {
    flex: 1,
    backgroundColor: Theme.surface
  }
});
