/**
 * This interface represents the repository for the entity {@link Marca}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    /**
     * Finds brands by the specified name.
     *
     * @param nome the name of the brand
     * @return a list of brands with the specified name
     */

    @Query("SELECT m FROM Marca m WHERE m.nome like :nome")
    public Marca findByNome(@Param("nome") final String nome);




    /**
     * Retrieves the name of the brand with the specified ID.
     *
     * @param id the ID of the brand
     * @return the name of the brand
     */
    @Query("SELECT ma.nome FROM Marca ma WHERE ma.id = :id")
    public String findByNomeID(@Param("id") final Long id);

    /**
     * Retrieves all active brands.
     *
     * @return a list of active brands
     */
    @Query("from Marca where ativo = true")
    public List<Marca> findAllAtivo();

    /**
     * Retrieves the active brands with the specified ID.
     *
     * @param id the ID of the brand
     * @return a list of active brands with the specified ID
     */
    @Query("from Marca where id = :id and ativo = true")
    public List<Marca> findActiveElement(@Param("id") Long id);
}