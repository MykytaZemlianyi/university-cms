ALTER TABLE university.lectures
DROP CONSTRAINT IF EXISTS fk_room;

ALTER TABLE university.lectures
ADD CONSTRAINT fk_room
FOREIGN KEY (room_id)
REFERENCES university.rooms(room_id)
ON DELETE SET NULL;