CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS city_name_trgm_idx ON cities USING gin (name gin_trgm_ops);
