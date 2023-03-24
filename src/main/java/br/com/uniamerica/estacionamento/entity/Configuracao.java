/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

/* ----------------------------------------------------- */
@Entity
@Table(name = "configuracoes", schema = "public")
public class Configuracao extends AbstractEntity {

    @Getter
    @Setter
    @Column(name = "valor_hora")
    private BigDecimal valorHora;

    @Getter
    @Setter
    @Column(name = "valor_minutomulta")
    private BigDecimal valorMinutoMulta;

    @Getter
    @Setter
    @Column(name = "inicio-expediente")
    private LocalTime inicioExpediente;

    @Getter
    @Setter
    @Column(name = "fim_expediente")
    private LocalTime fimExpediente;

    @Getter
    @Setter
    @Column(name = "tempo_paradesconto")
    private LocalTime tempoParaDesconto;

    @Getter
    @Setter
    @Column(name = "tempo_dedesconto")
    private LocalTime tempoDeDesconto;

    @Getter
    @Setter
    @Column(name = "gerar_desconto")
    private Boolean gerarDesconto;

    @Getter
    @Setter
    @Column(name = "vagas_moto")
    private int vagasMoto;

    @Getter
    @Setter
    @Column(name = "vagas_carro")
    private int vagasCarro;

    @Getter
    @Setter
    @Column(name = "vagas_van")
    private int vagasVan;


}
