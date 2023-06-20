//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


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
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;


    private Configuracao obterConfiguracao() {
        Configuracao configuracao = configuracaoRepository.ultimaConfiguracao();
        if (configuracao == null) {
            throw new EntityNotFoundException("Error, the configurations were not defined.");
        }
        return configuracao;
    }

    /**
     * Realiza a validação dos dados necessários para cadastrar uma movimentação de veículo no estacionamento.
     *
     * @param movimentacao a movimentação de veículo a ser validada
     */
    @Transactional
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        movimentacao.setCadastro(LocalDateTime.now());
        Configuracao configuracao = obterConfiguracao();

        BigDecimal valorMinutoMulta = configuracao.getValorMinutoMulta();
        System.out.println(valorMinutoMulta);
        movimentacao.setValorHoraMulta(valorMinutoMulta.multiply(new BigDecimal("60.0")));
        movimentacao.setValorHora(configuracao.getValorHora());

        validarMovimentacao(movimentacao);
        verificarVagasDisponiveis(movimentacao);

        if (movimentacao.getSaida() != null) {
//            Assert.isTrue(movimentacao.getEntrada().isBefore(movimentacao.getSaida()),
//                    "The entry time must be before the exit time.");

            saidaOperations(movimentacao);
            emitirRelatorio(movimentacao);
        } else {
            configurarValoresPadrao(movimentacao);
        }

        this.movimentacaoRepository.save(movimentacao);
    }

    /**
     * Método responsável por validar os dados de uma movimentação antes de sua atualização.
     *
     * @param movimentacao Movimentação a ser validada
     */
    @Transactional
    public void validarUpdateMovimentacao(Movimentacao movimentacao) {

        Configuracao configuracao = obterConfiguracao();

        BigDecimal valorMinutoMulta = configuracao.getValorMinutoMulta();
        movimentacao.setValorHoraMulta(valorMinutoMulta.multiply(new BigDecimal("60.0")));
        movimentacao.setValorHora(configuracao.getValorHora());

        movimentacao.setAtualizacao(LocalDateTime.now());
        Assert.notNull(movimentacao.getId(), "The ID of the provided movement is null.");

        Assert.isTrue(movimentacaoRepository.existsById(movimentacao.getId()),
                "The specified movement ID was not found in the database.");

        verificarVagasDisponiveis(movimentacao);
        validarMovimentacao(movimentacao);


        if (movimentacao.getSaida() != null) {
            Assert.isTrue(movimentacao.getEntrada().isBefore(movimentacao.getSaida()),
                    "The entry time must be before the exit time.");
            saidaOperations(movimentacao);
            emitirRelatorio(movimentacao);

        } else {
            configurarValoresPadrao(movimentacao);
        }

        this.movimentacaoRepository.save(movimentacao);
    }

    /**
     * Verifica se o ID da movimentação existe no banco de dados e, se existir, permite que ela seja excluída.
     *
     * @param id o ID da movimentação a ser excluída
     * @throws IllegalArgumentException se o ID da movimentação não existir no banco de dados
     */
    @Transactional
    public void validarDeleteMovimentacao(Long id){
        /*
         * Verifica se a Movimentação informada existe
         * */
        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.notNull(movimentacao, "Movimentação não encontrada!");

        movimentacaoRepository.delete(movimentacao);
    }

    private void validarMovimentacao(Movimentacao movimentacao) {
        // Validar veículo e condutor
        validarVeiculo(movimentacao.getVeiculo());
        validarCondutor(movimentacao.getCondutor());

        LocalTime entrada = LocalTime.from(movimentacao.getEntrada());
        LocalTime inicioExpediente = obterConfiguracao().getInicioExpediente();
        LocalTime fimExpediente = obterConfiguracao().getFimExpediente();

    }

    private void validarCondutor(Condutor condutor) {

        // Garantir que o condutor esteja ativo
        final List<Condutor> isActive = condutorRepository.findActiveElement(condutor.getId());
        Assert.isTrue(!isActive.isEmpty(), "The driver associated with this movement is inactive.");


        // Garantir que o condutor exista no repositório
        Assert.isTrue(condutorRepository.existsById(condutor.getId()),
                "Unable to register the movement, the specified driver was not found in the system.");

    }

    private void validarVeiculo(Veiculo veiculo) {

        final List<Veiculo> isActive = veiculoRepository.findActiveElement(veiculo.getId());
        Assert.isTrue(!isActive.isEmpty(), "The vehicle associated with this movement is inactive.");

        // Garantir que o veículo exista no repositório
        Assert.isTrue(veiculoRepository.existsById(veiculo.getId()),
                "Unable to register the movement, the specified vehicle was not found in the system.");

    }

    private void saidaOperations(Movimentacao movimentacao) {

        Configuracao configuracao = obterConfiguracao();

        // Definindo horário de funcionamento do estacionamento
        LocalTime OPENING_TIME = configuracao.getInicioExpediente();
        LocalTime CLOSING_TIME = configuracao.getFimExpediente();

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        BigDecimal valorMinutoMulta = configuracao.getValorMinutoMulta();

        // Calcula a duração entre a entrada e a saída
        Duration duracao = Duration.between(entrada, saida);
        System.out.println("duracao - saidaOperations : " + duracao);

        long totalSecoundsOfDuration = duracao.getSeconds();
        long hours = totalSecoundsOfDuration / 3600;
        long minutes = (totalSecoundsOfDuration % 3600) / 60;

        // Define as horas e minutos totais da movimentação
        movimentacao.setTempoHoras((int) hours);
        movimentacao.setTempoMinutos((int) minutes);

        // Calcular tempoMulta e valorTempoMulta
        calculateMulta(movimentacao, entrada, saida, OPENING_TIME, CLOSING_TIME);

        // Configurar tempos pagos e de desconto para o condutor associado
        valoresCondutor(movimentacao);

        // Gerenciar todas as operações de desconto
        manageDesconto(movimentacao);

        BigDecimal valorHorasEstacionadas = (new BigDecimal(movimentacao.getTempoHoras())
                .multiply(movimentacao.getValorHora()))
                .add(new BigDecimal(movimentacao.getTempoMinutos())
                        .multiply(movimentacao.getValorHora()
                                .divide(new BigDecimal(60), RoundingMode.HALF_UP)));

        BigDecimal valorTotal = (valorHorasEstacionadas.add(movimentacao.getValorMulta()))
                .subtract(movimentacao.getValorDesconto());
        movimentacao.setValorTotal(valorTotal);

    }

    private void valoresCondutor(Movimentacao movimentacao) {
        // Obter o condutor da movimentação
        Condutor condutor = condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        assert condutor != null;

        // Adicionar horas e minutos pagos ao condutor
        int hoursToAdd = movimentacao.getTempoHoras();
        int minutesToAdd = movimentacao.getTempoMinutos();

        condutor.setTempoPagoHoras(condutor.getTempoPagoHoras() + hoursToAdd);
        condutor.setTempoPagoMinutos(condutor.getTempoPagoMinutos() + minutesToAdd);

        // Verificar se os minutos pagos ultrapassaram 60 minutos
        int extraHours = condutor.getTempoPagoMinutos() / 60;
        condutor.setTempoPagoMinutos(condutor.getTempoPagoMinutos() % 60);
        condutor.setTempoPagoHoras(condutor.getTempoPagoHoras() + extraHours);

    }

    private void calculateMulta(Movimentacao movimentacao,
                                LocalDateTime entrada,
                                LocalDateTime saida,
                                LocalTime inicioExpediente,
                                LocalTime fimExpediente) {

        int tempoMultaMinutos = 0;

        int anos = saida.getYear() - entrada.getYear();
        int dias = saida.getDayOfYear() - entrada.getDayOfYear();

        if (anos > 0) {
            dias += anos * 365;
        }

        // Verificar se a entrada ocorreu antes do horário de início do expediente
        if (entrada.toLocalTime().isBefore(inicioExpediente)) {
            tempoMultaMinutos += Duration.between(entrada.toLocalTime(), inicioExpediente).toMinutes();
        }

        // Verificar se a saída ocorreu após o horário de fechamento do expediente
        if (saida.toLocalTime().isAfter(fimExpediente)) {
            tempoMultaMinutos += Duration.between(fimExpediente, saida.toLocalTime()).toMinutes();
        }

        if (dias > 0) {
            int duracaoExpediente = (int) Duration.between(inicioExpediente, fimExpediente).toMinutes();
            tempoMultaMinutos += dias * duracaoExpediente;
        }

        // Calcular horas e minutos da multa
        int tempoMultaHoras = tempoMultaMinutos / 60;
        int tempoMultaMinutes = tempoMultaMinutos % 60;

        movimentacao.setTempoMultaHoras(tempoMultaHoras);
        movimentacao.setTempoMultaMinutes(tempoMultaMinutes);

        // Calculando valor multa
        BigDecimal valorMultaTotal = (new BigDecimal(movimentacao.getTempoMultaHoras())
                .multiply(movimentacao.getValorHoraMulta()))
                .add(new BigDecimal(movimentacao.getTempoMultaMinutes())
                        .multiply(movimentacao.getValorHoraMulta()
                                .divide(new BigDecimal(60), RoundingMode.HALF_UP)));

        movimentacao.setValorMulta(valorMultaTotal);
    }


    private void manageDesconto(Movimentacao movimentacao) {

        Condutor condutor = condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);

        if (condutor == null) {
            return;
        }

        int tempoPagoHoras = condutor.getTempoPagoHoras();
        int tempoHoras = movimentacao.getTempoHoras();
        Configuracao configuracao = obterConfiguracao();

        int currentMultiple = tempoPagoHoras / configuracao.getTempoParaDesconto();
        int nextMultiple = (tempoPagoHoras + tempoHoras) / configuracao.getTempoParaDesconto();

        if (nextMultiple > currentMultiple) {
            int numNewMultiples = nextMultiple - currentMultiple;
            int descontoToAdd = numNewMultiples * configuracao.getTempoDeDesconto();
            int currentDesconto = condutor.getTempoDescontoHoras();
            int newDescontoHours = currentDesconto + descontoToAdd;
            condutor.setTempoDescontoHoras(newDescontoHours);
        }

        int tempoDesconto = condutor.getTempoDescontoHoras();
        movimentacao.setTempoDesconto(tempoDesconto);

        if (configuracao.getGerarDesconto()) {
            BigDecimal valorDesconto = BigDecimal.valueOf(tempoDesconto).multiply(movimentacao.getValorHora());
            movimentacao.setValorDesconto(valorDesconto);
        } else {
            movimentacao.setValorDesconto(BigDecimal.ZERO);
        }
    }


    private void verificarVagasDisponiveis(Movimentacao movimentacao) {
        Tipo tipoVeiculo = veiculoRepository.getTipoVeiculo(movimentacao.getVeiculo().getId());

        List<Movimentacao> qtdeVeiculo = movimentacaoRepository.findByVeiculoTipo(tipoVeiculo);

        int vagasOccupadas = qtdeVeiculo.size();

        int vagasDisponiveis = switch (tipoVeiculo) {
            case MOTORCYCLE -> obterConfiguracao().getVagasMoto() - vagasOccupadas;
            case CAR -> obterConfiguracao().getVagasCarro() - vagasOccupadas;
            case VAN -> obterConfiguracao().getVagasVan() - vagasOccupadas;
            default -> throw new IllegalArgumentException("Tipo de veículo inválido.");
        };

        if (vagasDisponiveis <= 0) {
            throw new IllegalArgumentException("There are no available parking spaces for " +
                    veiculoRepository.getTipoVeiculo(movimentacao.getVeiculo().getId()).toString());
        }
    }

    private void configurarValoresPadrao(Movimentacao movimentacao) {

        // Configurar valores padrão para a movimentação
        movimentacao.setTempoHoras(0);
        movimentacao.setTempoMinutos(0);
        movimentacao.setValorDesconto(BigDecimal.ZERO);
        movimentacao.setValorMulta(BigDecimal.ZERO);

    }

    public String emitirRelatorio(Movimentacao movimentacao) {
        String nomeCondutor = condutorRepository.findByNomeId(movimentacao.getCondutor().getId());
        String phoneCondutor = condutorRepository.findByPhone(movimentacao.getCondutor().getId());
        String placaVeiculo = veiculoRepository.findByPlacaID(movimentacao.getVeiculo().getId());
        Tipo tipo = veiculoRepository.getTipoVeiculo(movimentacao.getVeiculo().getId());
        String ano = veiculoRepository.findByAnoID(movimentacao.getVeiculo().getId());

        StringBuilder reportBuilder = new StringBuilder();
        String lineSeparator = "╟─────────────────────────────────────────────────────╢";
        String headerSeparator = "╠═════════════════════════════════════════════════════╣";

        reportBuilder.append("╔═════════════════════════════════════════════════════╗\n");
        reportBuilder.append("║            Fechamento da Movimentação               ║\n");
        reportBuilder.append(headerSeparator).append("\n");
        reportBuilder.append("║           Informações sobre o Condutor              ║\n");
        reportBuilder.append(lineSeparator).append("\n");
        reportBuilder.append("║ Nome do Condutor:             ").append(nomeCondutor).append("\n");
        reportBuilder.append("║ Telefone do Condutor:         ").append(phoneCondutor).append("\n");
        reportBuilder.append("║ Quantidade de Horas Desconto: ").append(movimentacao.getTempoDesconto())
                .append(" horas\n");
        reportBuilder.append(headerSeparator).append("\n");
        reportBuilder.append("║            Informações sobre o Veículo              ║\n");
        reportBuilder.append(lineSeparator).append("\n");
        reportBuilder.append("║ Placa do Veiculo:               ").append(placaVeiculo).append("\n");
        reportBuilder.append("║ Tipo  do Veiculo:               ").append(tipo).append("\n");
        reportBuilder.append("║ Ano de Fabricação:              ").append(ano).append("\n");
        reportBuilder.append(headerSeparator).append("\n");
        reportBuilder.append("║        Informações sobre a Movimentação Atual       ║\n");
        reportBuilder.append(lineSeparator).append("\n");
        reportBuilder.append("║ Data de Entrada:           ").append(movimentacao.getEntrada()).append("\n");
        reportBuilder.append("║ Data de Saída:             ").append(movimentacao.getSaida()).append("\n");
        reportBuilder.append("║ Tempo Estacionado:         ").append(movimentacao.getTempoHoras())
                .append(" horas e ")
                .append(movimentacao.getTempoMinutos()).append(" minutos\n");
        reportBuilder.append("║ Tempo Multa:               ").append(movimentacao.getTempoMultaHoras())
                .append(" horas e ")
                .append(movimentacao.getTempoMultaMinutes()).append(" minutos\n");
        reportBuilder.append("║ Tempo de Desconto:         ").append(movimentacao.getTempoDesconto())
                .append(" horas\n");
        reportBuilder.append(headerSeparator).append("\n");
        reportBuilder.append("║          Valores da Movimentação Atual              ║\n");
        reportBuilder.append(lineSeparator).append("\n");
        reportBuilder.append("║ Valor da Multa:            ").append(movimentacao.getValorMulta()).append("\n");
        reportBuilder.append("║ Valor de Desconto:         ").append(movimentacao.getValorDesconto()).append("\n");
        reportBuilder.append("║ Valor Total:               ").append(movimentacao.getValorTotal()).append("\n");
        reportBuilder.append("╚═════════════════════════════════════════════════════╝\n");

        System.out.println(reportBuilder.toString());
        return reportBuilder.toString();
    }

    public Page<Movimentacao> listAll(Pageable pageable) {
        return this.movimentacaoRepository.findAll(pageable);
    }


}