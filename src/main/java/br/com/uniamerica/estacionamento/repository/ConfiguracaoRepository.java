/**
 * This interface represents the repository for the entity {@link Configuracao}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {

    /**
     * Finds configurations by the specified hourly value.
     *
     * @param valorHora the hourly value
     * @return a list of configurations with the specified hourly value
     */
    public List<Configuracao> findByValorHora(final BigDecimal valorHora);

    /**
     * Finds a configuration by the specified penalty minute value.
     *
     * @param valorMinutoMulta the penalty minute value
     * @return the configuration with the specified penalty minute value
     */
    public Configuracao findByValorMinutoMulta(final BigDecimal valorMinutoMulta);

    /**
     * Retrieves the latest configuration.
     *
     * @return the latest configuration
     */
    @Query(value = "SELECT c FROM Configuracao c ORDER BY c.id DESC LIMIT 1")
    Configuracao ultimaConfiguracao();
}