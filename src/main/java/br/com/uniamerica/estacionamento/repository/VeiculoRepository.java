/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Tipo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/* ----------------------------------------------------- */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("FROM Veiculo WHERE placa = :placa")
    public Veiculo findByPlaca(@Param("placa") final String placa);

    @Query("SELECT v.placa FROM Veiculo v WHERE v.id = :id")
    public String findByPlacaID(@Param("id") final Long id);

    @Query("SELECT v.ano FROM Veiculo v WHERE v.id = :id")
    public String findByAnoID(@Param("id") final Long id);

    @Query("from Veiculo where ativo = true")
    public List<Veiculo> findAllAtivo();

    @Query(value = "FROM veiculos WHERE modelo.id = :id", nativeQuery = true)
    public List<Movimentacao> findByModeloId(@Param("id") final Long id);

    @Query("from Veiculo where id = :id and ativo = true")
    public List<Veiculo> findActiveElement(@Param("id") Long id);

    @Query("SELECT v.tipo FROM Veiculo v WHERE v.id = :id")
    Tipo getTipoVeiculo(@Param("id") Long id);

}
