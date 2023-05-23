package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for performing validations related to car brands (marcas).
 */
@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    /**
     * Validates the registration of a new brand.
     *
     * @param marca The brand object to be validated.
     * @throws IllegalArgumentException if the brand name already exists in the database or if it is not provided.
     */
    @Transactional
    public void validarCadastroMarca(Marca marca) {
        setTimestamps(marca);

        List<Marca> marcaExistente = marcaRepository.findByNome(marca.getNome());
        Assert.isTrue(marcaExistente.isEmpty(),
                "Já existe uma marca registrada com o nome informado. " +
                        "Verifique os dados informados e tente novamente.");

        marcaRepository.save(marca);
    }

    /**
     * Validates the update of an existing brand.
     *
     * @param marca The brand object to be validated.
     * @throws IllegalArgumentException if the brand ID is not provided or if the brand name already exists in the database.
     */
    @Transactional
    public void validarUpdateMarca(Marca marca) {
        setTimestamps(marca);

        Assert.notNull(marca.getId(),
                "O ID da marca fornecido é nulo. " +
                        "Certifique-se de que o objeto da marca tenha um ID válido antes de realizar essa operação.");

        Assert.isTrue(marcaRepository.existsById(marca.getId()),
                "O ID da marca especificada não foi encontrado na base de dados. " +
                        "Verifique se o ID está correto e tente novamente.");

        marcaRepository.save(marca);
    }

    /**
     * Validates the deletion of an existing brand.
     *
     * @param id The ID of the brand to be validated.
     * @throws IllegalArgumentException if the ID does not exist in the database.
     */
    @Transactional
    public void validarDeleteMarca(Long id) {
        Assert.isTrue(marcaRepository.existsById(id),
                "O ID da marca especificada não foi encontrada na base de dados. " +
                        "Verifique se o ID está correto e tente novamente.");
    }

    private void setTimestamps(Marca marca) {
        LocalDateTime now = LocalDateTime.now();
        marca.setCadastro(now);
        marca.setAtualizacao(now);
    }
}
