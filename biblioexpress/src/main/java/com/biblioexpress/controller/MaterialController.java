package com.biblioexpress.controller;

import com.biblioexpress.dto.MaterialResponse;
import com.biblioexpress.service.MaterialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UNIDAD 2 - CAPA CONTROLLER.
 *
 *  GET /api/materiales/disponibles
 */
@RestController
@RequestMapping("/api/materiales")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/disponibles")
    public List<MaterialResponse> disponibles() {
        return materialService.listarDisponibles();
    }
}
