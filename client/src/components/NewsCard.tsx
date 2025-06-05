import type { News } from '../types';

interface NewsCardProps {
  news: News;
}

export const NewsCard = ({ news }: NewsCardProps) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden h-[400px] flex flex-col">
      <div className="min-h-[180px] relative">
          <img
            src={news.imageUrl}
            alt={news.title}
            className="absolute w-full h-full object-cover object-center"
            loading="lazy"
          />
      </div>
      <div className="p-4 flex flex-col flex-grow">
        <div className="flex flex-col gap-2 h-[142px]">
          <h3 className="text-xl font-semibold">{news.title}</h3>
          <p className="text-gray-600 overflow-hidden">
            {news.summary}
          </p>
        </div>
        <div className="flex justify-between items-center mt-auto">
          <span className="text-sm text-gray-600">
            {news.publishedAt ? new Date(news.publishedAt).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
              day: 'numeric'
            }) : 'Date unavailable'}
          </span>
          <a
            href={news.url}
            target="_blank"
            rel="noopener noreferrer"
            className="text-gray-600 hover:bg-blue-50 px-2 py-1 rounded transition"
          >
            Read more
          </a>
        </div>
      </div>
    </div>
  );
}; 