CREATE TABLE IF NOT EXISTS property_entry (address_id TEXT NOT NULL, date TEXT, is_sold INTEGER, sell_price INTEGER, PRIMARY KEY (address_id, date));
CREATE TABLE IF NOT EXISTS address (kix_code TEXT PRIMARY KEY NOT NULL, country TEXT NOT NULL ,street TEXT NOT NULL, house_number INTEGER NOT NULL, extension TEXT, postal_code TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS price_history_entry ( id integer PRIMARY KEY, price INTEGER NOT NULL, date TEXT);
CREATE TABLE IF NOT EXISTS addressee ( id integer PRIMARY KEY, name TEXT NOT NULL, phone_number TEXT, email TEXT);