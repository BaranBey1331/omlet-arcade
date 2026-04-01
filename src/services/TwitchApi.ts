import axios from 'axios';

// Replace with your real Client ID. Since this is a prototype, we use a public one or require the user to provide one.
// The redirect URI configured in Twitch Developer Console must match the Expo scheme: omlet-arcade://
export const TWITCH_CLIENT_ID = 'gp762nuuoqcoxypju8c569th9wz7q5';

const helix = axios.create({
  baseURL: 'https://api.twitch.tv/helix',
});

// Attach tokens dynamically
export const setupAxiosInterceptors = (token: string) => {
  helix.interceptors.request.use(config => {
    config.headers['Client-ID'] = TWITCH_CLIENT_ID;
    config.headers['Authorization'] = `Bearer ${token}`;
    return config;
  });
};

export const fetchCurrentUser = async () => {
  const { data } = await helix.get('/users');
  return data.data[0];
};

export const fetchLiveStreams = async (language?: string) => {
  // Fetch live streams. If the user has a specific language, filter by it for personalization.
  const query = language ? `?language=${language}` : '';
  const { data } = await helix.get(`/streams${query}`);
  return data.data;
};
