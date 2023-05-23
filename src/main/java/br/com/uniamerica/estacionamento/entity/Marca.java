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
    @NotNull(message = "O nome da marca não pode ser nula.")
    @NotBlank(message = "O nome do marca não pode ser vazia.")
    @Size(min = 2,max = 30,message = "O nome do condutor deve ter no minimo 2 caracteres e no máximo 30 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Caracteres especiais não são permitidos no campo 'nome'")
    private String nome;

    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate() {
        this.nome = this.nome.toLowerCase();
    }

}
