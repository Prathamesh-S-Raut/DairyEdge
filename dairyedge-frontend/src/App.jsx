// src/App.jsx
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { api } from './services/api';

// Layout Components
import { HeaderWithRouter } from './components/layout/HeaderWithRouter';
import { Footer } from './components/layout/Footer';

// Auth Pages
import { LoginPage } from './pages/auth/LoginPage';
import { SignupPage } from './pages/auth/SignupPage';

// User Pages
import { HomePage } from './pages/user/HomePage';
import { ProductsPage } from './pages/user/ProductsPage';
import { CartPage } from './pages/user/CartPage';
import { OrdersPage } from './pages/user/OrderPage';

// Admin Pages
import { AdminProductsPage } from './components/admin/AdminProductsPage';
import { AdminOrdersPage } from './components/admin/AdminOrdersPage';
import { AdminSuppliersPage } from './components/admin/AdminSuppliersPage';

// Common Components
import { ProtectedRoute } from './components/common/ProtectedRoute';
import { PublicRoute } from './components/common/PublicRoute';
import { LoadingSpinner } from './components/common/LoadingSpinner';

const AppContent = () => {
  const { user, loading, isAdmin, token } = useAuth();
  const location = useLocation();
  const [cartCount, setCartCount] = useState(0);

  // Pages where header/footer should NOT show
  const noHeaderFooter = ['/login', '/signup'];

  // Fetch cart count for logged-in users
  const updateCartCount = async () => {
    if (user && token && !isAdmin) {
      try {
        const res = await api.getCart(token);
        const data = await res.json();
        setCartCount(data?.length || 0); // data is array of cart items
      } catch (error) {
        setCartCount(0);
      }
    }
  };

  useEffect(() => {
    updateCartCount();
  }, [user, token, isAdmin]);

  if (loading) return <LoadingSpinner />;

  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      {/* Header only if not login/signup */}
      {!noHeaderFooter.includes(location.pathname) && <HeaderWithRouter cartCount={cartCount} />}

      <main className="flex-grow">
        <Routes>
          {/* Public Pages */}
          <Route path="/" element={<HomePage />} />

          <Route
            path="/login"
            element={
              <PublicRoute>
                <LoginPage />
              </PublicRoute>
            }
          />
          <Route
            path="/signup"
            element={
              <PublicRoute>
                <SignupPage />
              </PublicRoute>
            }
          />

          {/* User Pages */}
          <Route
            path="/products"
            element={
              <ProtectedRoute>
                <ProductsPage onCartUpdate={updateCartCount} />
              </ProtectedRoute>
            }
          />
          <Route
            path="/cart"
            element={
              <ProtectedRoute>
                <CartPage onCartUpdate={updateCartCount} />
              </ProtectedRoute>
            }
          />
          <Route
            path="/orders"
            element={
              <ProtectedRoute>
                <OrdersPage />
              </ProtectedRoute>
            }
          />

          {/* Admin Pages */}
          <Route
            path="/admin/products"
            element={
              <ProtectedRoute requireAdmin={true}>
                <AdminProductsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/orders"
            element={
              <ProtectedRoute requireAdmin={true}>
                <AdminOrdersPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/suppliers"
            element={
              <ProtectedRoute requireAdmin={true}>
                <AdminSuppliersPage />
              </ProtectedRoute>
            }
          />

          {/* 404 Page */}
          <Route
            path="*"
            element={
              <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                  <h1 className="text-6xl font-bold text-gray-800 mb-4">404</h1>
                  <p className="text-xl text-gray-600">Page not found</p>
                </div>
              </div>
            }
          />
        </Routes>

      </main>

      {/* Footer only if not login/signup */}
      {!noHeaderFooter.includes(location.pathname) && <Footer />}
    </div>
  );
};

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

export default App;
