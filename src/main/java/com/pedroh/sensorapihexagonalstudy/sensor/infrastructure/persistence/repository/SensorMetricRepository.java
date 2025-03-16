package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.persistence.repository;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorMetricRepository extends JpaRepository<SensorMetric, Long> {
    @Query(value = "SELECT * FROM sensor_metrics ORDER BY read_time DESC LIMIT :limit",
            nativeQuery = true)
    List<SensorMetric> findTopByOrderByReadTimeDesc(@Param("limit") int limit);
}
