# Quick Start Guide - URL Shortener Frontend

## 🚀 Get Started in 2 Minutes

### 1. Prerequisites
- ✅ Node.js (v16+) installed
- ✅ npm installed
- ✅ Backend server running (see backend requirements below)

### 2. Installation & Setup

```bash
# Navigate to project directory
cd "frontend project"

# Install dependencies (already done if you followed setup)
npm install

# Start development server
npm run dev
```

The application will automatically open at `http://localhost:3000` with hot reload enabled.

### 3. Configure Backend Connection

Edit [src/services/apiService.ts](src/services/apiService.ts#L8):

```typescript
private baseURL = 'http://localhost:8080/api'; // Update to your backend URL
```

**Common configurations:**
```
Local Development:     http://localhost:8080/api
Docker Environment:    http://backend:8080/api
Cloud Deployment:      https://api.yourdomain.com/api
```

### 4. Verify Connection

1. Open the application: `http://localhost:3000`
2. Check the header - should show green "API Connected" badge
3. If red/error, verify:
   - Backend server is running
   - API URL is correct
   - CORS is enabled on backend

---

## 📁 File Structure Quick Reference

```
frontend\ project/
├── public/                  # Static assets
├── src/
│   ├── components/         # Reusable components
│   │   ├── Header.tsx
│   │   ├── URLForm.tsx
│   │   ├── AnalyticsCard.tsx
│   │   └── AnalyticsDetail.tsx
│   ├── pages/              # Page components
│   │   ├── ShortenPage.tsx
│   │   └── AnalyticsPage.tsx
│   ├── services/
│   │   └── apiService.ts   # ⚙️ UPDATE API URL HERE
│   ├── types/
│   │   └── index.ts
│   ├── App.tsx             # Main app
│   ├── main.tsx            # Entry point
│   └── index.css
├── index.html              # HTML template
├── vite.config.ts          # Vite config
├── tailwind.config.js      # Tailwind config
├── tsconfig.json           # TypeScript config
├── package.json
└── README.md
```

---

## 🎯 Usage

### Feature 1: Shorten URLs
1. Home page loads by default
2. Enter a long URL
3. Click "Shorten"
4. Copy the shortened URL
5. Share it anywhere!

### Feature 2: View Analytics
1. Click "Analytics" tab
2. See all your shortened URLs
3. View statistics (total clicks, etc.)
4. Click "View Details" for deep insights
5. See click trends and history

### Feature 3: Delete URLs
1. On Analytics page, click trash icon on any card
2. Confirm deletion
3. URL removed from dashboard

---

## 🛠️ Available Commands

```bash
# Development server with hot reload
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint TypeScript/React
npm run lint
```

---

## 📡 Backend API Requirements

Your backend must implement these endpoints:

### 1. Shorten URL
```
POST /api/urls/shorten
Content-Type: application/json

{
  "long_url": "https://example.com/very/long/path"
}

Response:
{
  "id": 1,
  "long_url": "https://example.com/very/long/path",
  "short_code": "Ab3Xy9",
  "shortened_url": "https://yoursite.com/s/Ab3Xy9"
}
```

### 2. Get All URLs
```
GET /api/urls

Response:
[
  {
    "id": 1,
    "long_url": "https://example.com/very/long/path",
    "short_code": "Ab3Xy9",
    "total_clicks": 42,
    "created_at": "2026-02-20T08:00:00Z"
  },
  ...
]
```

### 3. Get Analytics
```
GET /api/analytics/{shortCode}

Response:
{
  "total_clicks": 42,
  "clicks_last_24h": 5,
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
    "total_clicks": 42,
    "created_at": "2026-02-20T08:00:00Z"
  }
}
```

### 4. Delete URL
```
DELETE /api/urls/{id}

Response:
{
  "message": "URL deleted successfully"
}
```

**Note:** See [apiService.ts](src/services/apiService.ts) for complete API documentation.

---

## 🐛 Troubleshooting

### Problem: "Cannot GET /api/urls"
**Solution:** Ensure backend server is running on the configured URL

### Problem: "Module not found"
**Solution:** Run `npm install` again
```bash
npm install
```

### Problem: Port 3000 already in use
**Solution:** Vite will automatically use the next available port. Check terminal output for the actual URL.

### Problem: API returns CORS error
**Solution:** Configure CORS on your backend to allow requests from `http://localhost:3000`

Example (Node.js/Express):
```javascript
app.use(cors({
  origin: 'http://localhost:3000',
  credentials: true
}));
```

### Problem: Styles not applying
**Solution:** Ensure Tailwind CSS is properly built
```bash
npm run build
```

---

## 📊 Testing the Frontend

### Manual Testing Checklist

- [ ] Home page loads correctly
- [ ] Can enter a URL and shorten it
- [ ] Shortened URL displays correctly
- [ ] Copy button works
- [ ] Can navigate to Analytics tab
- [ ] URLs list loads
- [ ] Can search/filter URLs
- [ ] Can view URL details
- [ ] Analytics charts render
- [ ] Can delete URLs
- [ ] Error messages appear when API is down

### Automated Testing (Optional)
Add Jest/Vitest configuration for unit tests:
```bash
npm install -D vitest @testing-library/react
```

---

## 🎨 Customization

### Change Colors
Edit [tailwind.config.js](tailwind.config.js):
```javascript
theme: {
  extend: {
    colors: {
      primary: {
        600: '#0284c7', // Change primary color
        // ... other shades
      }
    }
  }
}
```

### Change API Base URL
Edit [src/services/apiService.ts](src/services/apiService.ts#L8):
```typescript
private baseURL = 'YOUR_CUSTOM_URL_HERE';
```

### Modify Page Content
Edit page files in `src/pages/`:
- `ShortenPage.tsx` - Home page content
- `AnalyticsPage.tsx` - Analytics page

---

## 📚 Documentation Files

- [README.md](README.md) - Full project documentation
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design and flows
- [UI_COMPONENTS.md](UI_COMPONENTS.md) - Component reference
- [API Service](src/services/apiService.ts) - API client methods
- [Type Definitions](src/types/index.ts) - TypeScript interfaces

---

## 🚢 Production Deployment

### Build for Production
```bash
npm run build
```

Creates `dist/` folder with optimized assets.

### Deploy
Upload contents of `dist/` to your hosting:
- **Netlify:** Drag & drop `dist/` folder
- **Vercel:** Connect GitHub repo
- **AWS S3:** Upload to S3 bucket with CloudFront
- **Docker:** Create Dockerfile for containerized deployment

```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## 💡 Tips & Tricks

### VS Code Extensions Recommended
- ES7+ React/Redux/React-Native snippets
- Tailwind CSS IntelliSense
- TypeScript Vue Plugin
- Prettier - Code formatter

### Development Workflow
```bash
# Terminal 1: Frontend dev server
npm run dev

# Terminal 2: Backend dev server (in separate window)
cd ../backend-project
npm start
```

### Browser DevTools
- React DevTools extension for component inspection
- Redux DevTools for state debugging
- Network tab to monitor API calls

---

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/my-feature`
2. Make your changes
3. Test thoroughly
4. Commit: `git commit -m 'feat: add my feature'`
5. Push: `git push origin feature/my-feature`
6. Create Pull Request

---

## ❓ FAQ

**Q: Can I use this frontend with a different backend?**
A: Yes! Just update the API URL in `apiService.ts` and ensure your backend implements the same API endpoints.

**Q: How do I add authentication?**
A: Add login functionality by:
1. Creating a LoginPage component
2. Storing auth token in localStorage
3. Adding token to API request headers

**Q: Can I customize the UI?**
A: Absolutely! All styling uses Tailwind CSS and can be modified in `tailwind.config.js` or individual component files.

**Q: How do I add more features?**
A: Follow the existing component structure:
1. Create component in `src/components/`
2. Add types in `src/types/index.ts`
3. Add API methods in `src/services/apiService.ts`
4. Integrate into pages

---

## 📞 Support & Resources

- **Documentation:** See [README.md](README.md) and [ARCHITECTURE.md](ARCHITECTURE.md)
- **Component Reference:** See [UI_COMPONENTS.md](UI_COMPONENTS.md)
- **React Docs:** https://react.dev
- **Tailwind CSS:** https://tailwindcss.com
- **TypeScript:** https://www.typescriptlang.org

---

## ✨ Next Steps

1. ✅ Frontend is ready
2. 📋 Start your backend server
3. 🔗 Update API URL in `apiService.ts`
4. 🚀 Run `npm run dev`
5. 🎉 Start using the application!

---

**Happy shortening! 🎯**
