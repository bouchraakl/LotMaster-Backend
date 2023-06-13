package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public MarcaRepository marcaRepository;
    @Autowired
    public ModeloRepository modeloRepository;


    /**
     * Validates the registration of a new brand.
     *
     * @param marca The brand object to be validated.
     * @throws IllegalArgumentException if the brand name already exists in the database or if it is not provided.
     */
    @Transactional
    public void validarCadastroMarca(Marca marca) {

        marca.setCadastro(LocalDateTime.now());

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

        marca.setAtualizacao(LocalDateTime.now());

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
    public void validarDeleteMarca(Long id){
        /*
         * Verifica se a Marca informado existe
         * */
        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
        Assert.notNull(marcaBanco, "Marca não encontrada!");

        this.marcaRepository.delete(marcaBanco);
    }

    public Page<Marca> listAll(Pageable pageable) {
        return this.marcaRepository.findAll(pageable);
    }
}
