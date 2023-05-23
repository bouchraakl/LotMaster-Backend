package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for performing data validations related to modelos.
 * All validations are performed through methods that are executed when creating, updating, or deleting a modelo.
 */
@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    /**
     * Constructs a ModeloService with the specified repositories.
     *
     * @param modeloRepository The modelo repository.
     * @param marcaRepository  The marca repository.
     */
    @Autowired
    public ModeloService(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    /**
     * Validates the information of a new modelo before it is saved to the database.
     *
     * @param modelo The modelo to be validated.
     * @throws IllegalArgumentException If the modelo information is incorrect.
     */
    @Transactional
    public void validarCadastroModelo(Modelo modelo) {
        modelo.setCadastro(LocalDateTime.now());
        validarNomeModelo(modelo.getNome());
        validarIdMarca(modelo.getMarca().getId());
        validarMarcaAtiva(modelo.getMarca().getId());
        modeloRepository.save(modelo);
    }

    /**
     * Validates the information of an existing modelo before it is updated.
     *
     * @param modelo The modelo to be validated.
     * @throws IllegalArgumentException If the modelo information is incorrect.
     */
    @Transactional
    public void validarUpdateModelo(Modelo modelo) {
        modelo.setAtualizacao(LocalDateTime.now());
        validarIdModelo(modelo.getId());
        validarIdMarca(modelo.getMarca().getId());
        validarMarcaAtiva(modelo.getMarca().getId());
        modeloRepository.save(modelo);
    }

    /**
     * Validates the information of a modelo to be deleted.
     *
     * @param id The ID of the modelo to be validated.
     * @throws IllegalArgumentException If the modelo ID does not exist in the database.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteModelo(Long id) {
        validarIdModelo(id);
    }

    /**
     * Validates if a modelo name is already registered in the database.
     *
     * @param nome The name of the modelo to be validated.
     * @throws IllegalArgumentException If a modelo with the provided name already exists.
     */
    private void validarNomeModelo(String nome) {
        Assert.isTrue(modeloRepository.findByNome(nome).isEmpty(),
                "A modelo with the provided name already exists. " +
                        "Please verify the entered data and try again.");
    }

    /**
     * Validates if a marca ID is provided and exists in the database.
     *
     * @param marcaId The ID of the marca associated with the modelo.
     * @throws IllegalArgumentException If the marca ID is not provided or does not exist in the database.
     */
    private void validarIdMarca(Long marcaId) {
        Assert.notNull(marcaId, "The marca ID in modelo cannot be null.");
        Assert.isTrue(marcaRepository.existsById(marcaId),
                "Unable to save the modelo because the associated marca was not found.");
    }

    /**
     * Validates if the marca associated with the modelo is active.
     *
     * @param marcaId The ID of the marca associated with the modelo.
     * @throws IllegalArgumentException If the marca associated with the modelo is inactive.
     */
    private void validarMarcaAtiva(Long marcaId) {
        final List<Marca> isActive = marcaRepository.findActiveElement(marcaId);
        Assert.isTrue(!isActive.isEmpty(), "The marca associated with this modelo is inactive.");
    }

    /**
     * Validates if a modelo ID exists in the database.
     *
     * @param modeloId The ID of the modelo to be validated.
     * @throws IllegalArgumentException If the modelo ID does not exist in the database.
     */
    private void validarIdModelo(Long modeloId) {
        Assert.notNull(modeloId,
                "The provided modelo ID is null. " +
                        "Please ensure that the modelo object has a valid ID before performing this operation.");
        Assert.isTrue(modeloRepository.existsById(modeloId),
                "The specified modelo ID was not found in the database. " +
                        "Please verify that the ID is correct and try again.");
    }
}
