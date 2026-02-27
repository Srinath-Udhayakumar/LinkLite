import { useState } from 'react';
import { Header } from './components/Header';
import { ShortenPage } from './pages/ShortenPage';
import { AnalyticsPage } from './pages/AnalyticsPage';

function App() {
  const [currentPage, setCurrentPage] = useState<'home' | 'analytics'>('home');

  return (
    <div className="min-h-screen bg-gray-50">
      <Header currentPage={currentPage} onNavigate={setCurrentPage} />
      
      <main>
        {currentPage === 'home' ? <ShortenPage /> : <AnalyticsPage />}
      </main>

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-400 py-8 mt-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">
            <div>
              <h3 className="text-white font-semibold mb-4">About URLSnip</h3>
              <p className="text-sm">
                A modern URL shortening service with real-time analytics built for
                performance and reliability.
              </p>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Features</h3>
              <ul className="text-sm space-y-2">
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    URL Shortening
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    Analytics Dashboard
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    Click Tracking
                  </a>
                </li>
              </ul>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Support</h3>
              <ul className="text-sm space-y-2">
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    Documentation
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    API Reference
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-white transition-colors">
                    Contact Us
                  </a>
                </li>
              </ul>
            </div>
          </div>

          <div className="border-t border-gray-800 pt-8 text-center text-sm">
            <p>&copy; 2026 URLSnip. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
