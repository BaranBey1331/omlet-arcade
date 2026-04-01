import { create } from 'zustand';

interface User {
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

export const useStore = create<AppState>((set) => ({
  accessToken: null,
  user: null,
  setAuth: (token, user) => set({ accessToken: token, user }),
  logout: () => set({ accessToken: null, user: null }),
}));
