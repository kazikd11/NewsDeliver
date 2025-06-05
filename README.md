# NewsDeliver

Full-stack local news delivery application built with Spring Boot and React.

## Prerequisites

- Java 21
- Node.js 18+
- PostgreSQL

## Environment Setup

1. Create `.env` file in the server directory:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/news
DB_USERNAME=postgres
DB_PASSWORD=your_password
PORT=8081

# News API Configuration
NEWS_API_KEY=your_news_api_key
NEWS_API_URL=https://api.worldnewsapi.com/search-news
# If true, periodically fetches news in the background
NEWS_FETCH_SCHEDULE=false
# If true, fetches news when application starts
NEWS_FETCH_ONSTART=false

# OpenAI Configuration
OPENAI_API_KEY=your_openai_api_key
OPENAI_MODEL=gpt-4o-mini
OPENAI_TEMPERATURE=0.1
OPENAI_MAX_TOKENS=50

# News Location Settings
# If true, periodically updates cities' geographical data
NEWS_LOCATION_SCHEDULE=false
# If true, updates cities' geographical data when application starts
NEWS_LOCATION_ONSTART=true
```

2. Create `.env` file in the `client` directory:

```env
VITE_API_URL=
```

## Running the Application

### Database Setup

1. Install PostgreSQL if not already installed
2. Create a database:
```sql
CREATE DATABASE news_db;
```
3. The tables will be created automatically by Flyway migrations when the application starts

### Backend

1. Navigate to server directory:
```bash
cd server
```

2. Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

### Frontend

1. Navigate to client directory:
```bash
cd client
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

## API Documentation

### Backend Endpoints

- `GET /news/global` - Get global news
- `GET /news/local?cityId={id}` - Get local news for a specific city
- `GET /cities?namePart={query}` - Search cities by name