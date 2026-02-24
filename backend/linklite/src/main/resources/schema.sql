-- LinkLite URL Shortener Database Schema
-- PostgreSQL Schema Initialization Script

-- Create URL table
CREATE TABLE IF NOT EXISTS url (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(10) NOT NULL UNIQUE,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create URL_CLICK table for analytics
CREATE TABLE IF NOT EXISTS url_click (
    id BIGSERIAL PRIMARY KEY,
    url_id BIGINT NOT NULL,
    short_code VARCHAR(10) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    referer VARCHAR(500),
    country VARCHAR(2),
    city VARCHAR(100),
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_url_click_url FOREIGN KEY (url_id) REFERENCES url(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_url_short_code ON url(short_code);
CREATE INDEX IF NOT EXISTS idx_url_created_at ON url(created_at);
CREATE INDEX IF NOT EXISTS idx_url_click_short_code ON url_click(short_code);
CREATE INDEX IF NOT EXISTS idx_url_click_clicked_at ON url_click(clicked_at);
CREATE INDEX IF NOT EXISTS idx_url_click_url_id ON url_click(url_id);

-- Create sequence for short code generation if needed
CREATE SEQUENCE IF NOT EXISTS short_code_seq START 1;

-- Insert sample data for testing (optional)
-- INSERT INTO url (short_code, long_url) VALUES 
--     ('TEST123', 'https://www.google.com'),
--     ('TEST456', 'https://www.github.com')
-- ON CONFLICT (short_code) DO NOTHING;
