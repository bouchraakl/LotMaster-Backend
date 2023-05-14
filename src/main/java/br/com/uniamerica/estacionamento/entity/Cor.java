/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

import jakarta.validation.constraints.NotNull;

/* ----------------------------------------------------- */
@NotNull
public enum Cor {
    PRATA,
    CINZA,
    BRANCO,
    PRETO,
    ROSA,
    AZUL
}
