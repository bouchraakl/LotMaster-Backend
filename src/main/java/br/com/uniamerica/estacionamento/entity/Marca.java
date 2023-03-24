/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/* ----------------------------------------------------- */
@Entity
@Table(name = "marcas", schema = "public")
public class Marca extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

}
