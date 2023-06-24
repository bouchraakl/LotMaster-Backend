/**
 * This interface represents the repository for the entity {@link Movimentacao}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Tipo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    /**
     * Finds movements by the specified vehicle.
     *
     * @param veiculo the vehicle
     * @return a list of movements associated with the specified vehicle
     */
    public List<Movimentacao> findByVeiculo(final Veiculo veiculo);

    /**
     * Retrieves all open movements.
     *
     * @return a list of open movements
     */
    @Query("from Movimentacao where saida = null")
    public List<Movimentacao> findAllAbertas();

    /**
     * Retrieves movements by the specified condutor ID.
     *
     * @param id the ID of the condutor
     * @return a list of movements associated with the specified condutor ID
     */
    @Query(value = "SELECT * FROM movimentacoes WHERE condutor_id = :id", nativeQuery = true)
    public List<Movimentacao> findByCondutorId(@Param("id") Long id);

    /**
     * Retrieves movements by the specified vehicle type.
     *
     * @param tipo the type of the vehicle
     * @return a list of movements associated with the specified vehicle type
     */
    @Query("SELECT m FROM Movimentacao m JOIN m.veiculo v WHERE v.tipo = :tipo")
    List<Movimentacao> findByVeiculoTipo(@Param("tipo") Tipo tipo);

    @Query("SELECT m FROM Movimentacao m JOIN m.veiculo v WHERE v.id = :id")
    List<Movimentacao> findByVeiculoId(@Param("id") Long id);

    @Query("from Movimentacao where saida != null")
    public List<Movimentacao> findAllFechadas();

    @Query(value = "SELECT m FROM Movimentacao m ORDER BY m.id DESC LIMIT 1")
    Movimentacao ultimaMovimentacao();
    @Query("SELECT m FROM Movimentacao m ORDER BY m.entrada DESC LIMIT 4")
    List<Movimentacao> findLastFiveByOrderByEntryDateDesc();
    @Query("SELECT m FROM Movimentacao m JOIN m.veiculo v WHERE v.placa = :placa AND m.saida IS NULL")
    List<Movimentacao> findMovimentacoesAbertasByPlaca(@Param("placa") String placa);

}