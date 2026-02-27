import React, { useState, useEffect } from 'react';
import { XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from 'recharts';
import { ArrowLeft, Calendar, Eye, Clock } from 'lucide-react';
import apiService from '../services/apiService';
import { AnalyticsData } from '../types';

interface AnalyticsDetailProps {
  shortCode: string;
  onBack: () => void;
}

export const AnalyticsDetail: React.FC<AnalyticsDetailProps> = ({ shortCode, onBack }) => {
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [chartData, setChartData] = useState<any[]>([]);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        setLoading(true);
        const data = await apiService.getAnalytics(shortCode);
        setAnalytics(data);

        // Process click history for charts
        if (data.click_history && data.click_history.length > 0) {
          const hourlyData: { [key: string]: number } = {};
          data.click_history.forEach((click) => {
            const date = new Date(click.clicked_at);
            const hourKey = date.toISOString().substring(0, 13) + ':00';
            hourlyData[hourKey] = (hourlyData[hourKey] || 0) + 1;
          });

          const sortedData = Object.entries(hourlyData)
            .map(([hour, count]) => ({
              time: new Date(hour).toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
              }),
              clicks: count,
            }))
            .sort((a, b) => a.time.localeCompare(b.time));

          setChartData(sortedData);
        }
      } catch (err: any) {
        setError(err.response?.data?.message || 'Failed to load analytics');
      } finally {
        setLoading(false);
      }
    };

    fetchAnalytics();
  }, [shortCode]);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="inline-block">
            <div className="w-12 h-12 border-4 border-primary-200 border-t-primary-600 rounded-full animate-spin"></div>
          </div>
          <p className="mt-4 text-gray-600 font-medium">Loading analytics...</p>
        </div>
      </div>
    );
  }

  if (error || !analytics) {
    return (
      <div className="bg-red-50 border border-red-300 rounded-lg p-6 text-center">
        <p className="text-red-700 font-semibold">{error || 'Failed to load analytics'}</p>
      </div>
    );
  }

  const clicksLast24h = analytics.clicks_last_24h || 0;
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-4 mb-6">
        <button
          onClick={onBack}
          className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5 text-gray-600" />
        </button>
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Analytics Details</h2>
          <p className="text-gray-600 font-mono text-primary-600">{shortCode}</p>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* Total Clicks */}
        <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm font-medium">Total Clicks</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {analytics.url_info.total_clicks.toLocaleString()}
              </p>
            </div>
            <div className="p-3 bg-blue-100 rounded-lg">
              <Eye className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        {/* Last 24h Clicks */}
        <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm font-medium">Last 24 Hours</p>
              <p className="text-3xl font-bold text-gray-900 mt-2">
                {clicksLast24h.toLocaleString()}
              </p>
            </div>
            <div className="p-3 bg-teal-100 rounded-lg">
              <Clock className="w-6 h-6 text-teal-600" />
            </div>
          </div>
        </div>

        {/* Created Date */}
        <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm font-medium">Created</p>
              <p className="text-sm font-semibold text-gray-900 mt-2">
                {formatDate(analytics.url_info.created_at)}
              </p>
            </div>
            <div className="p-3 bg-purple-100 rounded-lg">
              <Calendar className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </div>
      </div>

      {/* URL Info */}
      <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">URL Information</h3>
        <div className="space-y-4">
          <div>
            <label className="text-sm font-medium text-gray-600">Original URL</label>
            <p className="mt-1 text-gray-900 break-all p-3 bg-gray-50 rounded-lg">
              {analytics.url_info.long_url}
            </p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="text-sm font-medium text-gray-600">Short Code</label>
              <p className="mt-1 font-mono font-bold text-primary-600 p-3 bg-gray-50 rounded-lg">
                {analytics.url_info.short_code}
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-gray-600">Short URL</label>
              <p className="mt-1 font-mono text-gray-900 p-3 bg-gray-50 rounded-lg">
                {window.location.origin}/s/{analytics.url_info.short_code}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Charts */}
      {chartData.length > 0 && (
        <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Click Trend (Hourly)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="time" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line
                type="monotone"
                dataKey="clicks"
                stroke="#0284c7"
                strokeWidth={2}
                dot={{ fill: '#0284c7' }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}

      {/* Recent Clicks Table */}
      {analytics.click_history && analytics.click_history.length > 0 && (
        <div className="bg-white rounded-lg border border-gray-200 p-6 shadow-sm">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Clicks</h3>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-200">
                  <th className="text-left py-3 px-4 font-semibold text-gray-700">Time</th>
                  <th className="text-left py-3 px-4 font-semibold text-gray-700">IP Address</th>
                </tr>
              </thead>
              <tbody>
                {analytics.click_history.slice(0, 10).map((click, idx) => (
                  <tr key={idx} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="py-3 px-4">{formatDate(click.clicked_at)}</td>
                    <td className="py-3 px-4 font-mono text-gray-600">
                      {click.ip_address || 'Unknown'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
