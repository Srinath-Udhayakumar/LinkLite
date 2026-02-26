export interface ShortenedURL {
  id: number;
  long_url: string;
  short_code: string;
  total_clicks: number;
  created_at: string;
}

export interface URLClick {
  id: number;
  url_id: number;
  clicked_at: string;
  ip_address?: string;
}

export interface APIResponse<T> {
  success: boolean;
  data: T;
  message: string;
}

export interface AnalyticsData {
  total_clicks: number;
  clicks_last_24h: number;
  click_history: URLClick[];
  url_info: ShortenedURL;
}

export interface CreateURLRequest {
  long_url: string;
}

export interface CreateURLResponse {
  id: number;
  long_url: string;
  short_code: string;
  shortened_url: string;
}
