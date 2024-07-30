<<<<<<< HEAD
INSERT INTO reservation (id, user_id, concert_schedule_id, seat_id, status, created_at, updated_at, total_price)
VALUES (1, 1, 1, 1, 'PENDING', NOW(), NOW(), 100);
=======
-- Insert into concert table first
INSERT INTO concert (id, name, information)
VALUES (1, 'Sample Concert', 'This is a sample concert description.');

-- Insert into schedule table
INSERT INTO schedule (id, date_time, total_seats, available_seats, concert_id)
VALUES (1, '2024-07-25 00:00:00', 100, 100, 1);

-- Insert into seat table
INSERT INTO seat (id, seat_number, status, price, schedule_id, version)
VALUES (1, 1, 'AVAILABLE', 100, 1, 0);

-- Insert into reservation table
INSERT INTO reservation (id, user_id, concert_schedule_id, seat_id, status, created_at, updated_at, total_price)
VALUES (1, 1, 1, 1, 'PENDING', NOW(), NOW(), 100);
>>>>>>> feature/lock_test
