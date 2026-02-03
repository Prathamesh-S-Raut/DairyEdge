import React, { useEffect } from "react";

export const Toast = ({ message, duration = 3000, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, duration);

    return () => clearTimeout(timer);
  }, [message, duration, onClose]);

  if (!message) return null;

  return (
    <div className="fixed top-25 top-6 left-1/2 transform -translate-x-1/2 z-50 pointer-events-none">
      <div className="bg-green-600 text-white px-6 py-3 rounded-lg shadow-lg flex items-center space-x-2 pointer-events-auto animate-slide-down">
        <span>{message}</span>
        <button
          onClick={onClose}
          className="ml-2 font-bold text-white hover:text-gray-200"
        >
          ×
        </button>
      </div>
    </div>
  );
};
