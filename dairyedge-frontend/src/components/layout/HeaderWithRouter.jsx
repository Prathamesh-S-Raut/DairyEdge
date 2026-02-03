import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { ShoppingCart, Package, LogOut, Menu, X, Home, Users, Box, ShoppingBag, Milk } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export const HeaderWithRouter = ({ cartCount }) => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <header className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center py-4">

          {/* Logo */}
          <Link to={isAdmin ? "/admin/products" : "/products"} className="flex items-center space-x-3">
            <div className="p-2 bg-green-100 rounded-lg">
              <Milk className="w-8 h-8 text-green-600" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-gray-800">DairyEdge</h1>
              <p className="text-xs text-gray-500">Fresh from Farm</p>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex space-x-4">
            <Link to="/" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
              <Home className="w-5 h-5" />
              <span>Home</span>
            </Link>

            {!isAdmin && (
              <>
                <Link to="/products" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/products') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <Milk className="w-5 h-5" />
                  <span>Products</span>
                </Link>

                <Link to="/cart" className={`relative flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/cart') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <ShoppingCart className="w-5 h-5" />
                  <span>Cart</span>
                  {cartCount > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">{cartCount}</span>
                  )}
                </Link>

                <Link to="/orders" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/orders') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <Package className="w-5 h-5" />
                  <span>Orders</span>
                </Link>
              </>
            )}

            {isAdmin && (
              <>
                <Link to="/admin/products" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/admin/products') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <Box className="w-5 h-5" />
                  <span>Products</span>
                </Link>

                <Link to="/admin/orders" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/admin/orders') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <ShoppingBag className="w-5 h-5" />
                  <span>Orders</span>
                </Link>

                <Link to="/admin/suppliers" className={`flex items-center space-x-1 px-3 py-2 rounded-lg transition ${isActive('/admin/suppliers') ? 'bg-green-100 text-green-600' : 'text-gray-600 hover:bg-gray-100'}`}>
                  <Users className="w-5 h-5" />
                  <span>Suppliers</span>
                </Link>
              </>
            )}
          </nav>

          {/* User Info + Mobile Menu */}
          <div className="flex items-center space-x-3">
            <div className="hidden md:flex items-center space-x-3">
              <div className="text-right">
                <p className="text-sm font-semibold text-gray-800">{user?.name}</p>
                <p className="text-xs text-gray-500">{isAdmin ? 'Admin' : 'Customer'}</p>
              </div>
              <button onClick={handleLogout} className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition">
                <LogOut className="w-5 h-5" />
              </button>
            </div>

            {/* Mobile Menu Button */}
            <button onClick={() => setMenuOpen(!menuOpen)} className="md:hidden p-2 text-gray-600">
              {menuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>

        {/* Mobile Navigation */}
        {menuOpen && (
          <div className="md:hidden pb-4 space-y-2">
            <Link to="/" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Home</Link>

            {!isAdmin && (
              <>
                <Link to="/products" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Products</Link>
                <Link to="/cart" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Cart ({cartCount})</Link>
                <Link to="/orders" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Orders</Link>
              </>
            )}

            {isAdmin && (
              <>
                <Link to="/admin/products" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Products</Link>
                <Link to="/admin/orders" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Orders</Link>
                <Link to="/admin/suppliers" onClick={() => setMenuOpen(false)} className="block w-full text-left px-4 py-2 rounded-lg hover:bg-gray-100">Suppliers</Link>
              </>
            )}

            <button onClick={handleLogout} className="w-full text-left px-4 py-2 text-red-600 rounded-lg hover:bg-red-50">Logout</button>
          </div>
        )}
      </div>
    </header>
  );
};
