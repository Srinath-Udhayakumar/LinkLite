-- Verify database schema creation
-- This script checks if all required tables and indexes exist

-- Check if tables exist
SELECT table_name, table_type 
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name IN ('url', 'url_click')
ORDER BY table_name;

-- Check indexes on url table
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'url' 
AND schemaname = 'public'
ORDER BY indexname;

-- Check indexes on url_click table  
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'url_click' 
AND schemaname = 'public'
ORDER BY indexname;

-- Check foreign key constraints
SELECT
    tc.table_name, 
    kcu.column_name, 
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM 
    information_schema.table_constraints AS tc 
    JOIN information_schema.key_column_usage AS kcu
      ON tc.constraint_name = kcu.constraint_name
      AND tc.table_schema = kcu.table_schema
    JOIN information_schema.constraint_column_usage AS ccu
      ON ccu.constraint_name = tc.constraint_name
      AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' 
AND tc.table_schema = 'public';
