import React from 'react';
import Svg, { Path, Circle, Defs, LinearGradient, Stop } from 'react-native-svg';

interface Props {
  width?: number;
  height?: number;
  color?: string;
}

export const OmletLogo: React.FC<Props> = ({ width = 64, height = 64, color = '#9146FF' }) => (
  <Svg width={width} height={height} viewBox="0 0 100 100" fill="none">
    <Defs>
      <LinearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
        <Stop offset="0%" stopColor={color} stopOpacity="1" />
        <Stop offset="100%" stopColor="#B37AFF" stopOpacity="0.8" />
      </LinearGradient>
    </Defs>
    
    {/* Outer Sleek Ring */}
    <Circle cx="50" cy="50" r="45" stroke="url(#grad)" strokeWidth="6" opacity="0.3" />
    
    {/* Inner Dynamic Shapes (Stylized Omelet/Gamepad) */}
    <Path 
      d="M30 65 Q 50 85 70 65 Q 85 45 65 30 Q 45 15 30 35 Q 15 50 30 65 Z" 
      fill="url(#grad)" 
      opacity="0.9"
    />
    
    {/* Premium Accents */}
    <Circle cx="45" cy="45" r="8" fill="#1A1A1D" />
    <Circle cx="65" cy="55" r="5" fill="#1A1A1D" />
  </Svg>
);
