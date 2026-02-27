# URL Shortener Frontend - UI Components Documentation

## Component Reference

### Page 1: URL Shortening (ShortenPage)

#### Layout
```
┌─────────────────────────────────────────────────┐
│        Header (Navigation)                      │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Hero Section                             │ │
│  │  - Main heading                           │ │
│  │  - Subheading                             │ │
│  │  - Description                            │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  URL Form                                 │ │
│  │  ┌──────────────────────────────────────┐ │ │
│  │  │ Input URL:                           │ │ │
│  │  │ [https://example.com/very/long...]  │ │ │
│  │  │                           [Shorten] │ │ │
│  │  └──────────────────────────────────────┘ │ │
│  │                                           │ │
│  │  Error/Success Messages (conditional)    │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Feature Cards (4 columns)                │ │
│  │  ├─ Lightning Fast                        │ │
│  │  ├─ Real-time Analytics                  │ │
│  │  ├─ Secure & Reliable                    │ │
│  │  └─ Global Scale                         │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
├─────────────────────────────────────────────────┤
│        Footer                                    │
└─────────────────────────────────────────────────┘
```

#### URLForm Component

Props: None (standalone form)

State:
```typescript
{
  longURL: string;           // User input
  shortCode: string;         // Response short code
  shortenedURL: string;      // Full shortened URL
  copied: boolean;           // Copy button state
  loading: boolean;          // Submission loading
  error: string;             // Error message
  success: boolean;          // Success state
}
```

Key Features:
- Real-time URL validation (basic format check)
- Show/hide error messages
- Loading spinner during submission
- Success message with auto-dismiss (5 seconds)
- Copy to clipboard functionality
- Disabled button during loading

User Interactions:
```
1. User types in long URL field
   └─ Updates longURL state

2. User clicks "Shorten" button
   ├─ Validates URL format
   ├─ Shows error if invalid
   ├─ Submits to API if valid
   ├─ Shows loading spinner
   └─ On success:
      ├─ Displays shortened URL
      ├─ Shows short code
      ├─ Enables copy button
      └─ Auto-clears after 5 seconds
```

#### InfoCards Component

Each card displays:
- Icon (emoji)
- Title
- Description

Example:
```
┌──────────────────────┐
│ ⚡                  │
│ Lightning Fast      │
│ Shortened URLs gen- │
│ erated in millisec- │
│ onds                │
└──────────────────────┘
```

---

### Page 2: Analytics Dashboard (AnalyticsPage)

#### Layout
```
┌─────────────────────────────────────────────────┐
│        Header (Navigation)                      │
├─────────────────────────────────────────────────┤
│                                                 │
│  Page Title: "Your URLs Analytics"              │
│  Subtitle: Description                         │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Search & Filter Controls (if URLs exist)│ │
│  │  ┌──────────────────┐ ┌─────────────────┐ │ │
│  │  │ Search [______]  │ │ Sort By [____] │ │ │
│  │  └──────────────────┘ └─────────────────┘ │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Stats Summary (3 cards)                  │ │
│  │  ├─ Total URLs: 42                        │ │
│  │  ├─ Total Clicks: 1,250                   │ │
│  │  └─ Avg Clicks/URL: 29                    │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  URL Cards Grid (2 columns on large)      │ │
│  │  ┌──────────────┐ ┌──────────────┐       │ │
│  │  │ Card 1       │ │ Card 2       │       │ │
│  │  ├──────────────┤ ├──────────────┤       │ │
│  │  │ Card 3       │ │ Card 4       │       │ │
│  │  └──────────────┘ └──────────────┘       │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
├─────────────────────────────────────────────────┤
│        Footer                                    │
└─────────────────────────────────────────────────┘
```

#### AnalyticsPage Component

Props: None

State:
```typescript
{
  urls: ShortenedURL[];      // All shortened URLs
  loading: boolean;          // Initial load state
  error: string;             // Error message
  searchTerm: string;        // Search filter
  selectedURLId: number | null;  // Detail view ID
  sortBy: 'clicks' | 'date'; // Sort parameter
}
```

Key Features:
- Real-time search filtering
- Sort by clicks or creation date
- Displays summary statistics
- Shows error with retry button
- Handles empty state
- Conditional rendering of AnalyticsDetail

Data Processing:
```
Raw URLs → Filter (searchTerm) → Sort (by clicks/date) → Display
```

#### AnalyticsCard Component

Props:
```typescript
{
  url: ShortenedURL;              // URL data object
  onDelete?: (id: number) => void;    // Delete callback
  onViewDetails?: (id: number) => void; // Detail view callback
}
```

Display:
```
┌────────────────────────────────────┐
│  [Ab3Xy9] [Copy]         [Delete] │
│  https://example.com/very/long... │
│  Created: Feb 20, 2026 at 8:00 AM │
│                                    │
│  ┌──────────────────────────────┐  │
│  │ 👁 Total Clicks              │  │
│  │ 150                  [Details]│  │
│  └──────────────────────────────┘  │
│                                    │
│  📈 Active  │  📅 4 days ago      │
│                                    │
│  [Open Shortened Link]             │
└────────────────────────────────────┘
```

Features:
- Short code badge with copy button
- Original URL preview (truncated)
- Creation date
- Total click count in prominent stats box
- Trending indicators
- View Details button
- Delete button with confirmation
- Open link button

User Actions:
```
1. Copy Button
   └─ Copies shortened URL to clipboard
   └─ Shows confirmation (2 sec)

2. Delete Button
   ├─ Shows confirmation dialog
   └─ On confirm: removes from list

3. View Details Button
   └─ Switches to AnalyticsDetail view

4. Open Link Button
   └─ Opens shortened URL in new tab
```

---

### Page 2b: Analytics Details (AnalyticsDetail)

Shown when user clicks "View Details" on AnalyticsCard

#### Layout
```
┌─────────────────────────────────────────────────┐
│ [Back]  Analytics Details                       │
│         sh0rt                                   │
│                                                 │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │ Total    │ │ Last 24h │ │ Created  │       │
│  │ Clicks   │ │ Clicks   │ │ Date     │       │
│  │ 150      │ │ 23       │ │ Feb 20   │       │
│  └──────────┘ └──────────┘ └──────────┘       │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  URL Information                          │ │
│  │  Original URL:                            │ │
│  │  [https://example.com/very/long/path...] │ │
│  │                                           │ │
│  │  Short Code:     Short URL:               │ │
│  │  [Ab3Xy9]        [https://app.com/s/...] │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Click Trend (Hourly)                     │ │
│  │                                           │ │
│  │      │     ╱╲                            │ │
│  │      │    ╱  ╲    ╱╲                     │ │
│  │  ────┼───╱────╲──╱──╲───                │ │
│  │      │ ╱        │                        │ │
│  │      0   5   10   15   20                │ │
│  │          Hour                           │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
│  ┌───────────────────────────────────────────┐ │
│  │  Recent Clicks                            │ │
│  │  ┌─────────────────────────────────────┐ │ │
│  │  │ Time              │ IP Address      │ │ │
│  │  ├─────────────────────────────────────┤ │ │
│  │  │ Feb 24, 10:45 AM │ 192.168.1.100  │ │ │
│  │  │ Feb 24, 10:30 AM │ 192.168.1.101  │ │ │
│  │  │ ...               │ ...             │ │ │
│  │  └─────────────────────────────────────┘ │ │
│  └───────────────────────────────────────────┘ │
│                                                 │
└─────────────────────────────────────────────────┘
```

Props:
```typescript
{
  shortCode: string;          // URL short code
  onBack: () => void;         // Back button callback
}
```

State:
```typescript
{
  analytics: AnalyticsData | null;  // Fetched analytics
  loading: boolean;                 // Loading state
  error: string;                    // Error message
  chartData: any[];                 // Processed chart data
}
```

Components:
1. **Stats Grid** (3 cards)
   - Total Clicks
   - Last 24H Clicks
   - Created Date

2. **URL Information Section**
   - Original long URL
   - Short code
   - Short URL (clickable)

3. **Click Trend Chart**
   - Line chart using Recharts
   - Hourly data points
   - Interactive tooltip

4. **Recent Clicks Table**
   - Click timestamp
   - IP address
   - Shows last 10 clicks

---

### Header Component

Global navigation component shown on all pages.

Props:
```typescript
{
  currentPage: 'home' | 'analytics';  // Active page
  onNavigate: (page) => void;         // Navigate callback
}
```

Layout:
```
┌───────────────────────────────────────────────────┐
│ [Link Icon] URLSnip        [Home] [Analytics] [✓] │
│              Analytics                API Connected│
└───────────────────────────────────────────────────┘
```

Components:
1. **Logo Section**
   - Link icon
   - Brand name "URLSnip"
   - Tagline "URL Shortener & Analytics"

2. **Navigation Buttons**
   - Home button (highlights on home page)
   - Analytics button (highlights on analytics page)
   - Highlight color: primary-100 background

3. **Status Badge**
   - Shows green dot for "API Connected"
   - Hidden on mobile

---

## Data Types & Interfaces

### ShortenedURL
```typescript
{
  id: number;              // Database ID
  long_url: string;        // Original URL
  short_code: string;      // e.g., "Ab3Xy9"
  total_clicks: number;    // Click count
  created_at: string;      // ISO timestamp
}
```

### URLClick
```typescript
{
  id: number;
  url_id: number;          // Foreign key to URLs
  clicked_at: string;      // ISO timestamp
  ip_address?: string;     // Optional IP
}
```

### AnalyticsData
```typescript
{
  total_clicks: number;        // Total clicks all time
  clicks_last_24h: number;     // Last 24 hours
  click_history: URLClick[];   // Recent clicks
  url_info: ShortenedURL;      // URL metadata
}
```

### CreateURLRequest
```typescript
{
  long_url: string;  // URL to shorten
}
```

### CreateURLResponse
```typescript
{
  id: number;
  long_url: string;
  short_code: string;
  shortened_url: string;  // Full URL
}
```

---

## Styling Details

### Color Scheme
```
Primary (Cyan/Sky Blue):
  - Base: #0284c7 (primary-600)
  - Light: #38bdf8 (primary-400)
  - Lighter: #bae6fd (primary-200)
  - Lightest: #0356a1 (primary-800)

Background:
  - Page: #f8fafc (slate-50)
  - Cards: #ffffff (white)
  - Hover: #f1f5f9 (slate-100)

Text:
  - Primary: #1f2937 (gray-900)
  - Secondary: #6b7280 (gray-500)
  - Light: #d1d5db (gray-300)

Status:
  - Success: #10b981 (green-600)
  - Error: #ef4444 (red-600)
  - Warning: #f59e0b (amber-600)
```

### Typography
```
H1 (Page Title): 
  - Font Size: 2rem (32px)
  - Font Weight: 700 (bold)
  - Color: #1f2937

H2 (Section Title):
  - Font Size: 1.5rem (24px)
  - Font Weight: 600 (semibold)
  - Color: #1f2937

Body Text:
  - Font Size: 1rem (16px)
  - Font Weight: 400
  - Color: #4b5563

Caption:
  - Font Size: 0.875rem (14px)
  - Font Weight: 500
  - Color: #6b7280
```

### Spacing Scale
```
xs: 0.25rem (4px)
sm: 0.5rem (8px)
md: 1rem (16px)
lg: 1.5rem (24px)
xl: 2rem (32px)
2xl: 3rem (48px)
```

### Border Radius
```
Default: 0.5rem (8px)
Large: 1rem (16px)
Full: 9999px (circles)
```

---

## Responsive Breakpoints

```
Mobile First approach:

sm: 640px   (tablets)
md: 768px   (small laptops)
lg: 1024px  (desktops)
xl: 1280px  (large desktops)
2xl: 1536px (extra large)

Grid Changes:
- Cards grid: 1 column (mobile) → 2 columns (lg) → 3 columns (xl)
- Feature cards: 1 col (mobile) → 2 cols (md) → 4 cols (lg)
```

---

## Animation & Transitions

```
Button Hover:
  - Background color transition
  - Duration: 200ms
  - Easing: ease-in-out

Loading Spinner:
  - Rotation animation
  - Duration: 1s
  - Infinite loop

Card Hover:
  - Box shadow increase
  - Duration: 150ms

Copy Confirmation:
  - Icon change (copy → checkmark)
  - Duration: 2 seconds (then revert)

Success Message:
  - Auto-dismiss after 5 seconds
  - Fade out effect
```

---

## Accessibility

```
ARIA Labels:
- Buttons have descriptive text
- Forms have associated labels
- Icons have title attributes

Keyboard Navigation:
- Tab through all interactive elements
- Enter to activate buttons/links
- Space to toggle checkboxes

Color Contrast:
- Primary text on white: 7:1 ratio
- Secondary text: 4.5:1 ratio
- Meets WCAG AA standards

Focus States:
- Visible outline on focused elements
- Primary color ring
- 2px width
```
