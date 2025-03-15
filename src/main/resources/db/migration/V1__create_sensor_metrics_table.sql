CREATE TYPE sensor_type_enum AS ENUM ('THERMO_METER', 'FUEL_METER', 'HEART_RATE_METER');
CREATE TYPE unit_type_enum AS ENUM ('Puls', 'F', 'L');

CREATE TABLE IF NOT EXISTS sensor_metrics (
    id BIGSERIAL PRIMARY KEY,
    sensor_type sensor_type_enum NOT NULL,
    value INTEGER NOT NULL,
    unit unit_type_enum NOT NULL,
    read_time TIMESTAMP NOT NULL
);

CREATE INDEX idx_sensor_metrics_sensor_type ON sensor_metrics(sensor_type);
CREATE INDEX idx_sensor_metrics_read_time ON sensor_metrics(read_time);

COMMENT ON TABLE sensor_metrics IS 'Armazena métricas coletadas de diferentes sensores';
COMMENT ON COLUMN sensor_metrics.sensor_type IS 'Tipo do sensor (THERMO_METER, FUEL_METER, HEART_RATE_METER)';
COMMENT ON COLUMN sensor_metrics.value IS 'Valor numérico da leitura do sensor';
COMMENT ON COLUMN sensor_metrics.unit IS 'Unidade de medida (Pulses, Fahrenheit, Liters)';
COMMENT ON COLUMN sensor_metrics.read_time IS 'Data e hora da leitura do sensor';