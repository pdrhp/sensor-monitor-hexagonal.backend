package com.pedroh.sensorapihexagonalstudy.sensor.domain.enums;

public enum UnitType {
    Puls("Pulses"),
    F("Fahrenheit"),
    L("Liters");

    private final String description;

    UnitType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
