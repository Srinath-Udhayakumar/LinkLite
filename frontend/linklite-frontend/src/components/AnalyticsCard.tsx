import React, { useRef, useEffect } from 'react';
import { Eye, Calendar, TrendingUp, ExternalLink, Copy, Check, Trash2 } from 'lucide-react';
import { ShortenedURL } from '../types';

interface AnalyticsCardProps {
  url: ShortenedURL;
  onDelete?: (urlId: number) => void;
  onViewDetails?: (urlId: number) => void;
}

export const AnalyticsCard: React.FC<AnalyticsCardProps> = ({
  url,
  onDelete,
  onViewDetails,
}) => {
  const [copied, setCopied] = React.useState(false);
  const copyTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    return () => {
      if (copyTimeoutRef.current) clearTimeout(copyTimeoutRef.current);
    };
  }, []);

  const handleCopy = () => {
    const shortenedURL = `${window.location.origin}/s/${url.short_code}`;
    navigator.clipboard.writeText(shortenedURL);
    setCopied(true);
    if (copyTimeoutRef.current) clearTimeout(copyTimeoutRef.current);
    copyTimeoutRef.current = setTimeout(() => setCopied(false), 2000);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const truncateURL = (url: string, maxLength: number = 50) => {
    return url.length > maxLength ? url.substring(0, maxLength) + '...' : url;
  };

  return (
    <div className="bg-white rounded-lg border border-gray-200 shadow-sm hover:shadow-md transition-shadow p-6">
      <div className="space-y-4">
        {/* Header */}
        <div className="flex justify-between items-start gap-2">
          <div className="flex-1">
            <div className="flex items-center space-x-2 mb-2">
              <span className="inline-block px-3 py-1 bg-primary-100 text-primary-700 rounded-full text-sm font-bold font-mono">
                {url.short_code}
              </span>
              <button
                onClick={handleCopy}
                className="p-1 hover:bg-gray-100 rounded transition-colors"
                title="Copy short link"
              >
                {copied ? (
                  <Check className="w-4 h-4 text-green-500" />
                ) : (
                  <Copy className="w-4 h-4 text-gray-500" />
                )}
              </button>
            </div>
            <p className="text-sm text-gray-600 hover:text-primary-600 cursor-pointer break-all">
              {truncateURL(url.long_url)}
            </p>
            <p className="text-xs text-gray-400 mt-1">
              Created: {formatDate(url.created_at)}
            </p>
          </div>

          {/* Actions */}
          {onDelete && (
            <button
              onClick={() => onDelete(url.id)}
              className="p-2 hover:bg-red-50 rounded-lg transition-colors group"
              title="Delete URL"
            >
              <Trash2 className="w-5 h-5 text-gray-400 group-hover:text-red-600" />
            </button>
          )}
        </div>

        {/* Analytics Stats */}
        <div className="bg-gradient-to-r from-blue-50 to-cyan-50 rounded-lg p-4 border border-blue-100">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-primary-100 rounded-lg">
                <Eye className="w-5 h-5 text-primary-600" />
              </div>
              <div>
                <p className="text-xs text-gray-600 capitalize">Total Clicks</p>
                <p className="text-2xl font-bold text-gray-900">
                  {url.total_clicks.toLocaleString()}
                </p>
              </div>
            </div>

            {onViewDetails && (
              <button
                onClick={() => onViewDetails(url.id)}
                className="flex items-center space-x-2 px-4 py-2 bg-white border border-gray-200 rounded-lg hover:bg-primary-50 hover:border-primary-300 transition-colors text-sm font-medium text-gray-700"
              >
                <BarChart3Icon className="w-4 h-4" />
                <span>View Details</span>
              </button>
            )}
          </div>
        </div>

        {/* Additional Info */}
        <div className="grid grid-cols-2 gap-3">
          <div className="flex items-center space-x-2 text-sm">
            <TrendingUp className="w-4 h-4 text-gray-400" />
            <span className="text-gray-600">
              {url.total_clicks > 0 ? 'Active' : 'No clicks yet'}
            </span>
          </div>
          <div className="flex items-center space-x-2 text-sm">
            <Calendar className="w-4 h-4 text-gray-400" />
            <span className="text-gray-600">
              {Math.floor(
                (new Date().getTime() - new Date(url.created_at).getTime()) /
                  (1000 * 60 * 60 * 24)
              )}{' '}
              days ago
            </span>
          </div>
        </div>

        {/* Action Button */}
        <div>
          <a
            href={`${window.location.origin}/s/${url.short_code}`}
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center justify-center w-full px-4 py-2 bg-gray-50 hover:bg-primary-50 border border-gray-200 hover:border-primary-300 rounded-lg text-gray-700 hover:text-primary-700 font-medium transition-colors"
          >
            <ExternalLink className="w-4 h-4 mr-2" />
            Open Shortened Link
          </a>
        </div>
      </div>
    </div>
  );
};

// Helper component icon
const BarChart3Icon = ({ className = '' }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    stroke="currentColor"
    viewBox="0 0 24 24"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
  </svg>
);
