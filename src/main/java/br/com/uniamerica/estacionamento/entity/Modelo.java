package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "modelos", schema = "public")
@AuditTable(value = "modelos_audit", schema = "audit")
public class Modelo extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, unique = true, length = 40)
    @NotNull(message = "The model name cannot be null.")
    @NotBlank(message = "The model name cannot be blank.")
    @Size(min = 2, max = 40, message = "The model name must have a minimum of 2 characters and a maximum of 40 characters.")
    private String nome;

    @Getter
    @Setter
    @JoinColumn(name = "marca_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull(message = "The brand object must be provided.")
    private Marca marca;

}
