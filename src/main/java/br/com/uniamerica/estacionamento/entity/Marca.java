package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "marcas", schema = "public")
@AuditTable(value = "marcas_audit", schema = "audit")
public class Marca extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, length = 30)
    @NotNull(message = "The brand name field is required.")
    @NotBlank(message = "The brand name field must not be empty.")
    @Size(min = 2, max = 30, message = "The brand name must be between 2 and 30 characters in length.")
    private String nome;


}
