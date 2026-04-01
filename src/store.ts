import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import AsyncStorage from '@react-native-async-storage/async-storage';

export interface User {
  id: string;
  login: string;
  display_name: string;
  profile_image_url: string;
  broadcaster_language: string; // Used to filter streams by user's native language
}

interface AppState {
  accessToken: string | null;
  user: User | null;
  setAuth: (token: string, user: User) => void;
  logout: () => void;
}

export const useStore = create<AppState>()(
  persist(
    (set) => ({
      accessToken: null,
      user: null,
      setAuth: (token, user) => set({ accessToken: token, user }),
      logout: () => set({ accessToken: null, user: null }),
    }),
    {
      name: 'omlet-arcade-storage',
      storage: createJSONStorage(() => AsyncStorage),
    }
  )
);
