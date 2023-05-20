/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.UniqueElements;

/* ----------------------------------------------------- */
@Entity
@Audited
@Table(name = "modelos", schema = "public")
@AuditTable(value = "modelos_audit",schema = "audit")

public class Modelo extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, unique = true,length = 40)
    @NotNull(message = "O nome do modelo não pode ser nulo.")
    @NotBlank(message = "O nome do modelo não pode ser vazio.")
    @Size(min = 2,max = 40,message = "O nome do condutor deve ter no máximo 40 caracteres.")
    private String nome;

    @Getter
    @Setter
    @JoinColumn(name = "marca_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "O objeto marca não foi informado.")
    private Marca marca;


}
