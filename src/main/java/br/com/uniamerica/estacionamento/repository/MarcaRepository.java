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

    @Query("SELECT ma.nome FROM Marca ma WHERE ma.id = :id")
    public String findByNomeID(@Param("id") final Long id);

    @Query("from Marca where ativo = true")
    public List<Marca> findAllAtivo();

    @Query("from Marca where id = :id and ativo = true")
    public List<Marca> findActiveElement(@Param("id") Long id);


}
