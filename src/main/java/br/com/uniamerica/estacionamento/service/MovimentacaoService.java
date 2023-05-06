//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.w3c.dom.ranges.Range;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

//------------------------------------------------
@Service
public class MovimentacaoService {
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private CondutorService condutorService;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        // Verificar se a ID do veículo já existe
        if (!movimentacaoRepository.existsById(movimentacao.getId())) {
            throw new IllegalArgumentException("A ID do veículo não existe no banco de dados: " + movimentacao.getId());
        }

        // Executar validações no objeto do veículo
        veiculoService.validarCadastroVeiculo(movimentacao.getVeiculo());

        // Executar validações no objeto do condutor
        condutorService.validarCadastroCondutor(movimentacao.getCondutor());

        // Verificar se a movimentação está dentro do horário de funcionamento do estacionamento
        LocalTime inicioExpediente = LocalTime.parse("08:00:00");
        LocalTime fimExpediente = LocalTime.parse("18:00:00");
        LocalTime horarioMovimentacao = LocalTime.now().withNano(0);
        if (horarioMovimentacao.isBefore(inicioExpediente) || horarioMovimentacao.isAfter(fimExpediente)) {
            throw new IllegalArgumentException("A movimentação está fora do horário de funcionamento do estacionamento.");
        }

        // Verificar se há vagas disponíveis para o tipo de veículo informado
        long numeroVagas = configuracaoRepository.countByTipo(movimentacao.getVeiculo().getTipo());
        if (numeroVagas == 0) {
            throw new IllegalArgumentException("Não há vagas disponíveis para o tipo de veículo informado.");
        }
    }


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMovimentacao(Movimentacao movimentacao) {

        // Verificar se o ID da movimentacao não é nula e valido
        Assert.notNull(movimentacao.getId(), "Movimentação não cadastrada no banco de dados.");

        // Verificar se o ID do veiculo não é nulo e valido
        Assert.notNull(movimentacao.getVeiculo().getId(), "Veículo não cadastrado no banco de dados.");

        // Verificar se o ID do condutor não é nulo e valido
        Assert.notNull(movimentacao.getCondutor().getId(), "Condutor não cadastrado no banco de dados.");

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(movimentacao.getEntrada(), "Data de entrada não informada.");
        Assert.notNull(movimentacao.getSaida(), "Data de entrada não informada.");
        Assert.notNull(movimentacao.getVeiculo(), "Veículo não informado.");
        Assert.notNull(movimentacao.getCondutor(), "Condutor não informado.");
    }

    public void validarPlaca(String placa) {
        final String PADRAO_REGEX_PLACA_BRASILEIRA = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(placa.matches(PADRAO_REGEX_PLACA_BRASILEIRA), "Placa inválida.");
    }
}
