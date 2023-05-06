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
    public List<Condutor> findByNome(final String nome);

    @Query(value = "SELECT * FROM condutores WHERE cpf like :cpf",nativeQuery = true)
    public List<Condutor> findbyCPF(@Param("cpf") final String cpf);

    @Query(value = "SELECT * FROM condutores WHERE ativo like :true", nativeQuery = true)
    public List<Condutor> findAllByActive(@Param("true") final boolean ativo);

}
