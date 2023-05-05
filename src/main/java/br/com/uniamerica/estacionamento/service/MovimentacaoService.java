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

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        // Verificar se a movimentação já existe
        Assert.notNull(movimentacao.getId(), "Movimentação não cadastrada no banco de dados.");

        // Verificar se a placa do veículo informado é válido
        this.veiculoService.validarPlaca(movimentacao.getVeiculo().getPlaca());

        // Verificar se o CPF do cliente informado está cadastrado no sistema é valido
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(movimentacao.getCondutor().getCpf().matches(cpfFormat), "CPF inválido!");

        // Verificar se a movimentação está dentro do horário de funcionamento do estacionamento
        LocalTime inicioExpediente = LocalTime.parse("08:00:00");
        LocalTime fimExpediente = LocalTime.parse("18:00:00");

        LocalDateTime entrada = LocalDateTime.now();
        LocalTime horarioMovimentacao = entrada.toLocalTime();
        Assert.isTrue(horarioMovimentacao.isAfter(inicioExpediente)
                        && horarioMovimentacao.isBefore(fimExpediente),
                "Movimentação fora do horário de funcionamento do estacionamento.");

        // Verificar se há vagas disponíveis para o tipo de veículo informado
        int numeroVagas = this.configuracaoRepository.countByTipo(movimentacao.getVeiculo().getTipo());
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
}
