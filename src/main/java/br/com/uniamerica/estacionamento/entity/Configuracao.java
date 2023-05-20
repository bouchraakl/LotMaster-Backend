/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalTime;

/* ----------------------------------------------------- */
@Entity
@Audited
@Table(name = "configuracoes", schema = "public")
@AuditTable(value = "configuracoes_audit", schema = "audit")
public class Configuracao extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "valor_hora",nullable = false)
    @NotNull(message = "O valor da hora não pode ser nulo.")
    private BigDecimal valorHora;

    @Getter
    @Setter
    @Column(name = "valor_minutomulta",nullable = false)
    @NotNull(message = "O valor da minuto multa não pode ser nulo.")
    private BigDecimal valorMinutoMulta;

    @Getter
    @Setter
    @Column(name = "inicio-expediente",nullable = false)
    @NotNull(message = "O horário de início de expediente não pode ser nulo.")
    private LocalTime inicioExpediente;

    @Getter
    @Setter
    @Column(name = "fim_expediente",nullable = false)
    @NotNull(message = "O horário de fim de expediente não pode ser nulo.")
    private LocalTime fimExpediente;

    @Getter
    @Setter
    @Column(name = "tempo_paradesconto",nullable = false)
    @NotNull(message = "O tempo para desconto não pode ser nulo.")
    private int tempoParaDesconto;

    @Getter
    @Setter
    @Column(name = "tempo_dedesconto",nullable = false)
    @NotNull(message = "O tempo de desconto não pode ser nulo.")
    private LocalTime tempoDeDesconto;

    @Getter
    @Setter
    @Column(name = "gerar_desconto",nullable = false)
    @NotNull(message = "O campo gerarDesconto não pode ser nulo.")
    private Boolean gerarDesconto;

    @Getter
    @Setter
    @Column(name = "vagas_moto",nullable = false)
    @NotNull(message = "O campo vagas moto não pode ser nulo.")
    private int vagasMoto;

    @Getter
    @Setter
    @Column(name = "vagas_carro",nullable = false)
    @NotNull(message = "O campo vagas carro não pode ser nulo.")
    private int vagasCarro;

    @Getter
    @Setter
    @Column(name = "vagas_van",nullable = false)
    @NotNull(message = "O campo vagas van não pode ser nulo.")
    private int vagasVan;

}
