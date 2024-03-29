package com.example.springsecurityapplication.enumm;

public enum Status {
    Принят("принят"),
    оформлен("оформлен"),
    Ожидает("ожидает"),
    Получен("получен");

    private final String displayValue;

    private Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}