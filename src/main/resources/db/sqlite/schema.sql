CREATE TABLE IF NOT EXISTS vets (
  id INTEGER PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS specialties (
  id INTEGER PRIMARY KEY,
  name VARCHAR(80)
);

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id INT(4) NOT NULL,
  specialty_id INT(4) NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  UNIQUE (vet_id,specialty_id)
);

CREATE TABLE IF NOT EXISTS types (
  id INTEGER PRIMARY KEY,
  name VARCHAR(80)
);

CREATE TABLE IF NOT EXISTS owners (
  id INTEGER PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS pets (
  id INTEGER PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id INT(4) NOT NULL,
  owner_id INT(4) NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id)
);

CREATE TABLE IF NOT EXISTS visits (
  id INTEGER PRIMARY KEY,
  pet_id INT(4) NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);
