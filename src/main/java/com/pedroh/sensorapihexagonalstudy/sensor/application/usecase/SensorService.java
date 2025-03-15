package com.pedroh.sensorapihexagonalstudy.sensor.application.usecase;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input.ProcessSensorMetricUseCase;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output.SensorMetricPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService implements ProcessSensorMetricUseCase {

    private final SensorMetricPersistencePort saveSensorMetricPort;

    @Override
    public void process(SensorMetric sensorMetric) {
        log.info("Processando métrica do sensor na thread [{}]: {}",
                 Thread.currentThread().getName(), sensorMetric);

        saveSensorMetricPort.saveAsync(sensorMetric)
            .thenAccept(savedMetric ->
                log.info("Callback após salvamento assíncrono na thread [{}], ID: {}",
                        Thread.currentThread().getName(), savedMetric.getId()))
            .exceptionally(ex -> {
                log.error("Erro no salvamento assíncrono na thread [{}]: {}",
                         Thread.currentThread().getName(), ex.getMessage(), ex);
                return null;
            });

        log.info("Método process concluído na thread [{}] (não aguardou o salvamento)",
                 Thread.currentThread().getName());
    }
}
