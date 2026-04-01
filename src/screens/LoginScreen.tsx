import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { WebView } from 'react-native-webview';
import { useStore } from '../store';
import { setupAxiosInterceptors, fetchCurrentUser, TWITCH_CLIENT_ID } from '../services/TwitchApi';
import { Theme, Typography } from '../theme/colors';
import { OmletLogo } from '../components/OmletLogo';

const REDIRECT_URI = 'https://localhost'; // Using localhost to securely intercept without needing custom domain schemes
const TWITCH_AUTH_URL = `https://id.twitch.tv/oauth2/authorize?client_id=${TWITCH_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=token&scope=user:read:email`;

export const LoginScreen = () => {
  const setAuth = useStore(state => state.setAuth);
  const [showWebview, setShowWebview] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleNavigationChange = async (navState: any) => {
    const { url } = navState;
    if (url.includes('access_token=')) {
      setShowWebview(false);
      setLoading(true);
      
      const fragment = url.split('#')[1];
      const params = new URLSearchParams(fragment);
      const token = params.get('access_token');
      
      if (token) {
        try {
          setupAxiosInterceptors(token);
          const user = await fetchCurrentUser();
          setAuth(token, user);
        } catch (error) {
          console.error("Twitch Login Failed:", error);
          setLoading(false);
        }
      }
    }
  };

  if (showWebview) {
    return (
      <View style={{ flex: 1, backgroundColor: Theme.background }}>
        <WebView 
          source={{ uri: TWITCH_AUTH_URL }} 
          onNavigationStateChange={handleNavigationChange}
          incognito={true} // Force fresh login if needed
        />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <OmletLogo width={120} height={120} color={Theme.primary} />
      <Text style={styles.title}>OMLET PREMIUM</Text>
      <Text style={styles.subtitle}>ELEVATE YOUR WORKSTATION</Text>
      
      {loading ? (
        <ActivityIndicator size="large" color={Theme.primary} style={{ marginTop: 20 }} />
      ) : (
        <TouchableOpacity style={styles.button} onPress={() => setShowWebview(true)}>
          <Text style={styles.buttonText}>AUTHORIZE TWITCH</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.background,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20
  },
  title: {
    ...Typography.title,
    marginTop: 20,
    marginBottom: 4,
    fontSize: 28,
  },
  subtitle: {
    ...Typography.body,
    marginBottom: 50,
    color: Theme.textSecondary,
    letterSpacing: 2,
    fontSize: 12,
  },
  button: {
    backgroundColor: Theme.surface,
    paddingVertical: 16,
    paddingHorizontal: 40,
    borderRadius: 30, // Premium rounded look
    borderWidth: 1,
    borderColor: Theme.primary,
    shadowColor: Theme.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 5,
  },
  buttonText: {
    ...Typography.label,
    color: '#FFFFFF',
    fontSize: 14,
  }
});
