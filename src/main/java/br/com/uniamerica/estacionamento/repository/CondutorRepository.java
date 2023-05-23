/**
 * This interface represents the repository for the entity {@link Condutor}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CondutorRepository extends JpaRepository<Condutor, Long> {

    /**
     * Retrieves the name of the condutor with the specified ID.
     *
     * @param id the ID of the condutor
     * @return the name of the condutor
     */
    @Query("SELECT c.nome FROM Condutor c WHERE c.id = :id")
    public String findByNome(@Param("id") Long id);

    /**
     * Finds a condutor by their CPF.
     *
     * @param cpf the CPF of the condutor
     * @return the condutor with the specified CPF
     */
    @Query("from Condutor where cpf = :cpf")
    public Condutor findbyCPF(@Param("cpf") final String cpf);

    /**
     * Retrieves all active condutors.
     *
     * @return a list of active condutors
     */
    @Query("from Condutor where ativo = true")
    public List<Condutor> findAllAtivo();

    /**
     * Retrieves the active condutors with the specified ID.
     *
     * @param id the ID of the condutor
     * @return a list of active condutors with the specified ID
     */
    @Query("from Condutor where id = :id and ativo = true")
    public List<Condutor> findActiveElement(@Param("id") Long id);

    /**
     * Retrieves the phone number of the condutor with the specified ID.
     *
     * @param id the ID of the condutor
     * @return the phone number of the condutor
     */
    @Query("SELECT c.telefone FROM Condutor c WHERE c.id = :id")
    public String findByPhone(@Param("id") Long id);
}