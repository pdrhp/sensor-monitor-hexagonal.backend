package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.mapper;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.enums.SensorType;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.enums.UnitType;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.kafka.dto.SensorMetricDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SensorMetricKafkaMapper {
    public SensorMetric toDomain(SensorMetricDto dto) {
        if (dto == null) {
            return null;
        }

        SensorMetric domain = new SensorMetric();
        domain.setSensorType(SensorType.valueOf(dto.getSensorType()));
        domain.setValue(Integer.parseInt(dto.getValue()));
        domain.setUnit(UnitType.valueOf(dto.getUnit()));

        int[] readTime = dto.getReadTime();
        if (readTime != null && readTime.length >= 7) {
            domain.setReadTime(LocalDateTime.of(
                    readTime[0],
                    readTime[1],
                    readTime[2],
                    readTime[3],
                    readTime[4],
                    readTime[5],
                    readTime[6]
            ));
        }

        return domain;
    }
}
