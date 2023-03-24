package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CondutorRepository extends JpaRepository<Condutor,Long> {

    @Query(value = "select * from condutores where nome like :nome",nativeQuery = true)
        public List<Condutor> findByNomeLikeNative(@Param("nome") final String nome);

}
