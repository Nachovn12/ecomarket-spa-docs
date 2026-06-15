package com.ecomarket.usuarios.util;

import com.ecomarket.usuarios.exception.CredencialesInvalidasException;

/**
 * Validador de RUN/RUT chileno (algoritmo modulo 11).
 * Acepta formatos con o sin puntos: "12345678-9" o "12.345.678-9".
 */
public final class RutValidador {

    private RutValidador() {}

    public static String normalizar(String run) {
        if (run == null) return null;
        return run.replace(".", "").replace(" ", "").toUpperCase();
    }

    public static boolean esValido(String run) {
        if (run == null) return false;
        String limpio = normalizar(run);
        if (limpio.length() < 2) return false;

        String cuerpo;
        String dvIngresado;
        int idxGuion = limpio.indexOf('-');
        if (idxGuion >= 0) {
            cuerpo = limpio.substring(0, idxGuion);
            dvIngresado = limpio.substring(idxGuion + 1);
        } else {
            cuerpo = limpio.substring(0, limpio.length() - 1);
            dvIngresado = limpio.substring(limpio.length() - 1);
        }

        if (!cuerpo.matches("\\d+")) return false;

        int dvCalculado = calcularDv(cuerpo);
        return String.valueOf((char) (dvCalculado == 10 ? 'K' : '0' + dvCalculado)).equals(dvIngresado);
    }

    public static int calcularDv(String cuerpo) {
        int suma = 0;
        int multiplicador = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }
        int resto = suma % 11;
        int dv = 11 - resto;
        if (dv == 11) {
            dv = 0;
        }
        return dv;
    }

    public static void validar(String run) {
        if (!esValido(run)) {
            throw new CredencialesInvalidasException("RUN invalido: " + run);
        }
    }
}
