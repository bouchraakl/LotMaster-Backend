/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

import jakarta.validation.constraints.NotNull;

/* ----------------------------------------------------- */
@NotNull
public enum Tipo {
    CARRO,
    MOTO,
    VAN
}
