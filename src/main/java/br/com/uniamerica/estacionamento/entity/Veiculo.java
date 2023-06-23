package br.com.uniamerica.estacionamento.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "veiculos", schema = "public")
@AuditTable(value = "veiculos_audit", schema = "audit")
public class Veiculo extends AbstractEntity {

    @Column(name = "placa", nullable = false, unique = true)
    @NotNull(message = "The vehicle's license plate cannot be null.")
    @NotBlank(message = "The vehicle's license plate cannot be empty.")
    @Getter
    @Setter
    private String placa;

    @JoinColumn(name = "modelo_id", nullable = false)
    @ManyToOne
    @NotNull(message = "The model object was not provided.")
    @Getter
    @Setter
    private Modelo modelo;

    @Column(name = "ano", nullable = false)
    @Getter
    @Setter
    private int ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "cor", nullable = false)
    @NotNull(message = "The vehicle's color cannot be null.")
    @Getter
    @Setter
    private Cor cor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @NotNull(message = "The vehicle's type cannot be null.")
    @Getter
    @Setter
    private Tipo tipo;
}


