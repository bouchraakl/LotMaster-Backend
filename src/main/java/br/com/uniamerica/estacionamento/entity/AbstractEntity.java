package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter @Setter
    @Column(name = "dtCadastro")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cadastro;

    @Getter
    @Setter
    @Column(name = "dtAtualizacao")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime atualizacao;

    @Getter
    @Setter
    @Column(name = "ativo", nullable = false)
    @NotNull(message = "Campo ativo não informado.")
    private boolean ativo;

    @PrePersist
    private void updateCadastroOnPersist() {
        this.cadastro = LocalDateTime.now();
    }

    @PreUpdate
    private void updateAtualizacaoOnUpdate() {
        this.atualizacao = LocalDateTime.now();
    }


}
