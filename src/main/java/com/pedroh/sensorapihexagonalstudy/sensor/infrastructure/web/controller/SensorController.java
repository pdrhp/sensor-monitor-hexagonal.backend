package com.pedroh.sensorapihexagonalstudy.sensor.infrastructure.web.controller;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input.GetSensorMetricsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Slf4j
public class SensorController {

    private final GetSensorMetricsUseCase getSensorMetricsUseCase;

    @GetMapping(path = "/metrics/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Flux<SensorMetric> streamMetrics() {
        log.info("Cliente conectado ao stream de métricas na thread [{}]",
                Thread.currentThread().getName());

        return getSensorMetricsUseCase.getLatestMetrics()
                .doOnSubscribe(subscription ->
                        log.info("Cliente inscrito no stream de métricas"))
                .doOnCancel(() ->
                        log.info("Cliente desconectado do stream de métricas"))
                .doOnError(error ->
                        log.error("Erro no stream de métricas: {}", error.getMessage(), error));
    }
}
