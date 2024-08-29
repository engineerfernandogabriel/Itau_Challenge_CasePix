CREATE TABLE pix_keys (
    id UUID PRIMARY KEY,
    key_type VARCHAR(9) NOT NULL,
    key_value VARCHAR(77) NOT NULL UNIQUE,
    bank_account_type VARCHAR(15) NOT NULL,
    account_type VARCHAR(10) NOT NULL,
    agency_number VARCHAR(4) NOT NULL,
    account_number VARCHAR(8) NOT NULL,
    owner_name VARCHAR(30) NOT NULL,
    owner_last_name VARCHAR(45),
    created_at TIMESTAMP NOT NULL,
    inactivated_at TIMESTAMP
);