import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, Image, TouchableOpacity, ActivityIndicator } from 'react-native';
import { BlurView } from 'expo-blur';
import { useStore } from '../store';
import { fetchLiveStreams } from '../services/TwitchApi';
import { Theme, Typography } from '../theme/colors';
import { OmletLogo } from '../components/OmletLogo';

export const HomeScreen = ({ navigation }: any) => {
  const user = useStore(state => state.user);
  const [streams, setStreams] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLiveStreams(user?.broadcaster_language || 'en').then(data => {
      setStreams(data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, [user]);

  const renderStream = ({ item }: { item: any }) => {
    const thumbUrl = item.thumbnail_url.replace('{width}', '600').replace('{height}', '338');
    
    return (
      <TouchableOpacity 
        style={styles.card} 
        activeOpacity={0.8}
        onPress={() => navigation.navigate('Stream', { channel: item.user_login, stream: item })}
      >
        <View style={styles.thumbnailContainer}>
          <Image source={{ uri: thumbUrl }} style={styles.thumbnail} />
          
          <BlurView intensity={80} tint="dark" style={styles.liveBadge}>
            <View style={styles.liveDot} />
            <Text style={styles.liveText}>LIVE</Text>
          </BlurView>

          <BlurView intensity={80} tint="dark" style={styles.viewersBadge}>
            <Text style={styles.viewersText}>{item.viewer_count.toLocaleString()} VIEWERS</Text>
          </BlurView>
        </View>
        <View style={styles.metaContainer}>
          <Text style={styles.title} numberOfLines={1}>{item.title}</Text>
          <Text style={styles.subtitle}>{item.user_name}  •  {item.game_name}</Text>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      {/* Glassmorphic Premium Header */}
      <BlurView intensity={90} tint="dark" style={styles.header}>
        <View style={styles.headerContent}>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <OmletLogo width={32} height={32} />
            <Text style={styles.headerTitle}>Omlet Premium</Text>
          </View>
          {user?.profile_image_url && <Image source={{ uri: user.profile_image_url }} style={styles.avatar} />}
        </View>
      </BlurView>
      
      {loading ? (
        <View style={styles.loader}>
          <ActivityIndicator color={Theme.primary} size="large" />
        </View>
      ) : (
        <FlatList
          data={streams}
          keyExtractor={(item) => item.id}
          renderItem={renderStream}
          contentContainerStyle={styles.list}
          showsVerticalScrollIndicator={false}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F0F13', // Deep premium dark
  },
  header: {
    paddingTop: 50, // Safe area approx
    paddingBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.05)',
    zIndex: 10,
  },
  headerContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
  },
  headerTitle: {
    ...Typography.title,
    fontSize: 20,
    marginLeft: 10,
    letterSpacing: -0.5,
  },
  avatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    borderWidth: 2,
    borderColor: Theme.primary,
  },
  loader: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  list: {
    padding: 16,
    paddingTop: 24, // Space for header
  },
  card: {
    marginBottom: 24,
    borderRadius: 20,
    backgroundColor: '#1A1A1D',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.5,
    shadowRadius: 15,
    elevation: 8,
    overflow: 'hidden' // Keeps the image rounded
  },
  thumbnailContainer: {
    width: '100%',
    aspectRatio: 16 / 9,
    position: 'relative'
  },
  thumbnail: {
    width: '100%',
    height: '100%'
  },
  liveBadge: {
    position: 'absolute',
    top: 12,
    left: 12,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    overflow: 'hidden'
  },
  liveDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: Theme.danger,
    marginRight: 6
  },
  liveText: {
    ...Typography.label,
    color: '#FFF'
  },
  viewersBadge: {
    position: 'absolute',
    bottom: 12,
    left: 12,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    overflow: 'hidden'
  },
  viewersText: {
    ...Typography.label,
    color: '#FFF'
  },
  metaContainer: {
    padding: 16,
  },
  title: {
    ...Typography.subtitle,
    fontSize: 16,
    marginBottom: 6
  },
  subtitle: {
    ...Typography.body,
    fontSize: 13,
    color: Theme.textSecondary
  }
});
