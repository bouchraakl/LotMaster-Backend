//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Year;
import java.util.List;

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

    /**
     * Realiza validações para cadastrar um novo veículo no sistema.
     *
     * @param veiculo o objeto Veiculo a ser validado.
     * @throws IllegalArgumentException se alguma das validações não passar.
     */
    @Transactional
    public void validarCadastroVeiculo(Veiculo veiculo) {

        // Verifica se a placa já existe no banco de dados
        final List<Veiculo> veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculoByPlaca.isEmpty(),
                "Já existe um veículo cadastrado com a placa " + veiculo.getPlaca() +
                        ". Verifique se os dados estão corretos e tente novamente.");


        // Verifica se a placa está no formato correto (três letras maiúsculas seguidas de quatro números)
        final String placaFormat = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(veiculo.getPlaca().matches(placaFormat),
                "A placa do veículo deve seguir o formato AAA9999" +
                        ". Verifique a placa informada e tente novamente.");

        final List<Modelo> isActive = modeloRepository.findActiveElement(veiculo.getModelo().getId());
        Assert.isTrue(!isActive.isEmpty(), "A modelo associado a esse veiculo está inativo.");

        // Verifica se o ID do modelo foi informado
        Assert.notNull(veiculo.getModelo().getId(), "O ID do modelo em veiculo não pode ser nulo.");

        // Verificar se o modelo com o ID informado existe no banco de dados
        Assert.isTrue(this.modeloRepository.existsById(veiculo.getModelo().getId()),
                "Modelo não existe no banco de dados");

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
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateVeiculo(Veiculo veiculo) {


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
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteVeiculo(Long id) {

        // Verificar se o ID do veiculo existe no banco de dados
        Assert.isTrue(veiculoRepository.existsById(id),
                "O ID do veiculo especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }


}
