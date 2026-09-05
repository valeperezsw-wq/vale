package com.biblioexpress.service;

import com.biblioexpress.dto.MaterialResponse;
import com.biblioexpress.model.Material;
import com.biblioexpress.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * UNIDAD 2 - capa de servicio para consultas de materiales.
 */
@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * Devuelve los materiales disponibles ya convertidos a DTO
     * (el controller nunca ve las clases del modelo).
     */
    public List<MaterialResponse> listarDisponibles() {
        List<MaterialResponse> resultado = new ArrayList<>();
        for (Material material : materialRepository.listarDisponibles()) {
            resultado.add(MaterialResponse.desde(material));
        }
        return resultado;
    }
}
