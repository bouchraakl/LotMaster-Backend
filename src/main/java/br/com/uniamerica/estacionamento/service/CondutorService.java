/**
 * The CondutorService class provides methods for managing Condutor entities.
 * It applies clean code principles and follows professional coding standards.
 */
package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CondutorService {

    private final CondutorRepository condutorRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    @Autowired
    public CondutorService(CondutorRepository condutorRepository, MovimentacaoRepository movimentacaoRepository) {
        this.condutorRepository = condutorRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    /**
     * Validates the registration of a Condutor.
     *
     * @param condutor The Condutor to be validated and saved.
     * @throws IllegalArgumentException If a Condutor with the same CPF already exists.
     */
    @Transactional
    public void validarCadastroCondutor(Condutor condutor) {
        condutor.setCadastro(LocalDateTime.now());

        final Condutor condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isNull(condutorbyCPF,"CPF already exists");

        condutorRepository.save(condutor);
    }

    /**
     * Validates the update of a Condutor.
     *
     * @param condutor The Condutor to be validated and updated.
     * @throws IllegalArgumentException If the provided Condutor ID is null or not found in the database,
     *                                  or if a Condutor with the same CPF already exists.
     */
    @Transactional
    public void validarUpdateCondutor(Condutor condutor) {
        condutor.setAtualizacao(LocalDateTime.now());

        condutorRepository.save(condutor);
    }

    /**
     * Validates the deletion of a Condutor.
     *
     * @param id The ID of the Condutor to be validated and deleted.
     * @throws IllegalArgumentException If the provided Condutor ID is not found in the database.
     */
    @Transactional
    public void validarDeleteCondutor(Long id){

        /*
         * Verifica se o Condutor informado existe
         * */
        final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);
        Assert.notNull(condutorBanco, "Condutor não encontrado!");

        if(!this.movimentacaoRepository.findByVeiculoId(id).isEmpty()){
            condutorBanco.setAtivo(false);
            this.condutorRepository.save(condutorBanco);
        }else{
            this.condutorRepository.delete(condutorBanco);
        }
    }

    public Page<Condutor> listAll(Pageable pageable) {
        return this.condutorRepository.findAll(pageable);
    }
}
