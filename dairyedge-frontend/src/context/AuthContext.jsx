import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../services/api';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (token) {
      api.getCurrentUser(token)
        .then(res => res.json())
        .then(data => setUser(data))
        .catch(() => logout())
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [token]);

  const login = async (email, password) => {
    const res = await api.signin({ email, password });
    const data = await res.json();
    if (res.ok) {
      localStorage.setItem('token', data.jwt);
      setToken(data.jwt);
      return { success: true };
    }
    return { success: false, message: data.message };
  };

  const signup = async (userData) => {
    const res = await api.signup(userData);
    const data = await res.json();
    return { success: res.ok, message: data.message };
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  const isAdmin = user?.email && token ? (() => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.user_role === 'ROLE_ADMIN';
    } catch {
      return false;
    }
  })() : false;

  return (
    <AuthContext.Provider value={{ user, token, login, signup, logout, loading, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};