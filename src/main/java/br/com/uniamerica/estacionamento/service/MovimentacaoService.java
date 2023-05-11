//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Movimentacao;

import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.time.LocalTime;


//------------------------------------------------
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

    private static final LocalTime OPENING_TIME = LocalTime.parse("08:00:00");
    private static final LocalTime CLOSING_TIME = LocalTime.parse("18:00:00");
    private static final LocalTime CURRENT_TIME = LocalTime.now().withNano(0);

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMovimentacao(Movimentacao movimentacao) {

        Assert.isTrue(movimentacao.getVeiculo().isAtivo(),"Veiculo inativo.");
        Assert.isTrue(movimentacao.getCondutor().isAtivo(),"Condutor inativo.");

        Assert.notNull(movimentacao.getVeiculo(), "Objeto veículo não informado.");
        Assert.isTrue(this.veiculoRepository.existsById(movimentacao.getVeiculo().getId()),
                "ID veiculo não existe no banco de dados.");
        Assert.notNull(movimentacao.getVeiculo().getId(), "ID veiculo não informado.");

        Assert.notNull(movimentacao.getCondutor(), "Objeto condutor não informado.");
        Assert.isTrue(this.condutorRepository.existsById(movimentacao.getCondutor().getId()),
                "ID condutor não existe no banco de dados.");
        Assert.notNull(movimentacao.getCondutor().getId(), "ID condutor não informado.");

        // Verificar se a movimentação está dentro do horário de funcionamento do estacionamento
        Assert.isTrue(!CURRENT_TIME.isBefore(OPENING_TIME) || CURRENT_TIME.isAfter(CLOSING_TIME),
                "A movimentação está fora do horário de funcionamento do estacionamento.");


    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMovimentacao(Movimentacao movimentacao) {

        Assert.isTrue(movimentacao.getVeiculo().isAtivo(),"Veiculo inativo.");
        Assert.isTrue(movimentacao.getCondutor().isAtivo(),"Condutor inativo.");

        // Verificar se o ID da movimentacao não é nula e valido
        Assert.notNull(movimentacao.getId(), "ID movimentacao não informado.");

        Assert.notNull(movimentacao.getId(), "ID veiculo não informado.");
        Assert.notNull(movimentacao.getVeiculo(), "Objeto veículo não informado.");
        Assert.isTrue(this.veiculoRepository.existsById(movimentacao.getVeiculo().getId()),
                "ID veiculo não existe no banco de dados.");

        Assert.notNull(movimentacao.getId(), "ID condutor não informado.");
        Assert.notNull(movimentacao.getCondutor(), "Objeto condutor não informado.");
        Assert.isTrue(this.condutorRepository.existsById(movimentacao.getCondutor().getId()),
                "ID condutor não existe no banco de dados.");


        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(movimentacao.getEntrada(), "Data de entrada não informada.");
        Assert.notNull(movimentacao.getSaida(), "Data de entrada não informada.");

        // Verificar se a movimentação está dentro do horário de funcionamento do estacionamento
        Assert.isTrue(!CURRENT_TIME.isBefore(OPENING_TIME) || CURRENT_TIME.isAfter(CLOSING_TIME),
                "A movimentação está fora do horário de funcionamento do estacionamento.");


    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteMovimentacao(Long id) {
        Assert.isTrue(movimentacaoRepository.existsById(id), "ID da movimentacao não existe.");
    }

}
