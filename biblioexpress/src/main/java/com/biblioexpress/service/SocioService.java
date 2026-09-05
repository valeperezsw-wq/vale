package com.biblioexpress.service;

import com.biblioexpress.dto.DepurarDuplicadosResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UNIDAD 2 (validaciones iterativas) + UNIDAD 4 (HashSet).
 */
@Service
public class SocioService {

    /**
     * Recibe una lista de DNIs (con repetidos y/o basura) y devuelve solo los unicos,
     * recorriendo la lista UNA sola pasada.
     *
     * Como funciona el HashSet aca:
     *  - set.add(x) devuelve true si x NO estaba (y lo agrega),
     *    o false si x YA estaba.
     *  - Entonces, en una sola vuelta, si add() da true lo sumamos al resultado;
     *    si da false es un duplicado y lo salteamos.
     *  - Buscar "¿ya lo vi?" en un HashSet es O(1). Con una List seria O(n) por cada
     *    elemento -> O(n^2) en total. Por eso HashSet.
     *
     * Ademas valida cada DNI mientras recorre (validacion iterativa):
     * los que no son numeros de 7-8 digitos van a la lista de invalidos.
     */
    public DepurarDuplicadosResponse depurarDuplicados(List<String> dnis) {
        List<String> entrada = (dnis == null) ? List.of() : dnis;

        Set<String> vistos = new HashSet<>();
        List<String> unicos = new ArrayList<>();
        List<String> invalidos = new ArrayList<>();
        int duplicados = 0;

        for (String dniOriginal : entrada) {
            String dni = (dniOriginal == null) ? "" : dniOriginal.trim();

            if (!esDniValido(dni)) {
                invalidos.add(dniOriginal);
                continue;
            }
            if (vistos.add(dni)) {
                unicos.add(dni);        // primera vez que aparece
            } else {
                duplicados++;           // ya lo habiamos visto
            }
        }

        return new DepurarDuplicadosResponse(
                entrada.size(),
                unicos.size(),
                duplicados,
                unicos,
                invalidos
        );
    }

    /** Regla simple: solo digitos, entre 7 y 8 caracteres. */
    private boolean esDniValido(String dni) {
        if (dni.length() < 7 || dni.length() > 8) {
            return false;
        }
        for (int i = 0; i < dni.length(); i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
