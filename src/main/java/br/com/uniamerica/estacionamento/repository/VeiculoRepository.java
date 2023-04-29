/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/* ----------------------------------------------------- */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    public List<Veiculo> findByPlaca(final String placa);

    @Query(value = "SELECT * FROM veiculos WHERE ativo :true", nativeQuery = true)
    public List<Veiculo> findAllByActive(@Param("true") final boolean ativo);

    @Query(value = "FROM veiculos WHERE modelo.id = :id", nativeQuery = true)
    public List<Movimentacao> findByModeloId(@Param("id") final Long id);


}
