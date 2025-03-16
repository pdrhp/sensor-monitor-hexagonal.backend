package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.adapter;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output.SensorMetricPersistencePort;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output.SensorMetricQueryPort;
import com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.persistence.repository.SensorMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class SensorMetricPersistenceAdapter implements SensorMetricPersistencePort, SensorMetricQueryPort {
    private final SensorMetricRepository repository;

    public CompletableFuture<SensorMetric> saveAsync(SensorMetric sensorMetric) {
        log.info("Iniciando saveAsync na thread [{}]", Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> save(sensorMetric));
    }

    private SensorMetric save(SensorMetric sensorMetric) {
        log.info("Executando save na thread [{}]: tipo={}, valor={}, unidade={}, horário={}",
                Thread.currentThread().getName(),
                sensorMetric.getSensorType(),
                sensorMetric.getValue(),
                sensorMetric.getUnit(),
                sensorMetric.getReadTime());

        SensorMetric savedMetric = repository.save(sensorMetric);

        log.info("Métrica do sensor salva com sucesso na thread [{}]. ID: {}",
                 Thread.currentThread().getName(), savedMetric.getId());

        return savedMetric;
    }

    @Override
    public List<SensorMetric> findRecentMetrics(int limit) {
        return repository.findTopByOrderByReadTimeDesc(limit);
    }
}
