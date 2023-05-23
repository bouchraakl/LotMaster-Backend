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
    @NotNull(message = "A placa do veiculo não pode ser nula.")
    @NotBlank(message = "A placa do veiculo não pode ser vazia.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Caracteres especiais não são permitidos no campo 'placa'")
    @Getter
    @Setter
    private String placa;

    @JoinColumn(name = "modelo_id", nullable = false)
    @ManyToOne
    @NotNull(message = "O objeto modelo não foi informado.")
    @Getter
    @Setter
    private Modelo modelo;

    @Column(name = "ano", nullable = false)
    @Getter
    @Setter
    private int ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "cor", nullable = false)
    @NotNull(message = "A cor do veículo não pode ser nula.")
    @Getter
    @Setter
    private Cor cor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @NotNull(message = "O tipo do veículo não pode ser nulo.")
    @Getter
    @Setter
    private Tipo tipo;
}


