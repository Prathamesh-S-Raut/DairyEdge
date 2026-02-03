import React from 'react';
import { Milk } from 'lucide-react';

export const LoadingSpinner = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 to-emerald-100">
      <div className="text-center">
        <Milk className="w-16 h-16 text-green-600 mx-auto mb-4 animate-pulse" />
        <p className="text-gray-600">Loading...</p>
      </div>
    </div>
  );
};