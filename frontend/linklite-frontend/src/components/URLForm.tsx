import React, { useState } from 'react';
import { ArrowRight, Copy, Check, AlertCircle, Loader } from 'lucide-react';
import apiService from '../services/apiService';
import { CreateURLResponse } from '../types';

export const URLForm: React.FC = () => {
  const [longURL, setLongURL] = useState('');
  const [shortCode, setShortCode] = useState('');
  const [shortenedURL, setShortenedURL] = useState('');
  const [copied, setCopied] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const isValidURL = (url: string): boolean => {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  };

  const handleShorten = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess(false);

    // Validation
    if (!longURL.trim()) {
      setError('Please enter a URL to shorten');
      return;
    }

    if (!isValidURL(longURL)) {
      setError('Please enter a valid URL (e.g., https://example.com)');
      return;
    }

    setLoading(true);

    try {
      const response: CreateURLResponse = await apiService.shortenURL({
        long_url: longURL,
      });

      setShortCode(response.short_code);
      setShortenedURL(response.shortened_url);
      setSuccess(true);
      setLongURL('');

      // Auto-hide success message after 5 seconds
      setTimeout(() => setSuccess(false), 5000);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Failed to shorten URL. Please try again.';
      setError(errorMessage);
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = () => {
    if (shortenedURL) {
      navigator.clipboard.writeText(shortenedURL);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <div className="w-full max-w-2xl mx-auto">
      {/* Main Form */}
      <div className="bg-white rounded-lg shadow-md p-8 border border-gray-100">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-2">Shorten Your URL</h2>
          <p className="text-gray-600">
            Convert long, complex URLs into short, shareable links instantly
          </p>
        </div>

        <form onSubmit={handleShorten} className="space-y-6">
          {/* URL Input */}
          <div>
            <label htmlFor="longURL" className="block text-sm font-semibold text-gray-700 mb-2">
              Long URL
            </label>
            <div className="relative flex">
              <input
                type="url"
                id="longURL"
                value={longURL}
                onChange={(e) => setLongURL(e.target.value)}
                placeholder="https://example.com/very/long/path..."
                className="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
              <button
                type="submit"
                disabled={loading}
                className="ml-2 px-6 py-3 bg-primary-600 hover:bg-primary-700 disabled:bg-primary-400 text-white font-semibold rounded-lg transition-all flex items-center space-x-2"
              >
                {loading ? (
                  <>
                    <Loader className="w-4 h-4 animate-spin" />
                    <span>Shortening...</span>
                  </>
                ) : (
                  <>
                    <ArrowRight className="w-4 h-4" />
                    <span>Shorten</span>
                  </>
                )}
              </button>
            </div>
          </div>

          {/* Error Message */}
          {error && (
            <div className="p-4 bg-red-50 border border-red-300 rounded-lg flex items-start space-x-3">
              <AlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
              <div>
                <p className="font-semibold text-red-800">Error</p>
                <p className="text-sm text-red-700">{error}</p>
              </div>
            </div>
          )}

          {/* Success Result */}
          {success && shortenedURL && (
            <div className="space-y-4 p-6 bg-gradient-to-br from-green-50 to-emerald-50 rounded-lg border border-green-200">
              <div className="flex items-center space-x-2">
                <Check className="w-5 h-5 text-green-600" />
                <p className="font-semibold text-green-800">URL Shortened Successfully!</p>
              </div>

              <div className="space-y-2">
                <p className="text-sm text-gray-600">Your shortened URL:</p>
                <div className="flex items-center space-x-2 bg-white p-3 rounded-lg border border-green-300">
                  <input
                    type="text"
                    readOnly
                    value={shortenedURL}
                    className="flex-1 bg-transparent text-primary-600 font-semibold outline-none"
                  />
                  <button
                    type="button"
                    onClick={handleCopy}
                    className="p-2 hover:bg-gray-100 rounded-lg transition-all"
                    title="Copy to clipboard"
                  >
                    {copied ? (
                      <Check className="w-5 h-5 text-green-600" />
                    ) : (
                      <Copy className="w-5 h-5 text-gray-500" />
                    )}
                  </button>
                </div>
              </div>

              <div className="pt-2 border-t border-green-200">
                <p className="text-xs text-gray-600">
                  Short code: <span className="font-mono font-bold text-green-700">{shortCode}</span>
                </p>
              </div>
            </div>
          )}
        </form>
      </div>

      {/* Info Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-8">
        <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
          <h3 className="font-semibold text-gray-900 mb-2">Fast & Reliable</h3>
          <p className="text-sm text-gray-600">
            Create shortened URLs in milliseconds with 99.9% uptime
          </p>
        </div>

        <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
          <h3 className="font-semibold text-gray-900 mb-2">Full Analytics</h3>
          <p className="text-sm text-gray-600">
            Track clicks, timestamps, and engagement metrics in real-time
          </p>
        </div>

        <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm">
          <h3 className="font-semibold text-gray-900 mb-2">Easy Sharing</h3>
          <p className="text-sm text-gray-600">
            Share short links across social media and messaging platforms
          </p>
        </div>
      </div>
    </div>
  );
};
