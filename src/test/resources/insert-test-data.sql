INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('10000000-0000-0000-0000-000000000001', 'ADD', 100, 'test-user-1', '2017-06-01 01:52:56');
INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('10000000-0000-0000-0000-000000000002', 'ADD', 200, 'test-user-1', '2017-06-01 01:53:56');
INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('10000000-0000-0000-0000-000000000003', 'CONSUME', -100, 'test-user-1', '2017-06-01 01:54:56');
INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('10000000-0000-0000-0000-000000000004', 'CONSUME', -50, 'test-user-1', '2017-06-01 01:55:56');
INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('20000000-0000-0000-0000-000000000001', 'ADD', 100, 'test-user-2', '2017-06-01 01:55:56');
INSERT INTO point_event (event_id, event_type, amount, username, event_date)
VALUES ('20000000-0000-0000-0000-000000000002', 'CONSUME', 100, 'test-user-2', '2017-06-01 01:56:56');

INSERT INTO paid_entry (entry_id, event_id) VALUES (77, '10000000-0000-0000-0000-000000000003');
INSERT INTO paid_entry (entry_id, event_id) VALUES (88, '10000000-0000-0000-0000-000000000004');
INSERT INTO paid_entry (entry_id, event_id) VALUES (88, '20000000-0000-0000-0000-000000000002');