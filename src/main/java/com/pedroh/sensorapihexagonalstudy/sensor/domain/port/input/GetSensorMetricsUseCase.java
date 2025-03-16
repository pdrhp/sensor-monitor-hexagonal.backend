package com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import reactor.core.publisher.Flux;

public interface GetSensorMetricsUseCase {
    Flux<SensorMetric> getLatestMetrics();
}
