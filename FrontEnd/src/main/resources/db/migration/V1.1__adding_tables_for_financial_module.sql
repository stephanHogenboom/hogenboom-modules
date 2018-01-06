--inserting the basic tables to create financial entries
CREATE TABLE IF NOT EXISTS financial_entry (id integer PRIMARY KEY, name text NOT NULL, value real, categorie_id integer, timestamp TEXT);
CREATE TABLE IF NOT EXISTS category (id integer PRIMARY KEY, name text NOT NULL);