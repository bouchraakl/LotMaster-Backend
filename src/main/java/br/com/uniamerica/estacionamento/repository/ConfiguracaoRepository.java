/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.math.BigDecimal;
import java.util.List;

/* ----------------------------------------------------- */
public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {

    public List<Configuracao> findByValorHora(final BigDecimal valorHora);

    public Configuracao findByValorMinutoMulta(final BigDecimal valorMinutoMulta);

    @Query(value = "SELECT c FROM Configuracao c ORDER BY c.id DESC LIMIT 1")
    Configuracao ultimaConfiguracao();
}
