//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/*
- Essa classe é responsável por realizar validações de dados relacionados a movimentacoes.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de movimentacoes é solicitado.
*/
@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private CondutorRepository condutorRepository;
    @Autowired
    private ConfiguracaoRepository configuracaoRepository;


    private Configuracao obterConfiguracao() {
        Configuracao configuracao = configuracaoRepository.ultimaConfiguracao();
        if (configuracao == null) {
            throw new EntityNotFoundException("Erro, as configuracoes nao foram definidas.");
        }
        return configuracao;
    }

    /**
     * Realiza a validação dos dados necessários para cadastrar uma movimentação de veículo no estacionamento.
     *
     * @param movimentacao a movimentação de veículo a ser validada
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        Configuracao configuracao = obterConfiguracao();

        BigDecimal valorMinutoMulta = configuracao.getValorMinutoMulta();
        movimentacao.setValorHoraMulta(valorMinutoMulta.multiply(new BigDecimal("60.0")));

        movimentacao.setCadastro(LocalDateTime.now());
        validarMovimentacao(movimentacao);

        if (movimentacao.getSaida() != null) {

            Assert.isTrue(movimentacao.getEntrada().isBefore(movimentacao.getSaida()),
                    "O tempo de entrada deve ser posterior ao tempo de saída.");

            saidaOperations(movimentacao);

        } else {
            configurarValoresPadrao(movimentacao);
        }

        movimentacao.setValorHora(configuracao.getValorHora());
    }

    /**
     * Método responsável por validar os dados de uma movimentação antes de sua atualização.
     *
     * @param movimentacao Movimentação a ser validada
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMovimentacao(Movimentacao movimentacao) {

        movimentacao.setAtualizacao(LocalDateTime.now());

        Assert.notNull(movimentacao.getId(), "O ID da movimentação fornecida é nulo.");

        Assert.isTrue(movimentacaoRepository.existsById(movimentacao.getId()),
                "O ID da movimentação especificada não foi encontrada na base de dados.");

        validarVeiculo(movimentacao.getVeiculo());
        validarCondutor(movimentacao.getCondutor());
        Assert.notNull(movimentacao.getEntrada(), "A data de entrada da movimentação não foi informada.");

        if (movimentacao.getSaida() != null) {

            Assert.isTrue(movimentacao.getEntrada().isBefore(movimentacao.getSaida()),
                    "O tempo de entrada deve ser posterior ao tempo de saída.");

            saidaOperations(movimentacao);

        } else {
            configurarValoresPadrao(movimentacao);
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

    private void validarMovimentacao(Movimentacao movimentacao) {

        // Validar veículo e condutor
        validarVeiculo(movimentacao.getVeiculo());
        validarCondutor(movimentacao.getCondutor());

        // Garantir que a entrada não seja nula
        Assert.notNull(movimentacao.getEntrada(), "A data de entrada da movimentação não foi informada.");

        // Verificar se o horário atual está dentro do horário de funcionamento permitido
        Assert.isTrue(!LocalTime.from(movimentacao.getEntrada())
                        .isBefore(obterConfiguracao().getInicioExpediente()),
                "Erro: Horário inválido. O horário atual está fora do intervalo de funcionamento permitido. "
                        + "Horário de funcionamento: das "
                        + obterConfiguracao().getInicioExpediente() + " às "
                        + obterConfiguracao().getFimExpediente() + ".");

    }

    private void validarCondutor(Condutor condutor) {

        // Garantir que o condutor não seja nulo
        Assert.notNull(condutor, "O objeto condutor não foi informado.");

        // Garantir que o condutor esteja ativo
        Assert.isTrue(condutor.isAtivo(), "O condutor associado a essa movimentação está inativo.");

        // Garantir que o ID do condutor não seja nulo
        Assert.notNull(condutor.getId(), "O ID do condutor em movimentação não pode ser nulo.");

        // Garantir que o condutor exista no repositório
        Assert.isTrue(condutorRepository.existsById(condutor.getId()),
                "Não foi possível registrar a movimentação, " +
                        "o condutor informado não foi encontrado no sistema.");

    }

    private void validarVeiculo(Veiculo veiculo) {

        // Garantir que o veículo não seja nulo
        Assert.notNull(veiculo, "O objeto veículo não foi informado.");

        // Garantir que o veículo esteja ativo
        Assert.isTrue(veiculo.isAtivo(), "O veículo associado a essa movimentação está inativo.");

        // Garantir que o ID do veículo não seja nulo
        Assert.notNull(veiculo.getId(), "O ID do veículo em movimentação não pode ser nulo.");

        // Garantir que o veículo exista no repositório
        Assert.isTrue(veiculoRepository.existsById(veiculo.getId()),
                "Não foi possível registrar a movimentação, " +
                        "o veículo informado não foi encontrado no sistema.");

    }

    private void saidaOperations(Movimentacao movimentacao) {

        Configuracao configuracao = obterConfiguracao();

        // Defenindo horário de funcionamento do estacionamento
        LocalTime OPENING_TIME = configuracao.getInicioExpediente();
        LocalTime CLOSING_TIME = configuracao.getFimExpediente();

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();
        BigDecimal valorMinutoMulta = configuracao.getValorMinutoMulta();

        // Calcula a duração entre a entrada e a saída
        Duration duracao = Duration.between(entrada,saida);

        long totalSecoundsOfDuration = duracao.getSeconds();
        long hours = totalSecoundsOfDuration / 3600;
        long minutes = (totalSecoundsOfDuration % 3600) / 60;

        // Define as horas e minutos totais da movimentação
        movimentacao.setTempoHoras((int) hours);
        movimentacao.setTempoMinutos((int) minutes);

        // Calcular tempoMulta e valorTempoMulta
        calculateMulta(movimentacao,entrada,saida,OPENING_TIME,CLOSING_TIME);

        // Setting tempos pagos e de desconto para o condutor associado
        valoresCondutor(movimentacao,movimentacao.getTempoHoras(),movimentacao.getTempoMinutos());

    }

    private void valoresCondutor(Movimentacao movimentacao, int tempoHoras, int tempoMinutos) {

        Condutor condutor = condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        assert condutor != null;

        // Set tempoPagoHoras e tempoPagoMinutos em condutor

        int hoursToAdd = movimentacao.getTempoHoras();
        int minutesToAdd = movimentacao.getTempoMinutos() % 60;

        condutor.setTempoPagoHoras(condutor.getTempoPagoHoras() + hoursToAdd);
        condutor.setTempoPagoMinutos(condutor.getTempoPagoMinutos() + minutesToAdd);

        condutor.setTempoPagoHoras(condutor.getTempoPagoHoras() + condutor.getTempoPagoMinutos() / 60);
        condutor.setTempoPagoMinutos(condutor.getTempoPagoMinutos() % 60);

        // Verifica se os minutos pagos ultrapassaram 60 minutos
        int extraHours = condutor.getTempoPagoMinutos() / 60;
        condutor.setTempoPagoMinutos(condutor.getTempoPagoMinutos() % 60);
        condutor.setTempoPagoHoras(condutor.getTempoPagoHoras() + extraHours);

        // Management for all descount operations
        manageDesconto(movimentacao,condutor.getTempoPagoHoras(),condutor.getTempoPagoMinutos());

    }

    private void calculateMulta(Movimentacao movimentacao,
                                LocalDateTime entrada,
                                LocalDateTime saida,
                                LocalTime openingTime,
                                LocalTime closingTime) {


        long totalMinutes = Duration.between(entrada,saida).toMinutes();

        long openHoursinMinutes = Duration.between(openingTime,closingTime).toMinutes();

        int multaMinutes = 0;

        if (LocalTime.from(saida).isAfter(closingTime)){
            multaMinutes += Duration.between(closingTime,saida).toMinutes();
        }

        int diasEstacionados = Math.toIntExact(Duration.between(LocalDate.from(entrada).atTime(openingTime)
                        ,LocalDate.from(saida).atTime(closingTime))
                .toDays()) +1;

        multaMinutes += (diasEstacionados * openHoursinMinutes) - totalMinutes;

        int hours = multaMinutes /60;
        int minutes = multaMinutes % 60;

        LocalTime tempoMulta = LocalTime.of(hours, minutes);
        movimentacao.setTempoMulta(tempoMulta);

        movimentacao.setValorMulta(BigDecimal.valueOf(tempoMulta.getHour())
                .multiply(movimentacao.getValorHoraMulta()));

    }

    private void manageDesconto(Movimentacao movimentacao, int tempoPagoHoras, int tempoPagoMinutos) {



    }

    private void configurarValoresPadrao(Movimentacao movimentacao) {

        // Configurar valores padrão para a movimentação
        movimentacao.setTempoMulta(LocalTime.MIDNIGHT);
        movimentacao.setTempoHoras(0);
        movimentacao.setTempoMinutos(0);
        movimentacao.setValorDesconto(BigDecimal.ZERO);
        movimentacao.setValorMulta(BigDecimal.ZERO);

    }


}