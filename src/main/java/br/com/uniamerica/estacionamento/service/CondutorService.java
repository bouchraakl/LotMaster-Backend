/**
 * The CondutorService class provides methods for managing Condutor entities.
 * It applies clean code principles and follows professional coding standards.
 */
package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
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

        Assert.isNull(condutorRepository.findbyCPF(condutor.getCpf()),
                "Um condutor já está registrado com o CPF informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

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

        Assert.notNull(condutor.getId(), "O ID do condutor fornecido é nulo. " +
                "Certifique-se de que o objeto do condutor tenha um ID válido antes de realizar essa operação.");

        Assert.isTrue(condutorRepository.existsById(condutor.getId()),
                "O ID do condutor especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

        Optional<Condutor> condutorAtualOptional = condutorRepository.findById(condutor.getId());
        if (condutorAtualOptional.isPresent()) {
            Condutor condutorAtual = condutorAtualOptional.get();
            if (!condutorAtual.getCpf().equals(condutor.getCpf())) {
                Assert.isTrue(condutorRepository.findbyCPF(condutor.getCpf()) == null,
                        "Um condutor já está registrado com o CPF informado. " +
                                "Por favor, verifique os dados informados e tente novamente.");
            }
        }

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
}
