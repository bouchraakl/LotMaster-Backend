/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/* ----------------------------------------------------- */
@Entity
@Table(name = "modelos", schema = "public")
public class Modelo extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Getter
    @Setter
    @JoinColumn(name = "marca_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Marca marca;

}
