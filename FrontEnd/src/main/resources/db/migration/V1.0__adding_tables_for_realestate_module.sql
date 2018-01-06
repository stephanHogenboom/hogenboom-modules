CREATE TABLE property_entry (
    oid INTEGER NOT NULL,
    address_id TEXT NOT NULL,
    date TEXT NOT NULL,
    is_sold INTEGER,
    sell_price INTEGER,
    PRIMARY KEY (address_id, date)
    );

CREATE TABLE address (
    kix_code TEXT ,
    country TEXT NOT NULL,
    street TEXT NOT NULL,
    house_number INTEGER NOT NULL,
    extension TEXT,
    postal_code TEXT NOT NULL,
    city TEXT NOT NULL,
    PRIMARY KEY (kix_code) ON CONFLICT IGNORE
    );


CREATE TABLE price_history_entry (
    property_id INTEGER,
    price INTEGER NOT NULL,
    date TEXT,
    PRIMARY KEY (property_id, date) ON CONFLICT IGNORE
    FOREIGN KEY (property_id) REFERENCES property_entry (property_id) ON DELETE CASCADE
    );

CREATE TABLE addressee (
    name TEXT PRIMARY KEY,
    phone_number TEXT,
    email TEXT,
    kix_code TEXT NOT NULL,
    FOREIGN KEY (kix_code) REFERENCES address(kix_code)
    );