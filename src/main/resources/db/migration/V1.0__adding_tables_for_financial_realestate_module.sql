CREATE TABLE IF NOT EXISTS property_entry (id INTEGER PRIMARY KEY, address_id INTEGER NOT NULL, date TEXT, is_sold INTEGER, sell_price INTEGER);
CREATE TABLE IF NOT EXISTS address (id INTEGER PRIMARY KEY, street TEXT NOT NULL, house_number INTEGER NOT NULL, extension TEXT, postal_code TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS price_history_entry ( id integer PRIMARY KEY, price INTEGER NOT NULL, date TEXT);
CREATE TABLE IF NOT EXISTS addressee ( id integer PRIMARY KEY, name TEXT NOT NULL, phone_number TEXT, email TEXT);