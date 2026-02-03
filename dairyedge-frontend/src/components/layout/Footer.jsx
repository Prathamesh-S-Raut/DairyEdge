import React from 'react';
import { Milk, Mail, Phone, MapPin, Facebook, Twitter, Instagram, Linkedin } from 'lucide-react';

export const Footer = () => {
  return (
    <footer className="bg-gray-900 text-gray-300 text-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

          {/* Brand Section */}
          <div className="flex flex-col space-y-3">
            <div className="flex items-center space-x-2">
              <div className="p-1.5 bg-green-600 rounded-lg">
                <Milk className="w-6 h-6 text-white" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-white">DairyEdge</h3>
                <p className="text-xs text-green-400">Fresh from Farm</p>
              </div>
            </div>
            <p className="text-xs text-gray-400">
              Your trusted source for fresh, high-quality dairy products delivered straight from local farms.
            </p>
            <div className="flex space-x-2">
              <a href="#" className="p-1.5 bg-gray-800 rounded-lg hover:bg-green-600 transition">
                <Facebook className="w-4 h-4" />
              </a>
              <a href="#" className="p-1.5 bg-gray-800 rounded-lg hover:bg-green-600 transition">
                <Twitter className="w-4 h-4" />
              </a>
              <a href="#" className="p-1.5 bg-gray-800 rounded-lg hover:bg-green-600 transition">
                <Instagram className="w-4 h-4" />
              </a>
              <a href="#" className="p-1.5 bg-gray-800 rounded-lg hover:bg-green-600 transition">
                <Linkedin className="w-4 h-4" />
              </a>
            </div>
          </div>

          {/* Customer Service */}
          <div className="flex flex-col space-y-2">
            <h4 className="text-sm font-semibold text-white">Customer Service</h4>
            <ul className="space-y-1">
              <li><a href="#" className="hover:text-green-400 transition">Help Center</a></li>
              <li><a href="#" className="hover:text-green-400 transition">Track Order</a></li>
              <li><a href="#" className="hover:text-green-400 transition">Returns & Refunds</a></li>
              <li><a href="#" className="hover:text-green-400 transition">Shipping Info</a></li>
              <li><a href="#" className="hover:text-green-400 transition">FAQs</a></li>
            </ul>
          </div>

          {/* Contact Info */}
          <div className="flex flex-col space-y-2">
            <h4 className="text-sm font-semibold text-white">Contact Us</h4>
            <ul className="space-y-1">
              <li className="flex items-center space-x-2">
                <MapPin className="w-4 h-4 text-green-400" />
                <span>Nagpur, Maharashtra</span>
              </li>
              <li className="flex items-center space-x-2">
                <Phone className="w-4 h-4 text-green-400" />
                <span>+91 98765 43210</span>
              </li>
              <li className="flex items-center space-x-2">
                <Mail className="w-4 h-4 text-green-400" />
                <span>support@dairyedge.com</span>
              </li>
            </ul>
            <p className="text-xs text-gray-400 mt-2">
              Mon - Sat: 6:00 AM - 8:00 PM | Sun: 7:00 AM - 6:00 PM
            </p>
          </div>

        </div>

        {/* Bottom Bar */}
        <div className="border-t border-gray-800 mt-6 pt-4">
          <div className="flex flex-col md:flex-row justify-between items-center space-y-2 md:space-y-0">
            <p className="text-xs text-gray-400">
              © {new Date().getFullYear()} DairyEdge. All rights reserved.
            </p>
            <div className="flex space-x-4">
              <a href="#" className="hover:text-green-400 transition">Privacy Policy</a>
              <a href="#" className="hover:text-green-400 transition">Terms of Service</a>
              <a href="#" className="hover:text-green-400 transition">Cookie Policy</a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};
