package com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;

import java.util.concurrent.CompletableFuture;

public interface SensorMetricPersistencePort {
    CompletableFuture<SensorMetric> saveAsync(SensorMetric sensorMetric);
}
