package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.persistence.repository;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMetricRepository extends JpaRepository<SensorMetric, Long> { }
