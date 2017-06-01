CREATE TABLE point_event (
  event_id   VARCHAR(36)  NOT NULL,
  event_type VARCHAR(36)  NOT NULL,
  amount     INTEGER      NOT NULL,
  username   VARCHAR(128) NOT NULL,
  event_date DATETIME     NOT NULL,
  PRIMARY KEY (event_id),
  INDEX (username)
)
  ENGINE = InnoDB;

CREATE TABLE paid_entry (
  entry_id INTEGER     NOT NULL,
  event_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (entry_id, event_id),
  FOREIGN KEY (event_id) REFERENCES point_event (event_id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB;
