//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
- Essa classe é responsável por realizar validações de dados relacionados a movimentacoes.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de movimentacoes é solicitado.
*/
@Service
public class MovimentacaoService {

    // Declaração das constantes OPENING_TIME, CLOSING_TIME e CURRENT_TIME
    private static final LocalTime OPENING_TIME = LocalTime.parse("08:00:00");
    private static final LocalTime CLOSING_TIME = LocalTime.parse("18:00:00");
    private static final LocalTime CURRENT_TIME = LocalTime.now().withNano(0);

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private CondutorRepository condutorRepository;
    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    /**
     * Realiza a validação dos dados necessários para cadastrar uma movimentação de veículo no estacionamento.
     *
     * @param movimentacao a movimentação de veículo a ser validada
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        // Verifica se o veículo está ativo
        Assert.isTrue(movimentacao.getVeiculo().isAtivo(),
                "O veículo associado a essa movimentação está inativo. " +
                        "Por favor, verifique o status do veículo e tente novamente.");

        // Verifica se o objeto veículo foi informado
        Assert.notNull(movimentacao.getVeiculo(),
                "O objeto veículo não foi informado." +
                        " Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verifica se o ID do veículo existe no banco de dados
        Assert.isTrue(this.veiculoRepository.existsById(movimentacao.getVeiculo().getId()),
                "Não foi possível registrar a movimentação, " +
                        "o veículo informado não foi encontrado no sistema.");

        // Verifica se o ID do veículo foi informado
        Assert.notNull(movimentacao.getVeiculo().getId(),
                "O ID do veículo em movimentação não pode ser nulo.");

        // Verifica se o condutor está ativo
        Assert.isTrue(movimentacao.getCondutor().isAtivo(),
                "O condutor associado a essa movimentação está inativo." +
                        " Por favor, verifique o status do condutor e tente novamente.");

        // Verifica se o objeto condutor foi informado
        Assert.notNull(movimentacao.getCondutor(),
                "O objeto condutor não foi informado. " +
                        "Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verifica se o ID do condutor existe no banco de dados
        Assert.isTrue(this.condutorRepository.existsById(movimentacao.getCondutor().getId()),
                "Não foi possível registrar a movimentação," +
                        " o condutor informado não foi encontrado no sistema.");

        // Verifica se o ID do condutor foi informado
        Assert.notNull(movimentacao.getCondutor().getId(),
                "O ID do condutor em movimentação não pode ser nulo.");

        movimentacao.setEntrada(LocalDateTime.now());

        // Verifica se a movimentação está dentro do horário de funcionamento do estacionamento
        Assert.isTrue(!CURRENT_TIME.isBefore(OPENING_TIME) || CURRENT_TIME.isAfter(CLOSING_TIME),
                "Erro: Horário inválido. O horário atual está fora do intervalo de funcionamento permitido. " +
                        "Horário de funcionamento: das " + OPENING_TIME + " às " + CLOSING_TIME + ".");

        if (movimentacao.getSaida() != null) {
            LocalDateTime entrada = movimentacao.getEntrada();
            LocalDateTime saida = movimentacao.getSaida();
            Duration duracao = Duration.between(entrada.toLocalTime(), saida.toLocalTime());
            LocalTime duracaoLocalTime = duracao.toMinutes() >= 0 ? LocalTime.MIDNIGHT.plus(duracao) : LocalTime.MIDNIGHT.minus(duracao);
            movimentacao.setTempo(duracaoLocalTime);
        }else{
            movimentacao.setTempo(LocalTime.MIDNIGHT);
        }


    }

    /**
     * Método responsável por validar os dados de uma movimentação antes de sua atualização.
     *
     * @param movimentacao Movimentação a ser validada
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMovimentacao(Movimentacao movimentacao) {

        // Verifica se o ID da movimentação foi informado
        Assert.notNull(movimentacao.getId(),
                "O ID da movimentação fornecida é nulo." +
                        " Certifique-se de que o objeto de movimentação tenha " +
                        "um ID válido antes de realizar essa operação.");

        // Verifica se o veículo associado à movimentação está ativo
        Assert.isTrue(movimentacao.getVeiculo().isAtivo(),
                "O veículo associado a essa movimentação está inativo. " +
                        "Por favor, verifique o status do veículo e tente novamente.");

        // Verifica se o ID do veículo foi informado
        Assert.notNull(movimentacao.getVeiculo().getId(),
                "O ID do veículo em movimentação não pode ser nulo.");

        // Verifica se o objeto do veículo foi informado
        Assert.notNull(movimentacao.getVeiculo(),
                "O objeto veículo não foi informado." +
                        " Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verifica se o ID do veículo existe no banco de dados
        Assert.isTrue(this.veiculoRepository.existsById(movimentacao.getVeiculo().getId()),
                "Não foi possível registrar a movimentação, " +
                        "o veículo informado não foi encontrado no sistema.");

        // Verifica se o condutor associado à movimentação está ativo
        Assert.isTrue(movimentacao.getCondutor().isAtivo(),
                "O condutor associado a essa movimentação está inativo." +
                        " Por favor, verifique o status do condutor e tente novamente.");

        // Verifica se o ID do condutor foi informado
        Assert.notNull(movimentacao.getCondutor().getId(),
                "O ID do condutor em movimentação não pode ser nulo.");

        // Verifica se o objeto do condutor foi informado
        Assert.notNull(movimentacao.getCondutor(),
                "O objeto condutor não foi informado. " +
                        "Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verifica se o ID do condutor existe no banco de dados
        Assert.isTrue(this.condutorRepository.existsById(movimentacao.getCondutor().getId()),
                "Não foi possível registrar a movimentação," +
                        " o condutor informado não foi encontrado no sistema.");

        // Verifica se a data de entrada da movimentação foi informada
        Assert.notNull(movimentacao.getEntrada(), "A data de entrada da movimentação não foi informada.");

        // Verifica se a movimentação está dentro do horário de funcionamento do estacionamento
        Assert.isTrue(!CURRENT_TIME.isBefore(OPENING_TIME) || CURRENT_TIME.isAfter(CLOSING_TIME),
                "Erro: Horário inválido. O horário atual está fora do intervalo de funcionamento permitido. " +
                        "Horário de funcionamento: das " + OPENING_TIME + " às " + CLOSING_TIME + ".");

        if (movimentacao.getSaida() != null) {
            LocalDateTime entrada = movimentacao.getEntrada();
            LocalDateTime saida = movimentacao.getSaida();
            Duration duracao = Duration.between(entrada.toLocalTime(), saida.toLocalTime());
            LocalTime duracaoLocalTime = duracao.toMinutes() >= 0 ? LocalTime.MIDNIGHT.plus(duracao) : LocalTime.MIDNIGHT.minus(duracao);
            movimentacao.setTempo(duracaoLocalTime);
        }

    }

    /**
     * Verifica se o ID da movimentação existe no banco de dados e, se existir, permite que ela seja excluída.
     *
     * @param id o ID da movimentação a ser excluída
     * @throws IllegalArgumentException se o ID da movimentação não existir no banco de dados
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteMovimentacao(Long id) {

        // Verificar se o ID do movimentacao existe no banco de dados
        Assert.isTrue(movimentacaoRepository.existsById(id),
                "O ID da movimentação especificada não foi encontrada na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }

}
