package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMetricDto {
    private String sensorType;
    private String value;
    private String unit;
    private int[] readTime;
}