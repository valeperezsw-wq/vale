package com.biblioexpress.dto;

import java.util.List;

/**
 * UNIDAD 2 + 4 - DTO de salida de la depuracion de duplicados.
 *
 *  - cantidadRecibidos: cuantos DNIs llegaron
 *  - cantidadUnicos: cuantos quedaron despues de sacar repetidos
 *  - duplicadosDescartados: cuantos se tiraron por repetidos
 *  - dnisUnicos: la lista final, en el orden en que aparecieron
 *  - dnisInvalidos: los que se ignoraron por formato (validacion iterativa)
 */
public record DepurarDuplicadosResponse(
        int cantidadRecibidos,
        int cantidadUnicos,
        int duplicadosDescartados,
        List<String> dnisUnicos,
        List<String> dnisInvalidos
) {
}
