import { Calendar, Eye, Plus, TrendingUp } from 'lucide-react';
import React, { useEffect, useState } from 'react';
import apiService from '../services/apiService';
import { ShortenedURL } from '../types';
import { AnalyticsCard } from './AnalyticsCard';
import { Loading } from './Loading';

export const Dashboard: React.FC = () => {
  const [urls, setUrls] = useState<ShortenedURL[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [sortBy, setSortBy] = useState<'date' | 'clicks'>('date');

  useEffect(() => {
    fetchUrls();
  }, []);

  const fetchUrls = async () => {
    try {
      setLoading(true);
      const data = await apiService.getAllURLs();
      setUrls(data || []);
    } catch (err: any) {
      setError('Failed to load URLs');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this URL?')) {
      try {
        await apiService.deleteURL(id);
        setUrls(urls.filter((url) => url.id !== id));
      } catch (err) {
        alert('Failed to delete URL '+err);
      }
    }
  };

  const handleViewDetails = (url: ShortenedURL) => {
    // Navigate to analytics page for this URL
    window.location.href = `/analytics/${url.short_code}`;
  };

  const sortedUrls = [...urls].sort((a, b) => {
    if (sortBy === 'clicks') {
      return (b.total_clicks || 0) - (a.total_clicks || 0);
    }
    return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
  });

  if (loading) {
    return <Loading message="Loading your URLs..." />;
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-800 rounded-lg p-8 text-white">
        <h1 className="text-4xl font-bold mb-2">Dashboard</h1>
        <p className="text-primary-100">
          Manage your shortened URLs and track their performance
        </p>
      </div>

      {/* Stats Cards */}
      {urls.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Total URLs</p>
                <p className="text-3xl font-bold text-gray-900">{urls.length}</p>
              </div>
              <Plus className="w-12 h-12 text-primary-500 opacity-20" />
            </div>
          </div>

          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Total Clicks</p>
                <p className="text-3xl font-bold text-gray-900">
                  {urls.reduce((sum, url) => sum + (url.total_clicks || 0), 0)}
                </p>
              </div>
              <Eye className="w-12 h-12 text-blue-500 opacity-20" />
            </div>
          </div>

          <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm">Avg Clicks/URL</p>
                <p className="text-3xl font-bold text-gray-900">
                  {urls.length > 0
                    ? (
                        urls.reduce((sum, url) => sum + (url.total_clicks || 0), 0) /
                        urls.length
                      ).toFixed(1)
                    : 0}
                </p>
              </div>
              <TrendingUp className="w-12 h-12 text-green-500 opacity-20" />
            </div>
          </div>
        </div>
      )}

      {/* Sorting Controls */}
      {urls.length > 0 && (
        <div className="flex items-center gap-4">
          <label className="text-sm font-semibold text-gray-700">Sort by:</label>
          <div className="flex gap-2">
            <button
              onClick={() => setSortBy('date')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                sortBy === 'date'
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-200 text-gray-800 hover:bg-gray-300'
              }`}
            >
              <Calendar className="w-4 h-4 inline mr-2" />
              Date
            </button>
            <button
              onClick={() => setSortBy('clicks')}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                sortBy === 'clicks'
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-200 text-gray-800 hover:bg-gray-300'
              }`}
            >
              <Eye className="w-4 h-4 inline mr-2" />
              Clicks
            </button>
          </div>
        </div>
      )}

      {/* Error State */}
      {error && (
        <div className="bg-red-50 border border-red-300 rounded-lg p-4 text-red-700">
          {error}
        </div>
      )}

      {/* Empty State */}
      {urls.length === 0 && !error && (
        <div className="bg-white rounded-lg border border-gray-200 p-12 text-center">
          <div className="text-gray-500 mb-4">
            <Eye className="w-16 h-16 mx-auto opacity-20" />
          </div>
          <h3 className="text-lg font-semibold text-gray-900 mb-2">No URLs yet</h3>
          <p className="text-gray-600 mb-6">
            Create your first shortened URL to start tracking analytics
          </p>
          <button
            onClick={() => window.location.href = '/'}
            className="px-6 py-2 bg-primary-600 hover:bg-primary-700 text-white font-semibold rounded-lg transition-colors"
          >
            Create URL
          </button>
        </div>
      )}

      {/* URLs Grid */}
      <div className="grid grid-cols-1 gap-4">
        {sortedUrls.map((url) => (
          <AnalyticsCard
            key={url.id}
            url={url}
            onDelete={() => handleDelete(url.id)}
            onViewDetails={() => handleViewDetails(url)}
          />
        ))}
      </div>
    </div>
  );
};

export default Dashboard;
