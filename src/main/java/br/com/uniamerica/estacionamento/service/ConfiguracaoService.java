//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

//------------------------------------------------
@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroConfiguracao(final Configuracao configuracao) {

        Assert.notNull(configuracao.getValorHora(), "O valor da hora não pode ser nulo.");
        Assert.notNull(configuracao.getInicioExpediente(),
                "O horário de início de expediente não pode ser nulo.");
        Assert.notNull(configuracao.getFimExpediente(),
                "O horário de fim de expediente não pode ser nulo.");
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public void validarUpdateConfiguracao(Configuracao configuracao) {

        /*Verifica se a configuracao existe */
        Configuracao configuracaoBanco = this.configuracaoRepository.findById(configuracao.getId())
                .orElseThrow(() -> new IllegalArgumentException("Configuração não existe!"));


        Assert.notNull(configuracao.getValorHora(), "Valor hora não informada!");
        Assert.notNull(configuracao.getInicioExpediente(), "Início do expediente não informado!");
        Assert.notNull(configuracao.getFimExpediente(), "Fim do expediente não informado!");
    }

}


