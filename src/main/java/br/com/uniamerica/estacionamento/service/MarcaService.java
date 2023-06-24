package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Veiculo;
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

        Marca marcasByNome = marcaRepository.findByNome(marca.getNome());
        Assert.isNull(marcasByNome,
                "A brand is already registered with the provided name.");

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

        final Marca marca = this.marcaRepository.findById(id).orElse(null);
        Assert.notNull(marca, "Marca não encontrado!");

        if(!this.modeloRepository.findByMarcaId(id).isEmpty()){
            marca.setAtivo(false);
            this.marcaRepository.save(marca);
        }else{
            this.marcaRepository.delete(marca);
        }
    }

    public Page<Marca> listAll(Pageable pageable) {
        return this.marcaRepository.findAll(pageable);
    }
}
