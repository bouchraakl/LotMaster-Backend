/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/* ----------------------------------------------------- */
@Entity
@Audited
@Table(name = "marcas", schema = "public")
@AuditTable(value = "marcas_audit",schema = "audit")
public class Marca extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false,length = 30)
    @NotNull(message = "O nome da marca não pode ser nula.")
    @NotBlank(message = "O nome do marca não pode ser vazia.")
    @Size(min = 2,max = 30,message = "O nome do condutor deve ter no máximo 30 caracteres.")
    private String nome;

}
