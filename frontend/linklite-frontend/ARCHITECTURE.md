# URL Shortener Frontend - Architecture & User Flows

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Browser / Client                          │
├─────────────────────────────────────────────────────────────────┤
│                       React Application                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                     App.tsx (Root)                       │  │
│  │  - State Management (currentPage)                        │  │
│  │  - Navigation Logic                                      │  │
│  └───────────────────┬──────────────────────────────────────┘  │
│                      │                                           │
│        ┌─────────────┴─────────────┐                            │
│        │                           │                            │
│  ┌─────▼──────────────┐   ┌──────▼─────────────────┐           │
│  │  ShortenPage       │   │  AnalyticsPage        │           │
│  │  ├─ URLForm       │   │  ├─ AnalyticsCard     │           │
│  │  │  ├─ Input      │   │  │   (List view)       │           │
│  │  │  ├─ Validation │   │  │                     │           │
│  │  │  └─ Submit     │   │  ├─ AnalyticsDetail   │           │
│  │  │                │   │  │   (Detailed view)   │           │
│  │  └─ Features      │   │  │   ├─ Charts        │           │
│  └───────────────────┘   │  │   ├─ Stats Grid    │           │
│                           │  │   └─ Click History│           │
│                           └────────────────────────┘           │
│                                                                 │
│  ┌────────────────────────────────────────────────────────┐   │
│  │            Shared Components                           │   │
│  │  - Header.tsx (Navigation & Branding)                  │   │
│  └────────────────────────────────────────────────────────┘   │
│                                                                 │
│  ┌────────────────────────────────────────────────────────┐   │
│  │              API Service Layer                         │   │
│  │  - apiService.ts (Axios Instance)                      │   │
│  │  - Methods:                                            │   │
│  │    • shortenURL(url)                                   │   │
│  │    • getAllURLs()                                      │   │
│  │    • getAnalytics(shortCode)                           │   │
│  │    • deleteURL(id)                                     │   │
│  │    • getRecentClicks(shortCode)                        │   │
│  │    • getClickTrends(shortCode, period)                 │   │
│  └───────────────────┬──────────────────────────────────┘   │
│                      │                                         │
└──────────────────────┼─────────────────────────────────────────┘
                       │
                       │ HTTP/REST
                       │
┌──────────────────────▼─────────────────────────────────────────┐
│                 Backend API Server                              │
│           (Node.js, Java, Python, etc.)                         │
├─────────────────────────────────────────────────────────────────┤
│                  API Endpoints:                                 │
│  POST   /api/urls/shorten          - Create shortened URL       │
│  GET    /api/urls                  - Fetch all URLs             │
│  GET    /api/analytics/{code}      - Get analytics              │
│  GET    /api/analytics/url/{id}    - Analytics by ID            │
│  DELETE /api/urls/{id}             - Delete shortened URL       │
│                                                                 │
│  (Implements URL shortening, click tracking, storage)          │
└─────────────────────────────────────────────────────────────────┘
```

## Component Hierarchy

```
App
├── Header
│   ├── Navigation Buttons
│   └── Status Badge
├── ShortenPage
│   ├── URLForm
│   │   ├── Input Field
│   │   ├── Shorten Button
│   │   ├── Error Message
│   │   └── Success Message with Copy
│   └── Feature Cards
└── AnalyticsPage
    ├── Search & Sort Controls
    ├── Stats Summary (3 cards)
    └── AnalyticsCard (repeating)
        └── Delete button
        └── View Details button
            └── AnalyticsDetail
                ├── Stats Grid
                ├── URL Information
                ├── Click Trend Chart
                └── Recent Clicks Table
```

## User Flows

### Flow 1: Shorten a URL

```
User Opens App
    │
    ├─ Lands on ShortenPage
    │
    ├─ Sees URLForm with:
    │  • Long URL input field
    │  • Shorten button
    │  • Feature cards
    │
    ├─ Enters long URL
    │  (https://example.com/very/long/path)
    │
    ├─ Clicks "Shorten" button
    │  │
    │  ├─ Frontend validates URL format
    │  │
    │  └─ Sends POST /api/urls/shorten
    │     ├─ Backend generates short code
    │     ├─ Stores mapping in database
    │     └─ Returns response
    │
    ├─ Receives shortened URL
    │  (https://app.com/Ab3Xy9)
    │
    ├─ Sees success message with:
    │  • Shortened URL display
    │  • Short code (Ab3Xy9)
    │  • Copy to clipboard button
    │
    └─ Can click Copy button to copy URL
```

### Flow 2: View Analytics Dashboard

```
User Clicks "Analytics" Tab
    │
    ├─ Navigates to AnalyticsPage
    │
    ├─ Frontend loads all shortened URLs
    │  │
    │  └─ GET /api/urls
    │     └─ Returns list of URLs with click counts
    │
    ├─ Displays:
    │  • Total URLs count
    │  • Total clicks across all URLs
    │  • Average clicks per URL
    │  • Search/Filter controls
    │  • Sort options (by clicks, date)
    │
    ├─ Each URL shown as AnalyticsCard with:
    │  • Short code badge
    │  • Original long URL preview
    │  • Total click count
    │  • Creation date
    │  • Delete button
    │  • View Details button
    │
    └─ Can search by URL or short code
```

### Flow 3: View Detailed Analytics

```
User Clicks "View Details" on URL Card
    │
    ├─ AnalyticsDetail component loads
    │
    ├─ Frontend fetches detailed analytics
    │  │
    │  └─ GET /api/analytics/{shortCode}
    │     └─ Returns analytics data
    │
    ├─ Displays comprehensive analytics:
    │  │
    │  ├─ Stats Grid (3 cards):
    │  │  • Total Clicks
    │  │  • Last 24 Hours Clicks
    │  │  • Created Date
    │  │
    │  ├─ URL Information Section:
    │  │  • Original long URL
    │  │  • Short code
    │  │  • Short URL (clickable)
    │  │
    │  ├─ Click Trend Chart:
    │  │  • Line chart showing hourly clicks
    │  │  • X-axis: Time
    │  │  • Y-axis: Number of clicks
    │  │
    │  └─ Recent Clicks Table:
    │     • Click timestamp
    │     • IP address
    │     (Last 10 clicks shown)
    │
    └─ Can click back arrow to return to dashboard
```

### Flow 4: Delete a URL

```
User Clicks Delete Button on AnalyticsCard
    │
    ├─ Confirmation dialog appears
    │  "Are you sure you want to delete this shortened URL?"
    │
    ├─ User confirms deletion
    │  │
    │  └─ DELETE /api/urls/{id}
    │     ├─ Backend removes URL and clicks from database
    │     └─ Returns success response
    │
    └─ Frontend updates UI:
       • URL removed from list
       • Stats recalculated
       • Success feedback shown
```

## Data Flow

### URL Creation Flow

```
Frontend                                Backend
┌──────────────────┐                    
│   User Input     │                    
│ (long_url: str)  │──────────────────►┌──────────────────┐
└──────────────────┘   POST /api/urls   │  URL Service     │
                       /shorten         │                  │
                                        ├─ Validate URL   │
                                        ├─ Generate code  │
                                        ├─ Check uniqueness
                                        └─ Store in DB    │
                                                           │
                       ◄──────────────────────────────────┘
                       {                                    
                         id: 1,                            
                         short_code: "Ab3Xy9",             
                        shortened_url: "https://app.com/Ab3Xy9"
                       }                                   
                                                           
                       ┌──────────────────┐               
                       │  Display Result  │               
                       │  - Short code    │               
                       │  - URL           │               
                       │  - Copy button   │               
                       └──────────────────┘               
```

### Analytics Retrieval Flow

```
Frontend                                Backend
┌──────────────────────────┐           
│  GET /api/analytics/{id} │───────────►┌────────────────────┐
└──────────────────────────┘            │ Analytics Service  │
                                        │                    │
                                        ├─ Query clicks table│
                                        ├─ Count total clicks│
                                        ├─ Filter last 24h   │
                                        ├─ Get click history │
                                        └─ Return aggregated │
                                           data              │
                       ◄──────────────────                 
                       {                                    
                         total_clicks: 150,               
                         clicks_last_24h: 23,             
                         click_history: [...],            
                         url_info: {...}                  
                       }                                  
                                                          
                       ┌──────────────────────┐          
                       │ Render Analytics UI  │          
                       │ - Stats cards        │          
                       │ - Charts             │          
                       │ - Click table        │          
                       └──────────────────────┘          
```

## State Management

```
App Component (Root State)
├── currentPage: 'home' | 'analytics'
│   └─ Controls which page is displayed
│
ShortenPage Component
├── longURL: string
├── shortCode: string
├── shortenedURL: string
├── error: string
├── success: boolean
└── loading: boolean

AnalyticsPage Component
├── urls: ShortenedURL[]
├── loading: boolean
├── error: string
├── searchTerm: string
├── selectedURLId: number | null
└── sortBy: 'clicks' | 'date'

AnalyticsDetail Component
├── analytics: AnalyticsData | null
├── loading: boolean
├── error: string
└── chartData: any[]
```

## API Request/Response Examples

### Request: Shorten URL
```json
POST /api/urls/shorten
{
  "long_url": "https://example.com/very/long/path?param1=value1&param2=value2"
}
```

### Response: Success
```json
{
  "id": 1,
  "long_url": "https://example.com/very/long/path?param1=value1&param2=value2",
  "short_code": "Ab3Xy9",
  "shortened_url": "https://app.com/s/Ab3Xy9"
}
```

### Request: Get Analytics
```
GET /api/analytics/Ab3Xy9
```

### Response: Analytics Data
```json
{
  "total_clicks": 150,
  "clicks_last_24h": 23,
  "click_history": [
    {
      "id": 1,
      "url_id": 1,
      "clicked_at": "2026-02-24T10:30:00Z",
      "ip_address": "192.168.1.1"
    },
    ...
  ],
  "url_info": {
    "id": 1,
    "long_url": "https://example.com/very/long/path",
    "short_code": "Ab3Xy9",
    "total_clicks": 150,
    "created_at": "2026-02-20T08:00:00Z"
  }
}
```

## Technologies & Libraries

| Technology | Purpose | Version |
|-----------|---------|---------|
| React | UI Framework | 18.2.0 |
| TypeScript | Type Safety | 5.2.2 |
| Vite | Build Tool | 5.0.8 |
| Tailwind CSS | Styling | 3.3.6 |
| Axios | HTTP Client | 1.6.0 |
| Recharts | Charts & Graphs | 2.10.0 |
| Lucide React | Icons | 0.292.0 |

## Folder Structure Details

```
src/
├── components/          # Reusable UI components
│   ├── Header.tsx      # Navigation & branding
│   ├── URLForm.tsx     # URL input and shortening
│   ├── AnalyticsCard.tsx   # Single URL summary card
│   └── AnalyticsDetail.tsx # Detailed analytics view
│
├── pages/              # Full page views
│   ├── ShortenPage.tsx # URL shortening interface
│   └── AnalyticsPage.tsx   # Analytics dashboard
│
├── services/           # API & external services
│   └── apiService.ts   # Axios HTTP client with API methods
│
├── types/              # TypeScript type definitions
│   └── index.ts        # All interfaces and types
│
├── App.tsx             # Root component (routing)
├── main.tsx            # Application entry point
├── index.css           # Global styles with Tailwind
│
public/                # Static assets
└── vite.svg          # Vite logo

```

## Performance Considerations

1. **API Calls**: Debounced search to reduce requests
2. **Chart Rendering**: Only rendered when data is available
3. **List Rendering**: Efficient React rendering with proper keys
4. **CSS**: Tailwind CSS for optimized production build
5. **Build**: Minified and gzipped assets in production

## Accessibility Features

- Semantic HTML structure
- ARIA labels on interactive elements
- Keyboard navigation support
- Color contrast compliance
- Loading states and error messages

## Error Handling Strategy

```
Frontend Errors
├── Network Errors
│   └─ Display: "Failed to load. Make sure backend is running"
│
├── Validation Errors
│   ├─ Empty URL: "Please enter a URL to shorten"
│   └─ Invalid URL: "Please enter a valid URL (e.g., https://example.com)"
│
├── API Errors
│   └─ 4xx/5xx: Display backend error message from response
│
└── Retry Logic
    └─ Retry button in error messages
```
