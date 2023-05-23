/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.entity;

/* -------------------Imports--------------------------- */
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/* ----------------------------------------------------- */
@Entity
@Audited
@Table(name = "veiculos", schema = "public")
@AuditTable(value = "veiculos_audit",schema = "audit")
public class Veiculo extends AbstractEntity {

    @Getter @Setter
    @Column(name = "placa", nullable = false, unique = true)
    @NotNull(message = "A placa do veiculo não pode ser nula.")
    @NotBlank(message = "A placa do veiculo não pode ser vazia.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Caracteres especiais não são permitidos no campo 'placa'")
    private String placa;

    @Getter @Setter
    @JoinColumn(name = "modelo_id", nullable = false)
    @ManyToOne
    @NotNull(message = "O objeto modelo não foi informado.")
    private Modelo modelo;

    @Getter @Setter
    @Column(name = "ano", nullable = false)
    private int ano;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    @Column(name = "cor", nullable = false)
    @NotNull(message = "A cor do veículo não pode ser nula.")
    private Cor cor;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    @Column(name = "tipo", nullable = false)
    @NotNull(message = "O tipo do veículo não pode ser nulo.")
    private Tipo tipo;


    
}
