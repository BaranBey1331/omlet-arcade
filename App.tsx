import React, { useEffect, useState } from 'react';
import { View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useStore } from './src/store';
import { LoginScreen } from './src/screens/LoginScreen';
import { HomeScreen } from './src/screens/HomeScreen';
import { StreamScreen } from './src/screens/StreamScreen';
import { StatusBar } from 'expo-status-bar';

const Stack = createNativeStackNavigator();

export default function App() {
  const accessToken = useStore(state => state.accessToken);
  const _hasHydrated = useStore(state => state._hasHydrated);

  if (!_hasHydrated) {
    // Return a blank black screen while local storage loads to prevent flashing the login screen.
    return <View style={{ flex: 1, backgroundColor: '#000' }} />;
  }

  return (
    <NavigationContainer>
      <StatusBar style="light" />
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {accessToken == null ? (
          // Unauthenticated Flow
          <Stack.Screen name="Login" component={LoginScreen} />
        ) : (
          // Authenticated Flow
          <>
            <Stack.Screen name="Home" component={HomeScreen} />
            <Stack.Screen name="Stream" component={StreamScreen} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}
