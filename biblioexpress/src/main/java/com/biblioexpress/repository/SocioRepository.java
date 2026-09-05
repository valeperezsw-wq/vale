package com.biblioexpress.repository;

import com.biblioexpress.model.Socio;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * UNIDAD 4 - HASHMAP<id, Socio>.
 * Mismo criterio que MaterialRepository: el id del socio es unico -> clave del mapa.
 */
@Repository
public class SocioRepository {

    private final Map<Integer, Socio> sociosPorId = new HashMap<>();

    public Socio guardar(Socio socio) {
        sociosPorId.put(socio.getId(), socio);
        return socio;
    }

    public Optional<Socio> buscarPorId(int id) {
        return Optional.ofNullable(sociosPorId.get(id));
    }
}
