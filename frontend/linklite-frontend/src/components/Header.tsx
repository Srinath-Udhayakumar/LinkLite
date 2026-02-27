import React from 'react';
import { Link, BarChart3 } from 'lucide-react';

interface HeaderProps {
  currentPage: 'home' | 'analytics';
  onNavigate: (page: 'home' | 'analytics') => void;
}

export const Header: React.FC<HeaderProps> = ({ currentPage, onNavigate }) => {
  return (
    <header className="sticky top-0 z-50 bg-white border-b border-gray-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo and Brand */}
          <div className="flex items-center space-x-2">
            <Link className="w-8 h-8 text-primary-600" />
            <div className="flex flex-col">
              <h1 className="text-xl font-bold text-gray-900">URLSnip</h1>
              <p className="text-xs text-gray-500">URL Shortener & Analytics</p>
            </div>
          </div>

          {/* Navigation */}
          <nav className="flex space-x-1">
            <button
              onClick={() => onNavigate('home')}
              className={`px-4 py-2 rounded-lg font-medium transition-all flex items-center space-x-2 ${
                currentPage === 'home'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <Link className="w-4 h-4" />
              <span>Shorten URL</span>
            </button>
            <button
              onClick={() => onNavigate('analytics')}
              className={`px-4 py-2 rounded-lg font-medium transition-all flex items-center space-x-2 ${
                currentPage === 'analytics'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <BarChart3 className="w-4 h-4" />
              <span>Analytics</span>
            </button>
          </nav>

          {/* Info Badge */}
          <div className="hidden md:flex items-center px-3 py-1 bg-green-50 rounded-full">
            <div className="w-2 h-2 bg-green-500 rounded-full mr-2"></div>
            <span className="text-sm text-green-700 font-medium">API Connected</span>
          </div>
        </div>
      </div>
    </header>
  );
};
