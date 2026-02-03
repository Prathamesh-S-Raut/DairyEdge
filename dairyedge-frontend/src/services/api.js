const API_BASE_URL = 'http://localhost:8080';

// Helper function for error handling
const handleResponse = async (response) => {
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Request failed' }));
    console.error('API Error:', error);
    throw new Error(error.message || 'Request failed');
  }
  return response.json();
};

export const api = {
  // Auth APIs
  signup: (data) => fetch(`${API_BASE_URL}/users/signup`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  }),
  
  signin: (data) => fetch(`${API_BASE_URL}/users/signin`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  }),
  
  getCurrentUser: (token) => fetch(`${API_BASE_URL}/users/me`, {
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  // Product APIs
  getProducts: () => fetch(`${API_BASE_URL}/products`),
  
  getProduct: (id) => fetch(`${API_BASE_URL}/products/${id}`),
  
  addProduct: (token, data) => fetch(`${API_BASE_URL}/products`, {
    method: 'POST',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  }),
  
  updateProduct: (token, id, data) => fetch(`${API_BASE_URL}/products/${id}`, {
    method: 'PUT',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  }),
  
  deleteProduct: (token, id) => fetch(`${API_BASE_URL}/products/${id}`, {
    method: 'DELETE',
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  // Cart APIs - Updated to match new CartController
  getCart: async (token) => {
    console.log('Getting cart for authenticated user');
    const response = await fetch(`${API_BASE_URL}/cart`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return response;
  },
  
  addToCart: async (token, data) => {
    console.log('Adding to cart:', data);
    console.log('API URL:', `${API_BASE_URL}/cart/add`);
    
    try {
      const response = await fetch(`${API_BASE_URL}/cart/add`, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(data)
      });
      
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Error response:', errorText);
        throw new Error(`Failed to add to cart: ${response.status}`);
      }
      
      return response;
    } catch (error) {
      console.error('Add to cart error:', error);
      throw error;
    }
  },
  
  updateCartItem: async (token, cartItemId, quantity) => {
    console.log('Updating cart item:', { cartItemId, quantity });
    
    const response = await fetch(`${API_BASE_URL}/cart/item/${cartItemId}`, {
      method: 'PATCH',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ quantity })
    });
    
    return response;
  },
  
  removeCartItem: async (token, cartItemId) => {
    console.log('Removing cart item:', cartItemId);
    
    const response = await fetch(`${API_BASE_URL}/cart/item/${cartItemId}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    
    return response;
  },
  
  // Order APIs
  placeOrder: (token) =>
  fetch(`${API_BASE_URL}/orders/place`, {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` }
  }).then(handleResponse),


  
  getUserOrders: (token) => fetch(`${API_BASE_URL}/orders/user`, {
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  getAllOrders: (token) => fetch(`${API_BASE_URL}/orders`, {
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  updateOrderStatus: (token, orderId, status) => fetch(`${API_BASE_URL}/orders/${orderId}/status?status=${status}`, {
    method: 'PATCH',
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  // Supplier APIs
  getSuppliers: () => fetch(`${API_BASE_URL}/suppliers`),
  
  addSupplier: (token, data) => fetch(`${API_BASE_URL}/suppliers`, {
    method: 'POST',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  }),
  
  updateSupplier: (token, id, data) => fetch(`${API_BASE_URL}/suppliers/${id}`, {
    method: 'PUT',
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  }),
  
  deleteSupplier: (token, id) => fetch(`${API_BASE_URL}/suppliers/${id}`, {
    method: 'DELETE',
    headers: { 'Authorization': `Bearer ${token}` }
  }),
  
  // Initiate payment for an order (COD or ONLINE)
  initiatePayment: (token, orderId, method) =>
  fetch(`${API_BASE_URL}/payments/initiate/${orderId}?method=${method}`, {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` }
  }).then(handleResponse),

verifyRazorpayPayment: (token, orderId, payload) =>
  fetch(`${API_BASE_URL}/payments/razorpay/verify/${orderId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(payload)
  }).then(handleResponse),

getPaymentById: (token, paymentId) =>
  fetch(`${API_BASE_URL}/payments/${paymentId}`, {
    headers: { 'Authorization': `Bearer ${token}` }
  }).then(handleResponse)

};