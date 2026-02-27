# URL Shortener with Analytics System (USAS) - Frontend

A modern, responsive React frontend for a URL shortening service with real-time analytics and click tracking capabilities.

## Features

- **URL Shortening**: Convert long URLs to short, shareable links
- **Real-time Analytics**: View click counts and engagement metrics
- **Detailed Analytics**: Timeline charts, hourly trends, and recent click history.
- **Search & Filter**: Find URLs quickly with search and sorting options
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Modern UI**: Built with Tailwind CSS for a professional appearance

## Project Structure

```
src/
├── components/          # Reusable React components
│   ├── Header.tsx      # Navigation header
│   ├── URLForm.tsx     # URL shortening form
│   ├── AnalyticsCard.tsx   # Individual URL analytics card
│   └── AnalyticsDetail.tsx # Detailed analytics view with charts
├── pages/              # Full page components
│   ├── ShortenPage.tsx # URL shortening interface
│   └── AnalyticsPage.tsx   # Analytics dashboard
├── services/           # API integration
│   └── apiService.ts   # Axios-based API client
├── types/              # TypeScript interfaces
│   └── index.ts        # Type definitions
├── App.tsx             # Main application component
├── main.tsx            # Application entry point
└── index.css           # Global styles with Tailwind
```

## Prerequisites

- Node.js (v16 or higher)
- npm or yarn package manager

## Installation

1. Clone or extract the project:
```bash
cd frontend/linklite-frontend
```

2. Install dependencies:
```bash
npm install
```

## Configuration

### Backend API Configuration

Update the `baseURL` in [src/services/apiService.ts](src/services/apiService.ts#L8) to match your backend URL:

```typescript
private baseURL = 'http://localhost:8080/api'; // Change this to your backend URL
```

## Running the Application

### Development Server

Start the development server with hot reload:

```bash
npm run dev
```

The application will be available at `http://localhost:3000` (or the next available port).

### Production Build

Create an optimized production build:

```bash
npm run build
```

### Preview Production Build

Preview the production build locally:

```bash
npm run preview
```

## Available Routes

- **Home (`/`)**: URL shortening interface
- **Analytics (`/analytics`)**: Dashboard showing all shortened URLs and their analytics

## API Endpoints Expected

The frontend expects the following backend API endpoints:

### URL Management
- `POST /api/urls/shorten` - Shorten a new URL
- `GET /api/urls` - Get all shortened URLs
- `DELETE /api/urls/{id}` - Delete a shortened URL

### Analytics
- `GET /api/analytics/{shortCode}` - Get analytics for a specific short code
- `GET /api/analytics/url/{id}` - Get analytics by URL ID
- `GET /api/analytics/{shortCode}/clicks` - Get recent clicks
- `GET /api/analytics/{shortCode}/trends` - Get click trends

## Environment Variables

Create a `.env.local` file for environment-specific settings:

```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_TITLE=URLSnip
```

## Key Components

### Header Component
Navigation between URL shortening and analytics pages.

### URLForm Component
Main form for submitting URLs to be shortened, with:
- URL validation
- Loading states
- Success/error messages
- Copy-to-clipboard functionality

### AnalyticsCard Component
Displays summary information about a shortened URL including:
- Short code
- Total clicks
- Creation date
- Quick actions (delete, view details)

### AnalyticsDetail Component
Comprehensive analytics view with:
- Total click count
- 24-hour click statistics
- Hourly click trend chart
- Recent click history table

## Technologies Used

- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Utility-first styling
- **Axios** - HTTP client
- **Recharts** - Data visualization
- **Lucide React** - Icon library

## Styling

The project uses Tailwind CSS for styling. Customize colors and themes in [tailwind.config.js](tailwind.config.js).

### Color Scheme

- **Primary**: Cyan/Sky blue (#0284c7)
- **Background**: Light gray (#f8fafc)
- **Text**: Dark gray (#1f2937)

## Error Handling

The application includes comprehensive error handling:

- API error messages are displayed to users
- Loading states for async operations
- Graceful fallbacks for missing data
- Validation before URL submission

## Performance Optimizations

- Code splitting with Vite
- Lazy loading of components
- Optimized images and assets
- Debounced search inputs
- Memoized components where needed

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### API Connection Issues

If you see "API Connected" badge as red or get connection errors:

1. Ensure your backend server is running
2. Verify the API base URL is correct in `apiService.ts`
3. Check for CORS configuration on your backend
4. Review browser console for detailed error messages

### Port Already in Use

If port 3000 is in use, Vite will automatically use the next available port.

### Build Issues

Clear the cache and reinstall dependencies:

```bash
rm -rf node_modules package-lock.json
npm install
npm run build
```

## Contributing

Feel free to submit issues and enhancement requests!

## License

MIT License - feel free to use this project for personal or commercial purposes.

## Support

For issues or questions:
1. Check the [API Service Documentation](src/services/apiService.ts)
2. Review TypeScript interfaces in [types/index.ts](src/types/index.ts)
3. Check browser console for error details

## Future Enhancements

- [ ] User authentication and dashboard
- [ ] Custom short codes/aliases
- [ ] Geographic analytics
- [ ] QR code generation
- [ ] Bulk URL shortening
- [ ] Scheduled URL expiration
- [ ] Advanced security features
