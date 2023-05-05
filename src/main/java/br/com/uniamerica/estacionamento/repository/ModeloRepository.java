//--------------Package--------------------------
package br.com.uniamerica.estacionamento.repository;

//-------------Imports----------------------------

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//------------------------------------------------
@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    public List<Modelo> findByNome(final String nome);

    @Query(value = "SELECT * FROM modelos WHERE ativo = :true", nativeQuery = true)
    public List<Modelo> findAllByActive(@Param("true") final boolean ativo);

    @Query(value = "SELECT * FROM modelos WHERE marca_id = :id", nativeQuery = true)
    public List<Modelo> findByMarcaId(@Param("id") Long id);
}

