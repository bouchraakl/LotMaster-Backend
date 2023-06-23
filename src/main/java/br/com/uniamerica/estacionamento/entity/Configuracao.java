package br.com.uniamerica.estacionamento.entity;

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

@Entity
@Audited
@Table(name = "configuracoes", schema = "public")
@AuditTable(value = "configuracoes_audit", schema = "audit")
public class Configuracao extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "valor_hora", nullable = false)
    @NotNull(message = "The hour rate cannot be null.")
    private BigDecimal valorHora;

    @Getter
    @Setter
    @Column(name = "valor_minutomulta", nullable = false)
    @NotNull(message = "The minute penalty value cannot be null.")
    private BigDecimal valorMinutoMulta;

    @Getter
    @Setter
    @Column(name = "inicio-expediente", nullable = false)
    @NotNull(message = "The opening time cannot be null.")
    private LocalTime inicioExpediente;

    @Getter
    @Setter
    @Column(name = "fim_expediente", nullable = false)
    @NotNull(message = "The closing time cannot be null.")
    private LocalTime fimExpediente;

    @Getter
    @Setter
    @Column(name = "tempo_paradesconto", nullable = false)
    @NotNull(message = "The discount time limit cannot be null.")
    private int tempoParaDesconto;

    @Getter
    @Setter
    @Column(name = "tempo_dedesconto", nullable = false)
    @NotNull(message = "The discount time value cannot be null.")
    private int tempoDeDesconto;

    @Getter
    @Setter
    @Column(name = "gerar_desconto", nullable = false)
    @NotNull(message = "The generate discount field cannot be null.")
    private Boolean gerarDesconto;

    @Getter
    @Setter
    @Column(name = "vagas_moto", nullable = false)
    @NotNull(message = "The motorcycle spaces field cannot be null.")
    private int vagasMoto;

    @Getter
    @Setter
    @Column(name = "vagas_carro", nullable = false)
    @NotNull(message = "The car spaces field cannot be null.")
    private int vagasCarro;

    @Getter
    @Setter
    @Column(name = "vagas_van", nullable = false)
    @NotNull(message = "The van spaces field cannot be null.")
    private int vagasVan;

}
