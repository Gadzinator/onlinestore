CREATE TABLE cd.members
(
    mem_id         INTEGER PRIMARY KEY,
    surname        VARCHAR(200),
    firstname      VARCHAR(200),
    address        VARCHAR(300),
    zipcode        INTEGER,
    telephone      VARCHAR(20),
    recommended_by INTEGER,
    join_date      TIMESTAMP
);

INSERT INTO cd.members (mem_id, surname, firstname, address, zipcode, telephone, recommended_by, join_date)
VALUES (0, 'GUEST', 'GUEST', 'GUEST', 0, '(000) 000-0000', NULL, '2012-07-01 00:00:00'),
       (1, 'Smith', 'Darren', '8 Bloomsbury Close, Boston', 4321, '555-555-5555', NULL, '2012-07-02 12:02:05'),
       (2, 'Smith', 'Tracy', '8 Bloomsbury Close, New York', 4321, '555-555-5555', NULL, '2012-07-02 12:08:23'),
       (3, 'Rownam', 'Tim', '23 Highway Way, Boston', 23423, '(844) 693-0723', NULL, '2012-07-03 09:32:15'),
       (4, 'Joplette', 'Janice', '20 Crossing Road, New York', 234, '(833) 942-4710', 1, '2012-07-03 10:25:05'),
       (5, 'Butters', 'Gerald', '1065 Huntingdon Avenue, Boston', 56754, '(844) 078-4130', 1, '2012-07-09 10:44:09'),
       (6, 'Tracy', 'Burton', '3 Tunisia Drive, Boston', 45678, '(822) 354-9973', NULL, '2012-07-15 08:52:55'),
       (7, 'Dare', 'Nancy', '6 Hunting Lodge Way, Boston', 10383, '(833) 776-4001', 4, '2012-07-25 08:59:12'),
       (8, 'Boothe', 'Tim', '3 Bloomsbury Close, Reading, 00234', 234, '(811) 433-2547', 3, '2012-07-25 16:02:35'),
       (9, 'Stibbons', 'Ponder', '5 Dragons Way, Winchester', 87630, '(833) 160-3900', 6, '2012-07-25 17:09:05'),
       (10, 'Owen', 'Charles', '52 Cheshire Grove, Winchester, 28563', 28563, '(855) 542-5251', 1,
        '2012-08-03 19:42:37'),
       (11, 'Jones', 'David', '976 Gnats Close, Reading', 33862, '(844) 536-8036', 4, '2012-08-06 16:32:55'),
       (12, 'Baker', 'Anne', '55 Powdery Street, Boston', 80743, '844-076-5141', 9, '2012-08-10 14:23:22'),
       (13, 'Farrell', 'Jemima', '103 Firth Avenue, North Reading', 57392, '(855) 016-0163', NULL,
        '2012-08-10 14:28:01'),
       (14, 'Smith', 'Jack', '252 Binkington Way, Boston', 69302, '(822) 163-3254', 1, '2012-08-10 16:22:05'),
       (15, 'Bader', 'Florence', '264 Ursula Drive, Westford', 84923, '(833) 499-3527', 9, '2012-08-10 17:52:03'),
       (16, 'Baker', 'Timothy', '329 James Street, Reading', 58393, '833-941-0824', 13, '2012-08-15 10:34:25'),
       (17, 'Pinker', 'David', '5 Impreza Road, Boston', 65332, '811 409-6734', 13, '2012-08-16 11:32:47'),
       (20, 'Genting', 'Matthew', '4 Nunnington Place, Wingfield, Boston', 52365, '(811) 972-1377', 5,
        '2012-08-19 14:55:55'),
       (21, 'Mackenzie', 'Anna', '64 Perkington Lane, Reading', 64577, '(822) 661-2898', 1, '2012-08-26 09:32:05');

COMMIT;

WITH RECURSIVE recommenders(recommender, member) AS (SELECT recommended_by, mem_id
                                                     FROM cd.members

                                                     UNION ALL
                                                     SELECT m.recommended_by, rec.member

                                                     FROM recommenders rec
                                                              INNER JOIN cd.members m ON m.mem_id = rec.recommender)
SELECT rec.member member, rec.recommender, m.firstname, m.surname
FROM recommenders rec
         INNER JOIN cd.members m ON rec.recommender = m.mem_id
WHERE rec.member = 22
   OR rec.member = 12
ORDER BY rec.member ASC, rec.recommender DESC;