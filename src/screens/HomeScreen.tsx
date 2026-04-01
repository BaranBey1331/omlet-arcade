import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, Image, TouchableOpacity, SafeAreaView, ActivityIndicator } from 'react-native';
import { useStore } from '../store';
import { fetchLiveStreams } from '../services/TwitchApi';
import { Theme, Typography } from '../theme/colors';

export const HomeScreen = ({ navigation }: any) => {
  const user = useStore(state => state.user);
  const [streams, setStreams] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Fetch real live streams filtering by the authenticated user's native language.
    fetchLiveStreams(user?.broadcaster_language || 'en').then(data => {
      setStreams(data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, [user]);

  const renderStream = ({ item }: { item: any }) => {
    // Helix returns thumbnail_url with {width} and {height} placeholders.
    const thumbUrl = item.thumbnail_url.replace('{width}', '440').replace('{height}', '248');
    
    return (
      <TouchableOpacity 
        style={styles.card} 
        onPress={() => navigation.navigate('Stream', { channel: item.user_login, stream: item })}
      >
        <View style={styles.thumbnailContainer}>
          <Image source={{ uri: thumbUrl }} style={styles.thumbnail} />
          <View style={styles.liveBadge}><Text style={styles.liveText}>LIVE</Text></View>
          <View style={styles.viewersBadge}><Text style={styles.viewersText}>{item.viewer_count} VIEWERS</Text></View>
        </View>
        <View style={styles.metaContainer}>
          <Text style={styles.title} numberOfLines={1}>{item.title}</Text>
          <Text style={styles.subtitle}>{item.user_name.toUpperCase()}  //  {item.game_name.toUpperCase()}</Text>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>OMLET WORKSTATION</Text>
        {user?.profile_image_url && <Image source={{ uri: user.profile_image_url }} style={styles.avatar} />}
      </View>
      <View style={styles.divider} />
      
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
          ListHeaderComponent={<Text style={styles.sectionHeader}>LIVE FOR YOU ({user?.broadcaster_language?.toUpperCase() || 'EN'})</Text>}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.background,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
    backgroundColor: Theme.surface
  },
  headerTitle: {
    ...Typography.title,
    fontSize: 20
  },
  avatar: {
    width: 36,
    height: 36,
    borderWidth: 1,
    borderColor: Theme.border,
  },
  divider: {
    height: 1,
    backgroundColor: Theme.border
  },
  loader: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  list: {
    padding: 16,
  },
  sectionHeader: {
    ...Typography.label,
    color: Theme.primary,
    marginBottom: 16
  },
  card: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: Theme.border,
    backgroundColor: Theme.surface
  },
  thumbnailContainer: {
    width: '100%',
    aspectRatio: 16 / 9,
    backgroundColor: '#000',
    position: 'relative'
  },
  thumbnail: {
    width: '100%',
    height: '100%'
  },
  liveBadge: {
    position: 'absolute',
    top: 8,
    left: 8,
    backgroundColor: Theme.danger,
    paddingHorizontal: 6,
    paddingVertical: 2
  },
  liveText: {
    ...Typography.label,
    color: '#FFF'
  },
  viewersBadge: {
    position: 'absolute',
    bottom: 8,
    left: 8,
    backgroundColor: 'rgba(0,0,0,0.8)',
    borderWidth: 1,
    borderColor: '#333',
    paddingHorizontal: 6,
    paddingVertical: 2
  },
  viewersText: {
    ...Typography.label,
    color: '#FFF'
  },
  metaContainer: {
    padding: 12
  },
  title: {
    ...Typography.subtitle,
    marginBottom: 4
  },
  subtitle: {
    ...Typography.body,
    fontSize: 12
  }
});
