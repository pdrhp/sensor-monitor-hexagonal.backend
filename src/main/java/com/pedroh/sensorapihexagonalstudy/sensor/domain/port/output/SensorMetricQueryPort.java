package com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;

import java.util.List;

public interface SensorMetricQueryPort {
    List<SensorMetric> findRecentMetrics(int limit);
}
