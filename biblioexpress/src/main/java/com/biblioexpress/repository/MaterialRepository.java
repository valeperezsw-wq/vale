package com.biblioexpress.repository;

import com.biblioexpress.model.Material;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UNIDAD 4 - HASHMAP.
 *
 * Guarda los materiales en memoria en un HashMap<codigo, Material>.
 *
 * Por que HashMap y no una List?
 *  - Buscar un material por codigo es O(1) (instantaneo), no hay que recorrer la lista.
 *  - El codigo es unico por naturaleza: encaja perfecto como clave.
 *
 * @Repository: le dice a Spring "esto es la capa de acceso a datos", y crea una
 * unica instancia compartida por toda la app (por eso los datos "persisten" mientras
 * la app este prendida).
 */
@Repository
public class MaterialRepository {

    private final Map<String, Material> materialesPorCodigo = new HashMap<>();

    public Material guardar(Material material) {
        materialesPorCodigo.put(material.getCodigo(), material);
        return material;
    }

    public Optional<Material> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(materialesPorCodigo.get(codigo));
    }

    public List<Material> listarTodos() {
        return new ArrayList<>(materialesPorCodigo.values());
    }

    /** Recorre los valores del HashMap y devuelve solo los disponibles. */
    public List<Material> listarDisponibles() {
        List<Material> disponibles = new ArrayList<>();
        for (Material material : materialesPorCodigo.values()) {
            if (material.isDisponible()) {
                disponibles.add(material);
            }
        }
        return disponibles;
    }
}
