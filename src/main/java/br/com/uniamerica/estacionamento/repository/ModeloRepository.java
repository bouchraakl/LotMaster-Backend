//--------------Package--------------------------
package br.com.uniamerica.estacionamento.repository;

//-------------Imports----------------------------

import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//------------------------------------------------
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    public List<Modelo> findByNome(final String nome);

    @Query(value = "SELECT * FROM modelos WHERE ativo :true",nativeQuery = true)
    public List<Modelo> findAllByActive(@Param("true") final boolean ativo);


}

