/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/* ----------------------------------------------------- */
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter @Setter
    @Column(name = "dtCadastro", nullable = false)
    @NotNull(message = "Data de cadastro não informada.")
    private LocalDateTime cadastro;

    @Getter @Setter
    @Column(name = "dtAtualizacao")
    private LocalDateTime atualizacao;

    @Getter @Setter
    @Column(name = "ativo", nullable = false)
    @NotNull(message = "Campo ativo não informado.")
    private boolean ativo;

    @PrePersist
    private void prePersist() {
        this.cadastro = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.atualizacao = LocalDateTime.now();
    }
}
