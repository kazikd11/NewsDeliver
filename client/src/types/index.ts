export interface City {
  id: number;
  name: string;
  state: string;
}

export interface News {
  id: number;
  title: string;
  summary: string;
  imageUrl: string;
  url: string;
  publishedAt: string;
}

export interface NewsResponse {
  localNews: News[];
  nearbyNews: News[];
} 