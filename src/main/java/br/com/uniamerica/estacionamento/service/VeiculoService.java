package br.com.uniamerica.estacionamento.service;


import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;


/*
- Essa classe é responsável por realizar validações de dados relacionados a veiculos.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de veiculos é solicitado.
*/
@Service
public class VeiculoService {

    private static final int MIN_ALLOWED_YEAR = 2008;
    int currentYear = Year.now().getValue();

    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Realiza validações para cadastrar um novo veículo no sistema.
     *
     * @param veiculo o objeto Veiculo a ser validado.
     * @throws IllegalArgumentException se alguma das validações não passar.
     */
    @Transactional
    public void validarCadastroVeiculo(Veiculo veiculo) {

        veiculo.setCadastro(LocalDateTime.now());
        final Veiculo veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculoByPlaca == null,
                "Já existe um veículo cadastrado com a placa " + veiculo.getPlaca() +
                        ". Verifique se os dados estão corretos e tente novamente.");
        this.veiculoRepository.save(veiculo);

    }

    /**
     * Valida os dados de um objeto Veiculo antes de atualizá-lo no banco de dados.
     * A transação é somente para leitura e será revertida em caso de exceção.
     *
     * @param veiculo o objeto Veiculo a ser validado
     */

    @Transactional
    public void validarUpdateVeiculo(Veiculo veiculo) {
        veiculo.setAtualizacao(LocalDateTime.now());

        final Veiculo veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Optional<Veiculo> veiculoAtualOptional = veiculoRepository.findById(veiculo.getId());
        if (veiculoAtualOptional.isPresent()) {
            Veiculo veiculoAtual = veiculoAtualOptional.get();
            if (!veiculoAtual.getPlaca().equals(veiculo.getPlaca())) {
                Optional<Veiculo> veiculoByPlacaa = Optional.ofNullable(veiculoRepository.findByPlaca(veiculo.getPlaca()));
                Assert.isTrue(!veiculoByPlacaa.isPresent(), "Já existe um veículo cadastrado com a placa "
                        + veiculo.getPlaca() +
                        ". Verifique se os dados estão corretos e tente novamente.");
            }
        }


        veiculoRepository.save(veiculo);
    }

            /**
             * Valida se um Veiculo com o ID fornecido existe no banco de dados antes de permitir sua exclusão.
             * A transação é somente para leitura e será revertida em caso de exceção.
             *
             * @param id o ID do Veiculo a ser excluído
             * @throws IllegalArgumentException se o ID do Veiculo não existir no banco de dados
             */
    @Transactional
    public void validarDeleteVeiculo(Long id){

        /*
         * Verifica se o Veiculo informado existe
         * */
        final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);
        Assert.notNull(veiculoBanco, "Veiculo não encontrado!");

        /*
         * Verifica se o Veiculo informado está relacionado a uma Movimentação,
         * True: Desativa o cadastro
         * False: Faz o DELETE do registro
         * */
        if(!this.movimentacaoRepository.findByVeiculoId(id).isEmpty()){
            veiculoBanco.setAtivo(false);
            this.veiculoRepository.save(veiculoBanco);
        }else{
            this.veiculoRepository.delete(veiculoBanco);
        }
    }

    public Page<Veiculo> listAll(Pageable pageable) {
        return this.veiculoRepository.findAll(pageable);
    }








}
