CREATE TABLE expenses (
 descr TEXT,
 amount REAL,
 type TEXT,
 monthid INTEGER,
 catid INTEGER,
 id INTEGER PRIMARY KEY,
 FOREIGN KEY(monthid) REFERENCES month(monthid),
 FOREIGN KEY(catid) REFERENCES category(catid)
 );


CREATE TABLE account (
  name TEXT,
  balance REAL,
  accountid INTEGER PRIMARY KEY);
  
CREATE TABLE category (
  name TEXT,
  catid INTEGER PRIMARY KEY);

CREATE TABLE month(
  name TEXT,
  monthid INTEGER PRIMARY KEY);


CREATE TABLE budget (
  amount REAL,
  monthid INTEGER,
  catid INTEGER,
  FOREIGN KEY(monthid) REFERENCES month(monthid),
  FOREIGN KEY(catid) REFERENCES category(catid));
  