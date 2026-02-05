package org.mario.authservice.authservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeración que representa el estado de un usuario.
 * <p>
 * Esta enumeración define dos estados posibles para un usuario:
 * - A: Activo
 * - I: Inactivo
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("A", "Activo"),
    INACTIVE("I", "Inactivo");

    private final String code;
    private final String description;
}
