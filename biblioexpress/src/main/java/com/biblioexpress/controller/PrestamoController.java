package com.biblioexpress.controller;

import com.biblioexpress.dto.DevolverRequest;
import com.biblioexpress.dto.DevolverResponse;
import com.biblioexpress.dto.PrestarRequest;
import com.biblioexpress.dto.PrestamoResponse;
import com.biblioexpress.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UNIDAD 2 - CAPA CONTROLLER.
 *
 * Solo se ocupa de HTTP: mapear la URL, leer el body JSON (@RequestBody),
 * validarlo (@Valid) y delegar al service. NO tiene logica de negocio.
 *
 *  POST /api/prestamos/prestar
 *  POST /api/prestamos/devolver
 *  POST /api/prestamos/estrategia   (extra)
 *  GET  /api/prestamos/estrategia   (extra)
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/prestar")
    public PrestamoResponse prestar(@Valid @RequestBody PrestarRequest request) {
        return prestamoService.prestar(request);
    }

    @PostMapping("/devolver")
    public DevolverResponse devolver(@Valid @RequestBody DevolverRequest request) {
        return prestamoService.devolver(request);
    }

    @PostMapping("/estrategia")
    public Map<String, String> cambiarEstrategia(@RequestParam String tipo) {
        String activa = prestamoService.cambiarEstrategia(tipo);
        return Map.of("estrategiaActiva", activa);
    }

    @GetMapping("/estrategia")
    public Map<String, String> verEstrategia() {
        return Map.of("estrategiaActiva", prestamoService.getEstrategiaActiva());
    }
}
