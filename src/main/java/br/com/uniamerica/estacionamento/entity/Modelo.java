/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/* ----------------------------------------------------- */
@Entity
@Audited
@Table(name = "modelos", schema = "public")
@AuditTable(value = "modelos_audit",schema = "audit")

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
