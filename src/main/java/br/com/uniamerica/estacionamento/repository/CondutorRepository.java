/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/* ----------------------------------------------------- */
public interface CondutorRepository extends JpaRepository<Condutor, Long> {

    @Query("SELECT c.nome FROM Condutor c WHERE c.id = :id")
    public String findByNome(@Param("id") Long id);

    @Query("from Condutor where cpf = :cpf")
    public Condutor findbyCPF(@Param("cpf") final String cpf);

    @Query("from Condutor where ativo = true")
    public List<Condutor> findAllAtivo();

    @Query("from Condutor where id = :id and ativo = true")
    public List<Condutor> findActiveElement(@Param("id") Long id);

    @Query("SELECT c.telefone FROM Condutor c WHERE c.id = :id")
    public String findByPhone(@Param("id") Long id);

}
