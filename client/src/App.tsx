import { useState, useEffect } from 'react';
import { SearchBar } from './components/SearchBar';
import { NewsCard } from './components/NewsCard';
import type { City, News, NewsResponse } from './types';
import { API_URL } from './config';

function App() {
  const [selectedCity, setSelectedCity] = useState<City | null>(null);
  const [localNews, setLocalNews] = useState<NewsResponse | null>(null);
  const [globalNews, setGlobalNews] = useState<News[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchGlobalNews = async () => {
      try {
        const response = await fetch(`${API_URL}/news/global`);
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Failed to fetch global news');
        setGlobalNews(data.slice(0, 9));
      } catch (error) {
        console.error('Error fetching global news:', error);
      }
    };

    fetchGlobalNews();
  }, []);

  useEffect(() => {
    const fetchLocalNews = async () => {
      if (!selectedCity) {
        setLocalNews(null);
        return;
      }

      setIsLoading(true);
      try {
        const response = await fetch(
          `${API_URL}/news/local?cityId=${selectedCity.id}`
        );
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Failed to fetch local news');
        data.localNews = data.localNews?.slice(0, 9);
        data.nearbyNews = data.nearbyNews?.slice(0, 9);
        setLocalNews(data);
      } catch (error) {
        console.error('Error fetching local news:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchLocalNews();
  }, [selectedCity]);

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-[90%] mx-auto py-8">
        <SearchBar onCitySelect={setSelectedCity} />
      </div>

      <main className="max-w-[90%] mx-auto py-8">
        {isLoading ? (
          <div className="text-center text-gray-600">Loading news...</div>
        ) : (
          <div className="space-y-12">
            {selectedCity && (
              <section>
                <h2 className="text-3xl text-gray-600 mb-6">
                  {selectedCity.name} News
                </h2>
                {localNews?.localNews && localNews.localNews.length > 0 ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {localNews.localNews.map((item) => (
                      <NewsCard key={item.id} news={item} />
                    ))}
                  </div>
                ) : (
                  <div className="text-center py-8 text-gray-600">
                    No local news available for {selectedCity.name}. Check back later!
                  </div>
                )}
              </section>
            )}

            {selectedCity && localNews?.nearbyNews && localNews.nearbyNews.length > 0 && (
              <section>
                <h2 className="text-3xl text-gray-600 mb-6">
                  You May Also Be Interested In
                </h2>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {localNews.nearbyNews.map((item) => (
                    <NewsCard key={item.id} news={item} />
                  ))}
                </div>
              </section>
            )}

            <section>
              <h2 className="text-3xl text-gray-600 mb-6">
                Global News
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {globalNews.length > 0 ? (
                  globalNews.map((item) => (
                    <NewsCard key={item.id} news={item} />
                  ))
                ) : (
                  <p className="text-gray-600">No global news available</p>
                )}
              </div>
            </section>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
