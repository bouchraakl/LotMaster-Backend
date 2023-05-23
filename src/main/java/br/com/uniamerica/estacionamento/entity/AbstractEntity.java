/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
    @Column(name = "dtCadastro")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cadastro;

    @Getter @Setter
    @Column(name = "dtAtualizacao")
    private LocalDateTime atualizacao;

    @Getter @Setter
    @Column(name = "ativo", nullable = false)
    @NotNull(message = "Campo ativo não informado.")
    @AssertTrue(message = "O campo 'ativo' deve ser 'true' ou 'false'")
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
