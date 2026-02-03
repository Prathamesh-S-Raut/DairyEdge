// src/pages/user/HomePage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Milk, Package } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export const HomePage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const handleShopNow = () => {
    if (user) {
      navigate('/products'); // logged in → products page
    } else {
      navigate('/login');    // not logged in → login page
    }
  };

  const handleLogin = () => {
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <section className="bg-green-50 py-20">
        <div className="max-w-7xl mx-auto text-center px-4">
          <h1 className="text-5xl font-bold text-green-700 mb-4">
            DairyEdge – Fresh, Organic, Trusted
          </h1>
          <p className="text-lg text-gray-700 mb-8">
            Get pure and healthy dairy products delivered right to your doorstep.
          </p>

          <div className="flex justify-center gap-4">
            <button
              onClick={handleShopNow}
              className="bg-green-600 text-white px-6 py-3 rounded-lg text-lg font-semibold hover:bg-green-700 transition"
            >
              Shop Now
            </button>

            {!user && (
              <button
                onClick={handleLogin}
                className="bg-white border border-green-600 text-green-600 px-6 py-3 rounded-lg text-lg font-semibold hover:bg-green-100 transition"
              >
                Login
              </button>
            )}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
          <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition">
            <Milk className="w-16 h-16 text-green-600 mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Pure Milk</h3>
            <p className="text-gray-600">Sourced from trusted farms, always fresh.</p>
          </div>

          <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition">
            <Package className="w-16 h-16 text-green-600 mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Fast Delivery</h3>
            <p className="text-gray-600">Get your dairy essentials delivered quickly and safely.</p>
          </div>

          <div className="bg-white rounded-xl shadow-md p-6 hover:shadow-xl transition">
            <Milk className="w-16 h-16 text-green-600 mx-auto mb-4" />
            <h3 className="text-xl font-semibold mb-2">Organic Products</h3>
            <p className="text-gray-600">Healthy dairy products with no preservatives.</p>
          </div>
        </div>
      </section>
    </div>
  );
};
