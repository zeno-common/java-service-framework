CREATE TABLE IF NOT EXISTS jsf_leaf_holder
(
    id             BIGINT PRIMARY KEY,
    node_key       VARCHAR(100) NOT NULL,
    last_heartbeat BIGINT       NOT NULL,
    create_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_node_key ON jsf_leaf_holder (node_key);
