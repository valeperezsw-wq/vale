package com.biblioexpress.multas;

import org.springframework.stereotype.Component;

/**
 * Estrategia por defecto: $100 por cada dia de atraso.
 *
 * @Component hace que Spring cree UNA instancia de esta clase y la guarde en su
 * "contenedor". Despues el PrestamoService pide "todas las MultaStrategy que existan"
 * y Spring le pasa esta, MultaCampania y MultaFinDeSemana automaticamente.
 */
@Component
public class MultaNormal implements MultaStrategy {

    private static final double POR_DIA = 100.0;

    @Override
    public double calcular(int diasAtraso) {
        return diasAtraso * POR_DIA;
    }

    @Override
    public String nombre() {
        return "NORMAL";
    }
}
