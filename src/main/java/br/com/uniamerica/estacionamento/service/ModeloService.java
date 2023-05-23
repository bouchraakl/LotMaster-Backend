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
        validarNomeModelo(modelo);
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
        List<Modelo> findbyNome = modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(findbyNome.isEmpty(),"Nome ja existe no banco de dados.");
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
     * @param modelo the modelo to be validated.
     * @throws IllegalArgumentException If a modelo with the provided name already exists.
     */
    private void validarNomeModelo(Modelo modelo) {
        // Verificar se o nome do modelo já existe no banco de dados
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(),
                "Um modelo já está registrado com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");
    }

    /**
     * Validates if a marca ID is provided and exists in the database.
     *
     * @param marcaId The ID of the marca associated with the modelo.
     * @throws IllegalArgumentException If the marca ID is not provided or does not exist in the database.
     */
    private void validarIdMarca(Long marcaId) {
        Assert.notNull(marcaId, "O ID da marca em modelo não pode ser nulo.");
        Assert.isTrue(marcaRepository.existsById(marcaId),
                "Não foi possível salvar o modelo, pois a marca associada não foi encontrada.");
    }

    /**
     * Validates if the marca associated with the modelo is active.
     *
     * @param marcaId The ID of the marca associated with the modelo.
     * @throws IllegalArgumentException If the marca associated with the modelo is inactive.
     */
    private void validarMarcaAtiva(Long marcaId) {
        final List<Marca> isActive = marcaRepository.findActiveElement(marcaId);
        Assert.isTrue(!isActive.isEmpty(), "A marca associada a esse modelo está inativa.");
    }

    /**
     * Validates if a modelo ID exists in the database.
     *
     * @param modeloId The ID of the modelo to be validated.
     * @throws IllegalArgumentException If the modelo ID does not exist in the database.
     */
    private void validarIdModelo(Long modeloId) {
        Assert.notNull(modeloId,
                "ID do modelo nao pode ser nulo.");
        Assert.isTrue(modeloRepository.existsById(modeloId),
                "Não foi possível apagar o modelo, pois oID  não foi encontrado.");
    }
}
