import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { Toast } from '../../components/common/Toast';

export const ProductsPage = ({ onCartUpdate }) => {
  const { user, token } = useAuth();
  const navigate = useNavigate();

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [toastMessage, setToastMessage] = useState('');

  useEffect(() => {
    api.getProducts()
      .then(res => res.json())
      .then(data => {
        setProducts(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  const showToast = (msg) => setToastMessage(msg);

  const handleAddToCart = async (productId) => {
    if (!user) {
      navigate('/login');
      return;
    }
    try {
      await api.addToCart(token, { productId, quantity: 1 });
      showToast('Product added to cart!');
      if (onCartUpdate) onCartUpdate();
    } catch (error) {
      console.error('Add to cart failed:', error);
      showToast('Failed to add product to cart');
    }
  };

  const categories = ['ALL', 'MILK', 'CURD', 'BUTTER', 'CHEESE', 'GHEE', 'YOGURT'];

  const filteredProducts =
    selectedCategory === 'ALL'
      ? products
      : products.filter(p => p.category?.toUpperCase() === selectedCategory);

  if (loading) {
    return <div className="text-center py-12">Loading products...</div>;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h2 className="text-3xl font-bold text-gray-800 mb-6">
        Fresh Dairy Products
      </h2>

      {/* CATEGORY FILTER */}
      <div className="flex flex-wrap gap-2 mb-8">
        {categories.map(cat => (
          <button
            key={cat}
            onClick={() => setSelectedCategory(cat)}
            className={`px-4 py-2 rounded-full font-medium transition ${
              selectedCategory === cat
                ? 'bg-green-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* PRODUCTS GRID */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {filteredProducts.map(product => (
          <div
            key={product.id}
            className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition flex flex-col"
          >
            {/* IMAGE */}
            <div className="w-full aspect-[4/3] bg-gray-100 overflow-hidden">
              <img
                src={product.imageUrl || 'https://your-cloud-url/default.png'}
                alt={product.name}
                className="w-full h-full object-cover"
                loading="lazy"
                onError={(e) => {
                  e.target.src = 'https://your-cloud-url/default.png';
                }}
              />
            </div>

            {/* CONTENT */}
            <div className="p-4 flex flex-col flex-1">
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-bold text-gray-800">
                  {product.name}
                </h3>
                <span className="text-xs bg-green-100 text-green-600 px-2 py-1 rounded-full">
                  {product.category}
                </span>
              </div>

              <p className="text-sm text-gray-600 mb-3 line-clamp-2">
                {product.description}
              </p>

              <div className="mt-auto flex justify-between items-center">
                <div>
                  <p className="text-2xl font-bold text-green-600">
                    ₹{product.price}
                  </p>
                  <p className="text-xs text-gray-500">
                    Stock: {product.stockQuantity}
                  </p>
                </div>

                <button
                  onClick={() => handleAddToCart(product.id)}
                  disabled={product.stockQuantity === 0}
                  className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Add to Cart
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredProducts.length === 0 && (
        <div className="text-center py-12 text-gray-500">
          No products found in this category
        </div>
      )}

      {/* TOAST */}
      {toastMessage && (
        <Toast
          message={toastMessage}
          onClose={() => setToastMessage('')}
        />
      )}
    </div>
  );
};
