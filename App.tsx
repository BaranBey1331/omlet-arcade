import React from 'react';
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
