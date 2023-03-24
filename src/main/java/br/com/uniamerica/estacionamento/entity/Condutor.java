/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalTime;

/* ----------------------------------------------------- */
@Entity
@Table(name = "condutores", schema = "public")
public class Condutor extends AbstractEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 15)
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 17)
    private String telefone;

    @Column(name = "tempo_gasto")
    private LocalTime tempoPago;

    @Column(name = "tempo_desconto")
    private LocalTime tempoDesconto;

}
