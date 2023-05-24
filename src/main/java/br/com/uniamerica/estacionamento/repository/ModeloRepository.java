/**
 * This interface represents the repository for the entity {@link Modelo}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    /**
     * Finds models by the specified name.
     *
     * @param nome the name of the model
     * @return a list of models with the specified name
     */
    public Modelo findByNome(final String nome);

    /**
     * Retrieves all active models.
     *
     * @return a list of active models
     */
    @Query("from Modelo where ativo = true")
    public List<Modelo> findAllAtivo();

    /**
     * Retrieves models by the specified brand ID.
     *
     * @param id the ID of the brand
     * @return a list of models with the specified brand ID
     */
    @Query(value = "SELECT * FROM modelos WHERE marca_id = :id", nativeQuery = true)
    public List<Modelo> findByMarcaId(@Param("id") Long id);

    /**
     * Retrieves the name of the model with the specified ID.
     *
     * @param id the ID of the model
     * @return the name of the model
     */
    @Query("SELECT m.nome FROM Modelo m WHERE m.id = :id")
    public String findByNomeID(@Param("id") final Long id);

    /**
     * Retrieves the active models with the specified ID.
     *
     * @param id the ID of the model
     * @return a list of active models with the specified ID
     */
    @Query("from Modelo where id = :id and ativo = true")
    public List<Modelo> findActiveElement(@Param("id") Long id);
}