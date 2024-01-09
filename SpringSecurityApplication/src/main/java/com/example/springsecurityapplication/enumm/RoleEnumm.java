package com.example.springsecurityapplication.enumm;

public enum RoleEnumm {
    ROLE_ADMIN("роль админа"),
    ROLE_USER("роль пользователя");

    private final String displayValue;

    private RoleEnumm(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }
}