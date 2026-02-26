import React, { useState, useEffect } from 'react';
import { BarChart3, Loader, AlertCircle } from 'lucide-react';
import apiService from '../services/apiService';
import { ShortenedURL } from '../types';
import { AnalyticsCard } from '../components/AnalyticsCard';
import { AnalyticsDetail } from '../components/AnalyticsDetail';

export const AnalyticsPage: React.FC = () => {
  const [urls, setUrls] = useState<ShortenedURL[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedURLId, setSelectedURLId] = useState<number | null>(null);
  const [sortBy, setSortBy] = useState<'clicks' | 'date'>('clicks');

  useEffect(() => {
    fetchURLs();
  }, []);

  const fetchURLs = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await apiService.getAllURLs();
      setUrls(data);
    } catch (err: any) {
      setError(
        err.response?.data?.message || 'Failed to load URLs. Make sure the backend is running.'
      );
      console.error('Error fetching URLs:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (urlId: number) => {
    if (window.confirm('Are you sure you want to delete this shortened URL?')) {
      try {
        await apiService.deleteURL(urlId);
        setUrls(urls.filter((u) => u.id !== urlId));
      } catch (err: any) {
        alert('Failed to delete URL: ' + (err.response?.data?.message || 'Unknown error'));
      }
    }
  };

  const handleViewDetails = (urlId: number) => {
    const selectedURL = urls.find((u) => u.id === urlId);
    if (selectedURL) {
      setSelectedURLId(urlId);
    }
  };

  const filteredAndSortedURLs = urls
    .filter((url) =>
      url.long_url.toLowerCase().includes(searchTerm.toLowerCase()) ||
      url.short_code.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .sort((a, b) => {
      if (sortBy === 'clicks') {
        return b.total_clicks - a.total_clicks;
      } else {
        return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
      }
    });

  // If viewing details, show analytics detail page
  if (selectedURLId) {
    const selectedURL = urls.find((u) => u.id === selectedURLId);
    if (selectedURL) {
      return (
        <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 py-12 px-4 sm:px-6 lg:px-8">
          <div className="max-w-7xl mx-auto">
            <AnalyticsDetail
              shortCode={selectedURL.short_code}
              onBack={() => setSelectedURLId(null)}
            />
          </div>
        </div>
      );
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center space-x-3 mb-4">
            <BarChart3 className="w-8 h-8 text-primary-600" />
            <h1 className="text-4xl font-bold text-gray-900">Your URLs Analytics</h1>
          </div>
          <p className="text-gray-600">
            Monitor your shortened links and track user engagement in real-time
          </p>
        </div>

        {/* Error Message */}
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-300 rounded-lg flex items-start space-x-3">
            <AlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
            <div>
              <p className="font-semibold text-red-800">Error Loading Analytics</p>
              <p className="text-sm text-red-700">{error}</p>
              <button
                onClick={fetchURLs}
                className="mt-2 px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg text-sm font-medium transition-colors"
              >
                Retry
              </button>
            </div>
          </div>
        )}

        {/* Controls */}
        {!error && urls.length > 0 && (
          <div className="bg-white rounded-lg border border-gray-200 p-6 mb-6 shadow-sm">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Search */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Search URLs
                </label>
                <input
                  type="text"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  placeholder="Search by URL or short code..."
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                />
              </div>

              {/* Sort */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Sort By
                </label>
                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value as 'clicks' | 'date')}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                >
                  <option value="clicks">Most Clicks</option>
                  <option value="date">Most Recent</option>
                </select>
              </div>
            </div>
          </div>
        )}

        {/* Content */}
        {loading ? (
          <div className="flex items-center justify-center h-96">
            <div className="text-center">
              <Loader className="w-12 h-12 text-primary-600 animate-spin mx-auto mb-4" />
              <p className="text-gray-600 font-medium">Loading your analytics...</p>
            </div>
          </div>
        ) : urls.length === 0 ? (
          <div className="bg-white rounded-lg border-2 border-dashed border-gray-300 p-12 text-center">
            <BarChart3 className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h2 className="text-2xl font-bold text-gray-900 mb-2">No URLs Yet</h2>
            <p className="text-gray-600 mb-6">
              You haven't created any shortened URLs yet. Go to the Shorten URL page to get started!
            </p>
          </div>
        ) : filteredAndSortedURLs.length === 0 ? (
          <div className="bg-white rounded-lg border border-gray-200 p-12 text-center">
            <AlertCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h2 className="text-xl font-bold text-gray-900 mb-2">No Results Found</h2>
            <p className="text-gray-600">No URLs match your search criteria.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Stats Summary */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
                <p className="text-gray-600 text-sm font-medium">Total URLs</p>
                <p className="text-3xl font-bold text-gray-900 mt-2">{urls.length}</p>
              </div>
              <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
                <p className="text-gray-600 text-sm font-medium">Total Clicks</p>
                <p className="text-3xl font-bold text-gray-900 mt-2">
                  {urls.reduce((sum, url) => sum + url.total_clicks, 0).toLocaleString()}
                </p>
              </div>
              <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
                <p className="text-gray-600 text-sm font-medium">Avg Clicks per URL</p>
                <p className="text-3xl font-bold text-gray-900 mt-2">
                  {Math.round(
                    urls.reduce((sum, url) => sum + url.total_clicks, 0) / urls.length
                  )}
                </p>
              </div>
            </div>

            {/* URLs Grid */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {filteredAndSortedURLs.map((url) => (
                <AnalyticsCard
                  key={url.id}
                  url={url}
                  onDelete={handleDelete}
                  onViewDetails={handleViewDetails}
                />
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
