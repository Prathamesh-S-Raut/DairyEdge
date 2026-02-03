import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { api } from '../../services/api';

export const AdminOrdersPage = () => {
  const { token } = useAuth();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    api.getAllOrders(token).then(res => res.json()).then(setOrders);
  }, [token]);

  const updateStatus = async (orderId, status) => {
    await api.updateOrderStatus(token, orderId, status);
    api.getAllOrders(token).then(res => res.json()).then(setOrders);
  };

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

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h2 className="text-3xl font-bold text-gray-800 mb-6">All Orders</h2>
      
      <div className="space-y-4">
        {orders.map(order => (
          <div key={order.orderId} className="bg-white rounded-xl shadow-md p-6">
            <div className="flex justify-between items-start mb-4">
              <div>
                <p className="text-sm text-gray-500">Order #{order.orderId}</p>
                <p className="text-sm text-gray-500">{new Date(order.orderDate).toLocaleDateString()}</p>
                <p className="text-sm text-gray-500">User ID: {order.userId}</p>
              </div>
              <div className="text-right">
                <span className={`px-4 py-2 rounded-full font-semibold ${getStatusColor(order.orderStatus)}`}>
                  {order.orderStatus}
                </span>
                <p className="text-2xl font-bold text-green-600 mt-2">₹{order.totalAmount?.toFixed(2)}</p>
              </div>
            </div>
            
            <div className="space-y-2 mb-4">
              {order.orderItems?.map((item, idx) => (
                <div key={idx} className="flex justify-between items-center py-2 border-b">
                  <p className="font-medium text-gray-800">{item.productName} x {item.quantity}</p>
                  <p className="font-semibold">₹{item.price?.toFixed(2)}</p>
                </div>
              ))}
            </div>
            
            <div className="flex flex-wrap gap-2">
              {['CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'].map(status => (
                <button
                  key={status}
                  onClick={() => updateStatus(order.orderId, status)}
                  disabled={order.orderStatus === status}
                  className={`px-4 py-2 rounded-lg font-medium transition ${order.orderStatus === status ? 'bg-gray-200 text-gray-400 cursor-not-allowed' : 'bg-green-100 text-green-700 hover:bg-green-200'}`}
                >
                  Mark as {status}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};