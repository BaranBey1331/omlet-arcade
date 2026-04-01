export const Theme = {
  background: '#000000',
  surface: '#111111',
  border: '#222222',
  primary: '#9146FF', // Twitch Purple
  textPrimary: '#FFFFFF',
  textSecondary: '#AAAAAA',
  danger: '#FF0055'
};

export const Typography = {
  title: {
    fontFamily: 'System', // Fallback to system sans-serif
    fontWeight: '900' as const,
    fontSize: 24,
    color: Theme.textPrimary
  },
  subtitle: {
    fontFamily: 'System',
    fontWeight: 'bold' as const,
    fontSize: 16,
    color: Theme.textPrimary
  },
  body: {
    fontFamily: 'System',
    fontWeight: 'normal' as const,
    fontSize: 14,
    color: Theme.textSecondary
  },
  label: {
    fontFamily: 'System',
    fontWeight: 'bold' as const,
    fontSize: 10,
    letterSpacing: 1,
    color: Theme.textPrimary
  }
};
