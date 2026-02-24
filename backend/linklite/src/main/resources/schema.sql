-- LinkLite URL Shortener Database Schema
-- PostgreSQL Schema Initialization Script - STRICT REQUIREMENTS COMPLIANCE

-- Drop all existing tables to ensure clean state
DROP TABLE IF EXISTS url_clicks CASCADE;
DROP TABLE IF EXISTS url_click CASCADE;
DROP TABLE IF EXISTS urls CASCADE;
DROP TABLE IF EXISTS url CASCADE;

-- Create URLs table as per Requirement 8.1
CREATE TABLE urls (
    id BIGSERIAL PRIMARY KEY,
    long_url TEXT NOT NULL,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    total_clicks BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create URL_Clicks table as per Requirement 8.2
CREATE TABLE url_clicks (
    id BIGSERIAL PRIMARY KEY,
    url_id BIGINT NOT NULL,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    CONSTRAINT fk_url_clicks_urls FOREIGN KEY (url_id) REFERENCES urls(id) ON DELETE CASCADE
);

-- Create indexes for performance as per requirements
CREATE INDEX idx_urls_short_code ON urls(short_code);
CREATE INDEX idx_urls_created_at ON urls(created_at);
CREATE INDEX idx_url_clicks_url_id ON url_clicks(url_id);
CREATE INDEX idx_url_clicks_clicked_at ON url_clicks(clicked_at);

-- Create sequence for short code generation if needed
CREATE SEQUENCE IF NOT EXISTS short_code_seq START 1;
