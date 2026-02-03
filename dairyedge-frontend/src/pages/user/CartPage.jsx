import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ShoppingCart, Milk, X } from 'lucide-react';
import { api } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { Toast } from '../../components/common/Toast';

export const CartPage = ({ onCartUpdate }) => {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);

  // Toast state
  const [toastMessage, setToastMessage] = useState('');
  const showToast = (msg) => setToastMessage(msg);

  // Payment method state
  const [paymentMethod, setPaymentMethod] = useState('COD'); // default COD

  useEffect(() => {
    loadCart();
    // eslint-disable-next-line
  }, []);

  const loadCart = async () => {
    try {
      const res = await api.getCart(token);
      const data = await res.json();
      setCartItems(data || []);
      setLoading(false);
    } catch (error) {
      console.error('Failed to load cart:', error);
      setCartItems([]);
      setLoading(false);
      showToast('Failed to load cart');
    }
  };

  const handleUpdateQuantity = async (cartItemId, quantity) => {
    if (quantity < 1) return;
    try {
      await api.updateCartItem(token, cartItemId, quantity);
      await loadCart();
      showToast('Cart updated!');
      if (onCartUpdate) onCartUpdate();
    } catch (error) {
      console.error('Failed to update quantity:', error);
      showToast('Failed to update cart');
    }
  };

  const handleRemoveItem = async (cartItemId) => {
    try {
      await api.removeCartItem(token, cartItemId);
      await loadCart();
      showToast('Item removed from cart');
      if (onCartUpdate) onCartUpdate();
    } catch (error) {
      console.error('Failed to remove item:', error);
      showToast('Failed to remove item');
    }
  };

  const handlePlaceOrder = async () => {
    if (!cartItems.length) return;

    if (!confirm('Place this order?')) return;

    try {
      // 1️⃣ Place the order (backend returns orderId)
      const order = await api.placeOrder(token);
      const orderId = order?.orderId;

      if (!orderId) {
        throw new Error('Order ID not returned by backend');
      }

      // 2️⃣ Handle ONLINE payment
      if (paymentMethod === 'ONLINE') {
        const paymentRes = await api.initiatePayment(token, orderId, 'ONLINE');
        const { razorpayOrderId, amount, currency } = paymentRes;

        if (!razorpayOrderId) {
          throw new Error('Failed to initiate payment');
        }

        // 3️⃣ Launch Razorpay Checkout
        const options = {
          key: import.meta.env.VITE_RAZORPAY_KEY,
          amount: amount,
          currency: currency,
          name: 'DairyEdge',
          description: `Order #${orderId}`,
          order_id: razorpayOrderId,
          handler: async function (response) {
            try {
              // 4️⃣ Verify payment on backend
              await api.verifyRazorpayPayment(token, orderId, {
                razorpayPaymentId: response.razorpay_payment_id,
                razorpayOrderId: response.razorpay_order_id,
                razorpaySignature: response.razorpay_signature,
              });

              showToast('Payment successful and order placed!');
              navigate('/orders');
              if (onCartUpdate) onCartUpdate();
            } catch (err) {
              console.error('Payment verification failed:', err);
              showToast('Payment verification failed');
            }
          },
          prefill: {
            email: user?.email,
            contact: user?.phone,
          },
          theme: {
            color: '#22c55e',
          },
        };

        const rzp = new window.Razorpay(options);
        rzp.open();
      } else {
        // 5️⃣ COD flow
        showToast('Order placed successfully (COD)');
        navigate('/orders');
        if (onCartUpdate) onCartUpdate();
      }
    } catch (error) {
      console.error('Failed to place order:', error);
      showToast('Failed to place order');
    }
  };

  if (loading) return <div className="text-center py-12">Loading cart...</div>;

  if (!cartItems.length) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
        <ShoppingCart className="w-24 h-24 text-gray-300 mx-auto mb-4" />
        <h2 className="text-2xl font-bold text-gray-800 mb-2">Your cart is empty</h2>
        <p className="text-gray-600">Add some delicious dairy products to get started!</p>
        {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}
      </div>
    );
  }

  const totalAmount = cartItems.reduce(
    (sum, item) => sum + (item.totalPrice || item.price * item.quantity),
    0
  );

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h2 className="text-3xl font-bold text-gray-800 mb-6">Shopping Cart</h2>

      {/* Cart Items */}
      <div className="bg-white rounded-xl shadow-md p-6 mb-6">
        {cartItems.map(item => (
          <div key={item.cartItemId} className="flex items-center justify-between py-4 border-b last:border-b-0">
            <div className="flex items-center space-x-4">
              <div className="w-16 h-16 bg-gradient-to-br from-green-100 to-emerald-200 rounded-lg flex items-center justify-center">
                <Milk className="w-8 h-8 text-green-600" />
              </div>
              <div>
                <h3 className="font-semibold text-gray-800">{item.productName}</h3>
                <p className="text-sm text-gray-500">₹{item.price} each</p>
              </div>
            </div>

            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity - 1)}
                  className="w-8 h-8 bg-gray-100 rounded-lg hover:bg-gray-200"
                >
                  -
                </button>
                <span className="w-12 text-center font-semibold">{item.quantity}</span>
                <button
                  onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity + 1)}
                  className="w-8 h-8 bg-gray-100 rounded-lg hover:bg-gray-200"
                >
                  +
                </button>
              </div>

              <p className="font-bold text-green-600 w-24 text-right">
                ₹{(item.totalPrice || (item.price * item.quantity)).toFixed(2)}
              </p>

              <button
                onClick={() => handleRemoveItem(item.cartItemId)}
                className="text-red-600 hover:bg-red-50 p-2 rounded-lg"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Payment Selection */}
      <div className="bg-white rounded-xl shadow-md p-6 mb-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Select Payment Method</h3>
        <div className="flex gap-4">
          <label className="flex items-center gap-2">
            <input
              type="radio"
              name="payment"
              value="COD"
              checked={paymentMethod === 'COD'}
              onChange={(e) => setPaymentMethod(e.target.value)}
              className="accent-green-600"
            />
            Cash on Delivery
          </label>
          <label className="flex items-center gap-2">
            <input
              type="radio"
              name="payment"
              value="ONLINE"
              checked={paymentMethod === 'ONLINE'}
              onChange={(e) => setPaymentMethod(e.target.value)}
              className="accent-green-600"
            />
            Online Payment
          </label>
        </div>
      </div>

      {/* Total & Place Order */}
      <div className="bg-white rounded-xl shadow-md p-6">
        <div className="flex justify-between items-center mb-4">
          <span className="text-lg font-semibold text-gray-800">Total Amount:</span>
          <span className="text-3xl font-bold text-green-600">₹{totalAmount.toFixed(2)}</span>
        </div>
        <button
          onClick={handlePlaceOrder}
          className="w-full bg-green-600 text-white py-4 rounded-lg font-semibold hover:bg-green-700 transition"
        >
          Place Order
        </button>
      </div>

      {/* Toast */}
      {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}
    </div>
  );
};
