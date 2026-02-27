import axios, { AxiosError, AxiosInstance } from 'axios';
import { AnalyticsData, CreateURLRequest, CreateURLResponse, ShortenedURL } from '../types';

class APIService {
  private api: AxiosInstance;
  private baseURL: string;
  private maxRetries = 3;
  private retryDelay = 1000;

  constructor() {
    // Use environment variable or fallback to localhost
    this.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8081';
    
    this.api = axios.create({
      baseURL: this.baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
      timeout: 10000,
    });

    // Response interceptor for error handling
    this.api.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        console.error('API Error:', error.message);
        if (error.response?.status === 404) {
          throw new Error('URL not found');
        } else if (error.response?.status === 400) {
          throw new Error('Invalid request');
        } else if (error.response?.status === 500) {
          throw new Error('Server error');
        }
        throw error;
      }
    );
  }

  /**
   * Shorten a long URL with retry logic
   */
  async shortenURL(request: CreateURLRequest): Promise<CreateURLResponse> {
    let lastError: any;
    
    for (let attempt = 1; attempt <= this.maxRetries; attempt++) {
      try {
        const response = await this.api.post<CreateURLResponse>('/urls/shorten', request);
        return response.data;
      } catch (error) {
        lastError = error;
        console.warn(`Attempt ${attempt} failed, retrying...`, error);
        
        if (attempt < this.maxRetries) {
          await this.delay(this.retryDelay * attempt);
        }
      }
    }
    
    throw lastError || new Error('Failed to shorten URL');
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
      const response = await this.api.get<AnalyticsData>(`/urls/${shortCode}/analytics`);
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
   * Get click history with pagination
   */
  async getClickHistory(shortCode: string, page: number = 0, size: number = 10) {
    try {
      const response = await this.api.get(
        `/urls/${shortCode}/analytics/history`,
        {
          params: { page, size },
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching click history:', error);
      throw error;
    }
  }

  /**
   * Get last 24 hours analytics
   */
  async get24HoursAnalytics(shortCode: string) {
    try {
      const response = await this.api.get(`/urls/${shortCode}/analytics`, {
        params: { range: '24h' },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching 24h analytics:', error);
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
   * Export analytics as CSV
   */
  async exportClickHistory(shortCode: string): Promise<string> {
    try {
      const history = await this.getClickHistory(shortCode, 0, 1000);
      const csv = this.convertToCSV(history);
      return csv;
    } catch (error) {
      console.error('Error exporting analytics:', error);
      throw error;
    }
  }

  /**
   * Convert click history to CSV format
   */
  private convertToCSV(data: any): string {
    const headers = ['Timestamp', 'IP Address'];
    const rows = data.content?.map((click: any) => [
      click.clickedAt || '',
      click.ipAddress || '',
    ]) || [];

    const csvContent = [
      headers.join(','),
      ...rows.map((row: any[]) => row.join(',')),
    ].join('\n');

    return csvContent;
  }

  /**
   * Helper method for retry logic
   */
  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }
}

export default new APIService();
