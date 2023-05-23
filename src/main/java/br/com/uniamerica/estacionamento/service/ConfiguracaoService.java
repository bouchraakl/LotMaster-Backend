package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Service class for managing configurations.
 */
@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    /**
     * Constructs a ConfiguracaoService with the specified repository.
     *
     * @param configuracaoRepository the repository for configurations
     */
    @Autowired
    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    /**
     * Validates and saves the provided configuration.
     *
     * @param configuracao the configuration to be validated and saved
     */
    @Transactional
    public void validarCadastroConfiguracao(Configuracao configuracao) {
        configuracao.setCadastro(LocalDateTime.now());
        configuracaoRepository.save(configuracao);
    }

    /**
     * Validates and updates the provided configuration.
     *
     * @param configuracao the configuration to be validated and updated
     */
    @Transactional(rollbackFor = Exception.class)
    public void validarUpdateConfiguracao(Configuracao configuracao) {
        configuracao.setAtualizacao(LocalDateTime.now());
        validateConfiguracaoId(configuracao);
        configuracaoRepository.save(configuracao);
    }

    /**
     * Validates the ID of the provided configuration.
     *
     * @param configuracao the configuration to be validated
     * @throws IllegalArgumentException if the ID of the configuration is null or not found in the database
     */
    private void validateConfiguracaoId(Configuracao configuracao) {
        Assert.notNull(configuracao.getId(),
                "O ID da configuracao fornecido é nulo. " +
                        "Certifique-se de que o objeto da configuracao tenha" +
                        " um ID válido antes de realizar essa operação.");
        Assert.isTrue(configuracaoRepository.existsById(configuracao.getId()),
                "O ID da configuracao especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");
    }
}
