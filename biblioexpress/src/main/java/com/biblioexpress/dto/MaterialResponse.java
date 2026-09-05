package com.biblioexpress.dto;

import com.biblioexpress.model.Libro;
import com.biblioexpress.model.Material;
import com.biblioexpress.model.Revista;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * UNIDAD 2 - DTO de salida para representar un Material en el JSON.
 *
 * Por que no devolvemos el objeto Material directo?
 *  - No queremos atar la API a la estructura interna de las clases.
 *  - Queremos un formato plano y parejo (tipo, autor, numeroEdicion en el mismo nivel).
 *
 * @JsonInclude(NON_NULL): los campos en null no aparecen en el JSON
 * (una Revista no muestra "autor", un Libro no muestra "numeroEdicion").
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaterialResponse(
        String codigo,
        String titulo,
        String tipo,
        boolean disponible,
        String autor,
        Integer numeroEdicion
) {

    /** Fabrica: convierte un Material del modelo en este DTO. */
    public static MaterialResponse desde(Material m) {
        String autor = (m instanceof Libro libro) ? libro.getAutor() : null;
        Integer edicion = (m instanceof Revista revista) ? revista.getNumeroEdicion() : null;
        return new MaterialResponse(
                m.getCodigo(),
                m.getTitulo(),
                m.getTipo(),
                m.isDisponible(),
                autor,
                edicion
        );
    }
}
