CREATE TABLE IF NOT EXISTS students
(
  student_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  furigana VARCHAR(50) NOT NULL,
  nickname VARCHAR(50),
  e_mail VARCHAR(30) NOT NULL,
  live_municipality VARCHAR(100),
  age INT,
  gender VARCHAR(20),
  remark TEXT,
  is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
  course_id  INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT NOT NULL,
  course_name VARCHAR(50) NOT NULL,
  start_date TIMESTAMP,
  end_date TIMESTAMP
);