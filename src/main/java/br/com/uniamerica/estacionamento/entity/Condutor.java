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
    @Column(name = "nome", nullable = false, length = 40)
    @NotNull(message = "O nome do condutor não pode ser nulo.")
    @Size(min = 2, max = 30, message = "O nome do condutor deve ter no mínimo 2 caracteres e no máximo 30 caracteres.")
    @NotBlank(message = "O nome do condutor não pode ser vazio.")
    private String nome;

    @Getter
    @Setter
    @Column(name = "cpf", nullable = false, unique = true, length = 15)
    @CPF
    @NotNull(message = "O CPF do condutor não pode ser nulo.")
    @NotBlank(message = "O CPF do condutor não pode ser vazio.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "O formato do CPF é inválido. O formato correto é xxx.xxx.xxx-xx.")
    private String cpf;

    @Getter
    @Setter
    @Column(name = "telefone", nullable = false)
    @NotNull(message = "O telefone do condutor não pode ser nulo.")
    @NotBlank(message = "O telefone do condutor não pode ser vazio.")
    @Pattern(regexp = "(^\\+55\\s?\\(\\d{2}\\)\\d{9}$)|(^\\+55\\s?\\(\\d{2}\\)\\s?\\d{5}\\s?\\d{4}$)",
            message = "O número de telefone fornecido não está no formato válido." +
                    " O formato deve seguir o padrão: +55(xx)xxxxxxxxx ou +55 (xx) xxxxx xxxx." +
                    " Por favor, corrija o número de telefone e tente novamente.")
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

    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate() {
        this.nome = this.nome.toLowerCase();
    }
}
