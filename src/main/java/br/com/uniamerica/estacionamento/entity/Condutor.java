package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Audited
@Table(name = "condutores", schema = "public")
@AuditTable(value = "condutores_audit", schema = "audit")
public class Condutor extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "nome", nullable = false, length = 30)
    @NotNull(message = "The driver's name cannot be null.")
    @Size(min = 2, max = 30, message = "The driver's name must have at least 2 characters and at most 30 characters.")
    @NotBlank(message = "The driver's name cannot be empty.")
    private String nome;

    @Getter
    @Setter
    @Column(name = "cpf", nullable = false, unique = true, length = 15)
    @CPF
    @NotNull(message = "The driver's CPF cannot be null.")
    @NotBlank(message = "The driver's CPF cannot be empty.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "The CPF format is invalid. The correct format is xxx.xxx.xxx-xx.")
    private String cpf;


    @Getter
    @Setter
    @Column(name = "telefone", nullable = false)
    @NotNull(message = "The driver's phone number cannot be null.")
    @NotBlank(message = "The driver's phone number cannot be empty.")
    private String telefone;

    @Getter
    @Setter
    @Column(name = "tempohoras_gasto")
    private int tempoPagoHoras;

    @Getter
    @Setter
    @Column(name = "tempominutos_gasto")
    private int tempoPagoMinutos;

    @Getter
    @Setter
    @Column(name = "tempo_descontoHoras")
    private int tempoDescontoHoras;

}
