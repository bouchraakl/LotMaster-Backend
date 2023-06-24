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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        // Validate plate format
        String brazilFormat = "^[A-Z]{3}-\\d{4}$"; // Format ABC-1234
        String paraguayFormat = "^[A-Z]{3}\\s\\d{4}$"; // Format ABC 1234
        String argentinaFormat = "^[A-Z]{3}\\s\\d{3}$"; // Format ABC 123

        Pattern brazilPattern = Pattern.compile(brazilFormat);
        Pattern paraguayPattern = Pattern.compile(paraguayFormat);
        Pattern argentinaPattern = Pattern.compile(argentinaFormat);

        Matcher brazilMatcher = brazilPattern.matcher(veiculo.getPlaca());
        Matcher paraguayMatcher = paraguayPattern.matcher(veiculo.getPlaca());
        Matcher argentinaMatcher = argentinaPattern.matcher(veiculo.getPlaca());

        Assert.isTrue(brazilMatcher.matches() || paraguayMatcher.matches() || argentinaMatcher.matches(),
                "The plate format " + veiculo.getPlaca() + " is invalid. The expected format is ABC-1234 for Brazil, ABC 1234 for Paraguay, or ABC 123 for Argentina.");

        final Veiculo veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculoByPlaca == null,
                "There is already a registered vehicle with the license plate " + veiculo.getPlaca() +
                ". Please check if the data is correct and try again.");
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
                Assert.isTrue(!veiculoByPlacaa.isPresent(), "There is already a registered vehicle with the license plate " +
                        veiculo.getPlaca() +
                        ". Please check if the data is correct and try again.");
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
    public void validarDeleteVeiculo(Long id) {


        final Veiculo veiculo = this.veiculoRepository.findById(id).orElse(null);
        Assert.notNull(veiculo, "Vehicle not registered !");

        if (!this.movimentacaoRepository.findByVeiculoId(id).isEmpty()) {
            veiculo.setAtivo(false);
            this.veiculoRepository.save(veiculo);
        } else {
            this.veiculoRepository.delete(veiculo);
        }
    }

    public Page<Veiculo> listAll(Pageable pageable) {
        return this.veiculoRepository.findAll(pageable);
    }


}
