/**
 * This interface represents the repository for the entity {@link Veiculo}.
 * It extends the {@link JpaRepository} interface, providing CRUD operations for the entity.
 */
package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Tipo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    /**
     * Finds a vehicle by the specified license plate.
     *
     * @param placa the license plate
     * @return the vehicle with the specified license plate
     */
    @Query("FROM Veiculo WHERE placa = :placa")
    public Veiculo findByPlaca(@Param("placa") final String placa);

    /**
     * Retrieves the license plate of the vehicle with the specified ID.
     *
     * @param id the ID of the vehicle
     * @return the license plate of the vehicle
     */
    @Query("SELECT v.placa FROM Veiculo v WHERE v.id = :id")
    public String findByPlacaID(@Param("id") final Long id);

    /**
     * Retrieves the year of the vehicle with the specified ID.
     *
     * @param id the ID of the vehicle
     * @return the year of the vehicle
     */
    @Query("SELECT v.ano FROM Veiculo v WHERE v.id = :id")
    public String findByAnoID(@Param("id") final Long id);

    /**
     * Retrieves all active vehicles.
     *
     * @return a list of active vehicles
     */
    @Query("from Veiculo where ativo = true")
    public List<Veiculo> findAllAtivo();

    /**
     * Retrieves movements associated with the vehicle of the specified model ID.
     *
     * @param id the ID of the model
     * @return a list of movements associated with the specified model ID
     */
    @Query("FROM Veiculo WHERE id = :id")
    public List<Movimentacao> findByModeloId(@Param("id") final Long id);

    /**
     * Retrieves the active vehicle with the specified ID.
     *
     * @param id the ID of the vehicle
     * @return a list containing the active vehicle with the specified ID
     */
    @Query("from Veiculo where id = :id and ativo = true")
    public List<Veiculo> findActiveElement(@Param("id") Long id);

    /**
     * Retrieves the vehicle type of the vehicle with the specified ID.
     *
     * @param id the ID of the vehicle
     * @return the vehicle type
     */
    @Query("SELECT v.tipo FROM Veiculo v WHERE v.id = :id")
    Tipo getTipoVeiculo(@Param("id") Long id);

    @Query("SELECT COUNT(v) > 0 FROM Veiculo v WHERE v.placa = :placa AND v.id <> :id")
    boolean existsByPlacaAndIdNot(@Param("placa") String placa, @Param("id") Long id);

    @Query("SELECT COUNT(v) > 0 FROM Veiculo v WHERE v.placa = :placa")
    boolean existsByPlaca(@Param("placa") String placa);
}