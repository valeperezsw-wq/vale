package com.biblioexpress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * UNIDAD 2 - DTO de entrada para POST /api/prestamos/prestar.
 *
 * Es un "record": clase inmutable con getters automaticos. Solo transporta datos.
 * Las anotaciones @NotBlank / @Positive validan el JSON que manda el cliente:
 * si falta el codigo o el socioId es <= 0, Spring responde 400 antes de entrar al service.
 */
public record PrestarRequest(

        @Positive(message = "socioId debe ser un numero positivo")
        int socioId,

        @NotBlank(message = "codigoMaterial es obligatorio")
        String codigoMaterial
) {
}
