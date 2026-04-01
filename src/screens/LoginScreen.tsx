import React, { useState, useRef } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { WebView } from 'react-native-webview';
import { useStore } from '../store';
import { setupAxiosInterceptors, fetchCurrentUser, TWITCH_CLIENT_ID } from '../services/TwitchApi';
import { Theme, Typography } from '../theme/colors';
import { OmletLogo } from '../components/OmletLogo';

const REDIRECT_URI = 'https://localhost';
const TWITCH_AUTH_URL = `https://id.twitch.tv/oauth2/authorize?client_id=${TWITCH_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=token&scope=user:read:email`;

// Spoof standard Chrome browser to bypass Google 403 "disallowed_useragent" on OAuth flows inside WebViews.
const CHROME_USER_AGENT = "Mozilla/5.0 (Linux; Android 13; SM-S901B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36";

export const LoginScreen = () => {
  const setAuth = useStore(state => state.setAuth);
  const [showWebview, setShowWebview] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  
  // Prevent token loop: The WebView fires onNavigationStateChange multiple times.
  const hasHandledToken = useRef(false);

  const handleNavigationChange = async (navState: any) => {
    const { url } = navState;
    if (url.includes('access_token=') && !hasHandledToken.current) {
      hasHandledToken.current = true; // Lock immediately so it only fires once
      setShowWebview(false);
      setLoading(true);
      setErrorMsg(null);
      
      const fragment = url.split('#')[1];
      const params = new URLSearchParams(fragment);
      const token = params.get('access_token');
      
      if (token) {
        try {
          setupAxiosInterceptors(token);
          const user = await fetchCurrentUser();
          setAuth(token, user);
        } catch (error) {
          console.error("Twitch Login API Failed:", error);
          setErrorMsg("Login failed. Could not fetch Twitch profile. Try again.");
          setLoading(false);
          hasHandledToken.current = false; // Unlock so user can try again
        }
      } else {
        setLoading(false);
        hasHandledToken.current = false;
        setErrorMsg("Failed to extract access token.");
      }
    }
  };

  if (showWebview) {
    return (
      <View style={{ flex: 1, backgroundColor: Theme.background }}>
        <WebView 
          source={{ uri: TWITCH_AUTH_URL }} 
          onNavigationStateChange={handleNavigationChange}
          userAgent={CHROME_USER_AGENT}
          incognito={true}
        />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <OmletLogo width={120} height={120} color={Theme.primary} />
      <Text style={styles.title}>OMLET PREMIUM</Text>
      <Text style={styles.subtitle}>ELEVATE YOUR WORKSTATION</Text>
      
      {errorMsg && (
        <Text style={styles.errorText}>{errorMsg}</Text>
      )}

      {loading ? (
        <ActivityIndicator size="large" color={Theme.primary} style={{ marginTop: 20 }} />
      ) : (
        <TouchableOpacity style={styles.button} onPress={() => {
          hasHandledToken.current = false;
          setErrorMsg(null);
          setShowWebview(true);
        }}>
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
    marginBottom: 40,
    color: Theme.textSecondary,
    letterSpacing: 2,
    fontSize: 12,
  },
  errorText: {
    ...Typography.body,
    color: Theme.danger,
    marginBottom: 20,
    textAlign: 'center',
    paddingHorizontal: 20,
  },
  button: {
    backgroundColor: Theme.surface,
    paddingVertical: 16,
    paddingHorizontal: 40,
    borderRadius: 30,
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
