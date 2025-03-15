package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.kafka.consumer;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input.ProcessSensorMetricUseCase;
import com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.kafka.dto.SensorMetricDto;
import com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.mapper.SensorMetricKafkaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorMetricConsumer {
    private final ProcessSensorMetricUseCase processSensorMetricUseCase;
    private final SensorMetricKafkaMapper sensorMetricKafkaMapper;

    @KafkaListener(topics = "${app.kafka.topics.sensor-data}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(SensorMetricDto sensorMetricDto) {
        log.info("Recebida mensagem do Kafka na thread [{}]: {}",
                 Thread.currentThread().getName(), sensorMetricDto);

        try {
            SensorMetric sensorMetric = sensorMetricKafkaMapper.toDomain(sensorMetricDto);
            log.info("Mapeamento concluído na thread [{}]", Thread.currentThread().getName());

            processSensorMetricUseCase.process(sensorMetric);
            log.info("Processamento iniciado na thread [{}]", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("Erro ao processar mensagem do Kafka na thread [{}]: {}",
                     Thread.currentThread().getName(), e.getMessage(), e);
        }
    }
}
