/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/* ----------------------------------------------------- */
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    public List<Marca> findByNome(final String nome);

    @Query(value = "select * from marcas where ativo = :true", nativeQuery = true)
    public List<Marca> findAllByActive(@Param("true") final boolean ativo);

}
