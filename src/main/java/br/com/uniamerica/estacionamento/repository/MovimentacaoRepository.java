/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.repository;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

/* ----------------------------------------------------- */
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    public List<Movimentacao> findByVeiculo(final Veiculo veiculo);

    @Query(value = "SELECT * FROM movimentacoes mov WHERE mov.entrada IS NOT NULL AND mov.saida IS NULL")
    public List<Movimentacao> findByMovAbertas();

}
