package com.biblioexpress.repository;

import com.biblioexpress.model.Prestamo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * UNIDAD 4 - HASHMAP<codigoMaterial, Prestamo>.
 *
 * Guarda los prestamos ACTIVOS. La clave es el codigo del material porque
 * un material solo puede estar prestado a un socio a la vez.
 * Al devolver, se elimina la entrada.
 */
@Repository
public class PrestamoRepository {

    private final Map<String, Prestamo> prestamosActivos = new HashMap<>();

    public Prestamo guardar(Prestamo prestamo) {
        prestamosActivos.put(prestamo.getCodigoMaterial(), prestamo);
        return prestamo;
    }

    public Optional<Prestamo> buscarPorCodigoMaterial(String codigoMaterial) {
        return Optional.ofNullable(prestamosActivos.get(codigoMaterial));
    }

    public boolean existePrestamoActivo(String codigoMaterial) {
        return prestamosActivos.containsKey(codigoMaterial);
    }

    public void eliminar(String codigoMaterial) {
        prestamosActivos.remove(codigoMaterial);
    }
}
