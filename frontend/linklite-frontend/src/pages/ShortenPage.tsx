import React from 'react';
import { URLForm } from '../components/URLForm';

export const ShortenPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Hero Section */}
        <div className="text-center mb-12">
          <h1 className="text-5xl md:text-6xl font-bold text-gray-900 mb-4">
            Shorten Your Links
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-cyan-600">
              {' '}
              Instantly
            </span>
          </h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto mb-8">
            Create short, trackable URLs in seconds. Track clicks, monitor engagement,
            and gain insights into your link performance with powerful analytics.
          </p>
        </div>

        {/* Main Form */}
        <URLForm />

        {/* Features Section */}
        <div className="mt-16 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <FeatureCard
            icon="⚡"
            title="Lightning Fast"
            description="Shortened URLs generated in milliseconds"
          />
          <FeatureCard
            icon="📊"
            title="Real-time Analytics"
            description="Track every click with detailed metrics"
          />
          <FeatureCard
            icon="🔐"
            title="Secure & Reliable"
            description="Enterprise-grade infrastructure"
          />
          <FeatureCard
            icon="🌍"
            title="Global Scale"
            description="Fast redirects worldwide with minimal latency"
          />
        </div>
      </div>
    </div>
  );
};

interface FeatureCardProps {
  icon: string;
  title: string;
  description: string;
}

const FeatureCard: React.FC<FeatureCardProps> = ({ icon, title, description }) => (
  <div className="bg-white rounded-lg p-6 border border-gray-200 shadow-sm hover:shadow-md transition-shadow">
    <div className="text-3xl mb-3">{icon}</div>
    <h3 className="font-semibold text-gray-900 mb-2">{title}</h3>
    <p className="text-gray-600 text-sm">{description}</p>
  </div>
);
