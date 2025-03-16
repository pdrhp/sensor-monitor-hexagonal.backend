package com.pedroh.sensorapihexagonalstudy.sensor.application.usecase;

import com.pedroh.sensorapihexagonalstudy.sensor.domain.model.SensorMetric;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input.GetSensorMetricsUseCase;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.input.ProcessSensorMetricUseCase;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output.SensorMetricPersistencePort;
import com.pedroh.sensorapihexagonalstudy.sensor.domain.port.output.SensorMetricQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService implements ProcessSensorMetricUseCase, GetSensorMetricsUseCase {


    private final Sinks.Many<SensorMetric> metricSink = Sinks.many().multicast().onBackpressureBuffer();

    private final SensorMetricPersistencePort saveSensorMetricPort;
    private final SensorMetricQueryPort sensorMetricQueryPort;

    @Override
    public void process(SensorMetric sensorMetric) {
        log.info("Processando métrica do sensor na thread [{}]: {}",
                Thread.currentThread().getName(), sensorMetric);

        saveSensorMetricPort.saveAsync(sensorMetric)
                .thenAccept(savedMetric -> {
                            log.info("Callback após salvamento assíncrono na thread [{}], ID: {}",
                                    Thread.currentThread().getName(), savedMetric.getId());

                            Sinks.EmitResult result = metricSink.tryEmitNext(savedMetric);
                            if (result.isSuccess()) {
                                log.info("Métrica salva publicada com sucesso para clientes SSE na thread [{}]",
                                        Thread.currentThread().getName());
                            } else {
                                log.warn("Falha ao publicar métrica salva para clientes SSE: {}", result);
                            }
                        }
                )
                .exceptionally(ex -> {
                    log.error("Erro no salvamento assíncrono na thread [{}]: {}",
                            Thread.currentThread().getName(), ex.getMessage(), ex);
                    return null;
                });

        log.info("Método process concluído na thread [{}] (não aguardou o salvamento)",
                Thread.currentThread().getName());
    }

    @Override
    public Flux<SensorMetric> getLatestMetrics() {
        return Flux.concat(
                Flux.defer(() ->
                        Flux.fromIterable(sensorMetricQueryPort.findRecentMetrics(20))
                ).subscribeOn(Schedulers.boundedElastic()),

                metricSink.asFlux()
        );
    }
}
