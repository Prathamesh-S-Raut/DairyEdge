import React, { useState, useEffect } from 'react';
import { Package } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { api } from '../../services/api';

export const OrdersPage = () => {
  const { token } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getUserOrders(token)
      .then(res => res.json())
      .then(data => {
        setOrders(data);
        setLoading(false);
      });
  }, [token]);

  const getStatusColor = (status) => {
    const colors = {
      PLACED: 'bg-blue-100 text-blue-600',
      CONFIRMED: 'bg-purple-100 text-purple-600',
      SHIPPED: 'bg-yellow-100 text-yellow-600',
      DELIVERED: 'bg-green-100 text-green-600',
      CANCELLED: 'bg-red-100 text-red-600'
    };
    return colors[status] || 'bg-gray-100 text-gray-600';
  };

  if (loading) return <div className="text-center py-12">Loading orders...</div>;

  if (orders.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
        <Package className="w-24 h-24 text-gray-300 mx-auto mb-4" />
        <h2 className="text-2xl font-bold text-gray-800 mb-2">No orders yet</h2>
        <p className="text-gray-600">Start shopping to place your first order!</p>
      </div>
    );
  }


  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h2 className="text-3xl font-bold text-gray-800 mb-6">My Orders</h2>
      
      <div className="space-y-4">
        {orders.map(order => (
          <div key={order.orderId} className="bg-white rounded-xl shadow-md p-6">
            <div className="flex justify-between items-start mb-4">
              <div>
                <p className="text-sm text-gray-500">Order #{order.orderId}</p>
                <p className="text-sm text-gray-500">{new Date(order.orderDate).toLocaleDateString()}</p>
              </div>
              <span className={`px-4 py-2 rounded-full font-semibold ${getStatusColor(order.orderStatus)}`}>
                {order.orderStatus}
              </span>
            </div>
            
            <div className="space-y-2 mb-4">
              {order.orderItems?.map((item, idx) => (
                <div key={idx} className="flex justify-between items-center py-2 border-b last:border-b-0">
                  <div>
                    <p className="font-medium text-gray-800">{item.productName}</p>
                    <p className="text-sm text-gray-500">Qty: {item.quantity}</p>
                  </div>
                  <p className="font-semibold text-gray-800">₹ {item.price?.toFixed(2)}</p>
                </div>
              ))}
            </div>
            
            <div className="flex justify-between items-center pt-4 border-t">
              <span className="font-semibold text-gray-700">Total Amount:</span>
              <span className="text-2xl font-bold text-green-600">₹ {order.totalAmount?.toFixed(2)}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};