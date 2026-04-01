import React, { useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import * as AuthSession from 'expo-auth-session';
import { useStore } from '../store';
import { setupAxiosInterceptors, fetchCurrentUser, TWITCH_CLIENT_ID } from '../services/TwitchApi';
import { Theme, Typography } from '../theme/colors';

const redirectUri = AuthSession.makeRedirectUri({
  scheme: 'omlet-arcade'
});

const discovery = {
  authorizationEndpoint: 'https://id.twitch.tv/oauth2/authorize',
  tokenEndpoint: 'https://id.twitch.tv/oauth2/token',
  revocationEndpoint: 'https://id.twitch.tv/oauth2/revoke',
};

export const LoginScreen = () => {
  const setAuth = useStore(state => state.setAuth);

  const [request, response, promptAsync] = AuthSession.useAuthRequest(
    {
      clientId: TWITCH_CLIENT_ID,
      redirectUri,
      responseType: AuthSession.ResponseType.Token,
      scopes: ['user:read:email'],
    },
    discovery
  );

  useEffect(() => {
    if (response?.type === 'success') {
      const { access_token } = response.params;
      setupAxiosInterceptors(access_token);
      // Fetch the real user from Twitch
      fetchCurrentUser().then(user => {
        setAuth(access_token, user);
      }).catch(err => console.error("API Error", err));
    }
  }, [response]);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>OMLET WORKSTATION</Text>
      <Text style={styles.subtitle}>LINK YOUR TWITCH ACCOUNT</Text>
      
      <TouchableOpacity 
        style={styles.button} 
        disabled={!request} 
        onPress={() => promptAsync()}
      >
        <Text style={styles.buttonText}>AUTHORIZE VIA TWITCH</Text>
      </TouchableOpacity>
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
    marginBottom: 8,
  },
  subtitle: {
    ...Typography.body,
    marginBottom: 40,
    color: Theme.textSecondary
  },
  button: {
    backgroundColor: Theme.primary,
    paddingVertical: 16,
    paddingHorizontal: 32,
    borderWidth: 1,
    borderColor: '#B37AFF',
  },
  buttonText: {
    ...Typography.label,
    color: '#FFFFFF'
  }
});
