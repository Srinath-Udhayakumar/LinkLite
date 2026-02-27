import axios, { AxiosInstance } from 'axios';
import { CreateURLRequest, CreateURLResponse, AnalyticsData, ShortenedURL } from '../types';

class APIService {
  private api: AxiosInstance;
  private baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

  constructor() {
    this.api = axios.create({
      baseURL: this.baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  /**
   * Shorten a long URL
   */
  async shortenURL(request: CreateURLRequest): Promise<CreateURLResponse> {
    try {
      const response = await this.api.post<CreateURLResponse>('/urls/shorten', request);
      return response.data;
    } catch (error) {
      console.error('Error shortening URL:', error);
      throw error;
    }
  }

  /**
   * Get all shortened URLs
   */
  async getAllURLs(): Promise<ShortenedURL[]> {
    try {
      const response = await this.api.get<ShortenedURL[]>('/urls');
      return response.data;
    } catch (error) {
      console.error('Error fetching URLs:', error);
      throw error;
    }
  }

  /**
   * Get analytics for a specific shortened URL
   */
  async getAnalytics(shortCode: string): Promise<AnalyticsData> {
    try {
      const response = await this.api.get<AnalyticsData>(`/analytics/${shortCode}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching analytics:', error);
      throw error;
    }
  }

  /**
   * Get analytics for a URL by ID
   */
  async getAnalyticsById(urlId: number): Promise<AnalyticsData> {
    try {
      const response = await this.api.get<AnalyticsData>(`/analytics/url/${urlId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching analytics:', error);
      throw error;
    }
  }

  /**
   * Delete a shortened URL
   */
  async deleteURL(urlId: number): Promise<void> {
    try {
      await this.api.delete(`/urls/${urlId}`);
    } catch (error) {
      console.error('Error deleting URL:', error);
      throw error;
    }
  }

  /**
   * Get recent clicks for a URL
   */
  async getRecentClicks(shortCode: string, limit: number = 10) {
    try {
      const response = await this.api.get(`/analytics/${shortCode}/clicks`, {
        params: { limit },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching recent clicks:', error);
      throw error;
    }
  }

  /**
   * Get click trends (hourly, daily, etc.)
   */
  async getClickTrends(shortCode: string, period: 'hourly' | 'daily' | 'weekly') {
    try {
      const response = await this.api.get(`/analytics/${shortCode}/trends`, {
        params: { period },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching click trends:', error);
      throw error;
    }
  }
}

export default new APIService();
