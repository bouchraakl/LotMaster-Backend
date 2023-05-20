/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Tipo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/* ----------------------------------------------------- */
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    public List<Movimentacao> findByVeiculo(final Veiculo veiculo);

    @Query("from Movimentacao where saida = null")
    public List<Movimentacao> findAllAbertas();

    @Query(value = "SELECT * FROM movimentacoes WHERE condutor_id = :id", nativeQuery = true)
    public List<Movimentacao> findByCondutorId(@Param("id") Long id);


    @Query(value = "SELECT * FROM movimentacoes WHERE veiculo_id = :id", nativeQuery = true)
    public List<Movimentacao> findByVeiculoId(@Param("id") Long id);

}
