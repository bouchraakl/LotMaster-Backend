package br.com.uniamerica.estacionamento.service;


import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
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

        // Verifica se a placa já existe no banco de dados
        final Veiculo veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculoByPlaca == null,
                "Já existe um veículo cadastrado com a placa " + veiculo.getPlaca() +
                        ". Verifique se os dados estão corretos e tente novamente.");


        // Verifica se a placa está no formato correto (três letras maiúsculas seguidas de quatro números)
        final String placaFormat = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(veiculo.getPlaca().matches(placaFormat),
                "A placa do veículo deve seguir o formato AAA9999" +
                        ". Verifique a placa informada e tente novamente.");

        // Verificar se o modelo com o ID informado existe no banco de dados
        Assert.isTrue(this.modeloRepository.existsById(veiculo.getModelo().getId()),
                "Modelo não existe no banco de dados");

        final List<Modelo> isActive = modeloRepository.findActiveElement(veiculo.getModelo().getId());
        Assert.isTrue(!isActive.isEmpty(), "A modelo associado a esse veiculo está inativo.");

        // Verifica se o ID do modelo foi informado
        Assert.notNull(veiculo.getModelo().getId(), "O ID do modelo em veiculo não pode ser nulo.");


        // Define o range permitido para o ano do veículo
        Range<Integer> rangeAno = Range.closed(MIN_ALLOWED_YEAR, currentYear);

        // Verifica se o ano do veículo está dentro do range permitido
        Assert.isTrue(rangeAno.contains(veiculo.getAno()),
                "O ano do veículo deve estar dentro do intervalo permitido " +
                        "(" + MIN_ALLOWED_YEAR + "-" + currentYear + "), " +
                        "mas o valor fornecido foi " + veiculo.getAno() + ".");

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

        // Verificar se o ID do veiculo não é nulo
        Assert.notNull(veiculo.getId(),
                "O ID do veiculo fornecido é nulo. " +
                        "Certifique-se de que o objeto do veiculo tenha um ID válido antes de realizar essa operação.");

        // Verifica se ID do veiculo existe no banco de dados
        Assert.isTrue(veiculoRepository.existsById(veiculo.getId()),
                "O ID do veiculo especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");


        // Verifica se a placa está no formato correto (três letras maiúsculas seguidas de quatro números)
        final String placaFormat = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(veiculo.getPlaca().matches(placaFormat),
                "A placa do veículo deve seguir o formato AAA9999" +
                        ". Verifique a placa informada e tente novamente.");

        // Verificar se o ID do modelo do veículo foi informado
        Assert.notNull(veiculo.getModelo().getId(), "O ID do modelo em veiculo não pode ser nulo.");

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


        // Verificar se o modelo com o ID informado existe no banco de dados
        Assert.isTrue(this.modeloRepository.existsById(veiculo.getModelo().getId()),
                "Modelo não existe no banco de dados");

        // Verificar se o modelo do veículo está ativo
        final List<Modelo> isActive = modeloRepository.findActiveElement(veiculo.getModelo().getId());
        Assert.isTrue(!isActive.isEmpty(), "A modelo associado a esse veiculo está inativo.");

        // Define o range permitido para o ano do veículo
        Range<Integer> rangeAno = Range.closed(MIN_ALLOWED_YEAR, currentYear);

        // Verifica se o ano do veículo está dentro do range permitido
        Assert.isTrue(rangeAno.contains(veiculo.getAno()),
                "O ano do veículo deve estar dentro do intervalo permitido " +
                        "(" + MIN_ALLOWED_YEAR + "-" + currentYear + "), " +
                        "mas o valor fornecido foi " + veiculo.getAno() + ".");

        this.veiculoRepository.save(veiculo);

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
