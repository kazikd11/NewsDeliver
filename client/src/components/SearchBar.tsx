import { useState, useEffect } from 'react';
import type { KeyboardEvent } from 'react';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import type { City } from '../types';
import { API_URL } from '../config';

interface SearchBarProps {
  onCitySelect: (city: City) => void;
}

export const SearchBar = ({ onCitySelect }: SearchBarProps) => {
  const [query, setQuery] = useState('');
  const [cities, setCities] = useState<City[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showOptions, setShowOptions] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const [isSelected, setIsSelected] = useState(false);

  useEffect(() => {
    const searchCities = async () => {
      if (!query || isSelected) {
        setCities([]);
        return;
      }

      setIsLoading(true);
      try {
        const response = await fetch(`${API_URL}/cities?namePart=${encodeURIComponent(query)}`);
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || 'Failed to fetch cities');
        setCities(data);
        setShowOptions(true);
        setSelectedIndex(-1);
      } catch (error) {
        console.error('Error fetching cities:', error);
        setCities([]);
      } finally {
        setIsLoading(false);
      }
    };

    const timeoutId = setTimeout(searchCities, 300);
    return () => clearTimeout(timeoutId);
  }, [query, isSelected]);

  const handleSelect = (cityString: string) => {
    const selectedCity = cities.find(city => `${city.name}, ${city.state}` === cityString);
    if (selectedCity) {
      onCitySelect(selectedCity);
      setQuery(cityString);
      setShowOptions(false);
      setSelectedIndex(-1);
      setIsSelected(true);
    }
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (!showOptions || cities.length === 0) return;

    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        setSelectedIndex(prev => (prev < cities.length - 1 ? prev + 1 : prev));
        break;
      case 'ArrowUp':
        e.preventDefault();
        setSelectedIndex(prev => (prev > 0 ? prev - 1 : prev));
        break;
      case 'Enter':
        e.preventDefault();
        if (selectedIndex >= 0 && selectedIndex < cities.length) {
          const city = cities[selectedIndex]!;
          handleSelect(`${city.name}, ${city.state}`);
        }
        break;
      case 'Escape':
        setShowOptions(false);
        setSelectedIndex(-1);
        break;
    }
  };

  return (
    <div className="relative w-full max-w-2xl mx-auto">
      <div className="relative">
        <input
          type="text"
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
            setIsSelected(false);
            if (e.target.value === '') {
              setShowOptions(false);
            }
          }}
          onKeyDown={handleKeyDown}
          onFocus={() => !isSelected && query && cities.length > 0 && setShowOptions(true)}
          placeholder="Search for a city..."
          className="w-full px-4 py-2 pl-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <MagnifyingGlassIcon className="absolute left-3 top-2.5 h-5 w-5 text-gray-600" />
      </div>

      {showOptions && (cities.length > 0 || isLoading) && (
        <ul className="absolute w-full mt-1 bg-white border border-gray-300 rounded-lg shadow-lg z-10 max-h-60 overflow-auto">
          {isLoading ? (
            <li className="px-4 py-2 text-gray-600">Loading...</li>
          ) : (
            cities.map((city, index) => (
              <li key={city.id}>
                <button
                  onClick={() => handleSelect(`${city.name}, ${city.state}`)}
                  className={`w-full px-4 py-2 text-left hover:bg-blue-50 focus:bg-blue-50 focus:outline-none transition-colors
                    ${selectedIndex === index ? 'bg-blue-50' : ''}`}
                >
                  {city.name}, {city.state}
                </button>
              </li>
            ))
          )}
        </ul>
      )}
    </div>
  );
}; 