CREATE TABLE users (
id INTEGER PRIMARY KEY,
name VARCHAR,
age INTEGER,
license BOOLEAN,
car_id INTEGER REFERENCES cars(id)
);

CREATE TABLE cars (
id INTEGER PRIMARY KEY,
brand VARCHAR,
model VARCHAR,
coast INTEGER
);

 SELECT student.name, student.age, faculty.name
 FROM student
 INNER JOIN faculty ON student.faculty_id = faculty.id;

 SELECT student
 FROM student
 INNER JOIN avatar ON student.id = avatar.student_id;
