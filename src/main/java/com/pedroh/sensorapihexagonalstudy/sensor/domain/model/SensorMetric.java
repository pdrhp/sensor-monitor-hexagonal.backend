package com.pedroh.sensorapihexagonalstudy.sensor.domain.model;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.enums.SensorType;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.enums.UnitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sensor_metrics")
public class SensorMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", columnDefinition = "unit_type_enum")
    @ColumnTransformer(
            write = "?::unit_type_enum",
            read = "unit::VARCHAR"
    )
    private UnitType unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type", columnDefinition = "sensor_type_enum")
    @ColumnTransformer(
            write = "?::sensor_type_enum",
            read = "sensor_type::VARCHAR"
    )
    private SensorType sensorType;

    private Integer value;

    private LocalDateTime readTime;
}
