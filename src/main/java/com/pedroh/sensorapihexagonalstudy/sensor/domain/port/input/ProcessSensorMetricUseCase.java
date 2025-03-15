package com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;

public interface ProcessSensorMetricUseCase {
    void process(SensorMetric sensorMetric);
}
