//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/*
- Essa classe é responsável por realizar validações de dados relacionados a modelos.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de modelo é solicitado.
*/
@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    /**
     * Verifica se as informações de um novo modelo estão corretas antes de serem cadastradas no banco de dados.
     *
     * @param modelo O modelo a ser validado.
     * @throws IllegalArgumentException Se as informações do modelo não estiverem corretas.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroModelo(Modelo modelo) {

        // Verificar se o nome do modelo foi informado
        Assert.notNull(modelo.getNome(), "O nome do modelo não pode ser nulo.");

        // Verificar se o campo nome foi preenchido
        Assert.hasText(modelo.getNome(), "O nome do modelo não pode ser vazio.");

        // Verificar se o nome do modelo já existe no banco de dados
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(),
                "Um modelo já está registrado com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verifica se o nome do modelo contém apenas letras, espaços e hífens
        final String nomeFormat = "^[a-zA-ZÀ-ÿ0-9\\\\s\\\\-]+$";
        Assert.isTrue(modelo.getNome().matches(nomeFormat),
                "O nome do modelo deve conter apenas letras, números, espaços e hífens. " +
                        "Por favor, verifique e tente novamente.");


        // Verificar se o objeto marca foi informado
        Assert.notNull(modelo.getMarca(), "O objeto marca não foi informado." +
                " Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verificar se o ID da marca foi informado e se ele existe no banco de dados
        Assert.notNull(modelo.getMarca().getId(), "O ID da marca em modelo não pode ser nulo.");
        Assert.isTrue(marcaRepository.existsById(modelo.getMarca().getId()),
                "Não foi possível salvar o modelo, pois a marca associada não foi encontrada.");

        // Verificar se a marca está ativa
        Assert.isTrue(modelo.getMarca().isAtivo(),
                "A marca associada a esse modelo está inativa. " +
                        "Por favor, verifique o status da marca e tente novamente.");

    }

    /**
     * Verifica se as informações de um modelo já existente foram corretamente atualizadas.
     *
     * @param modelo O modelo a ser validado.
     * @throws IllegalArgumentException Se as informações do modelo não estiverem corretas.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateModelo(Modelo modelo) {

        // Verificar se a data de cadastro do modelo foi informada
        Assert.notNull(modelo.getCadastro(),
                "O cadastro do veículo não pode ser nulo. " +
                        "Verifique se todas as informações foram preenchidas corretamente.");

        // Verificar se o modelo existe no banco de dados
        Assert.notNull(modelo.getId(),
                "O ID do modelo fornecido é nulo. " +
                        "Certifique-se de que o objeto do modelo tenha um ID válido antes de realizar essa operação.");

        // Verifica se ID do modelo existe no banco de dados
        Assert.isTrue(modeloRepository.existsById(modelo.getId()),
                "O ID do modelo especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

        // Verificar se o nome do modelo foi informado
        Assert.notNull(modelo.getNome(), "O nome do modelo não pode ser nulo.");

        // Verificar se o campo nome foi preenchido
        Assert.hasText(modelo.getNome(), "O nome do modelo não pode ser vazio.");

        // Verificar se o nome do modelo já existe no banco de dados
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(),
                "Um modelo já está registrado com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verifica se o nome do modelo contém apenas letras, espaços e hífens
        final String nomeFormat = "^[a-zA-ZÀ-ÿ0-9\\\\s\\\\-]+$";
        Assert.isTrue(modelo.getNome().matches(nomeFormat),
                "O nome do modelo deve conter apenas letras, números, espaços e hífens. " +
                        "Por favor, verifique e tente novamente.");

        // Verificar se o objeto marca foi informado
        Assert.notNull(modelo.getMarca(),
                "O objeto marca não foi informado." +
                        " Por favor, preencha todas as informações obrigatórias para prosseguir com a movimentação.");

        // Verificar se o ID da marca foi informado e se ele existe no banco de dados
        Assert.notNull(modelo.getMarca().getId(), "ID marca não informado.");
        Assert.isTrue(marcaRepository.existsById(modelo.getMarca().getId()),
                "Não foi possível salvar o modelo, pois a marca associada não foi encontrada.");

        // Verificar se a marca está ativa
        Assert.isTrue(modelo.getMarca().isAtivo(),
                "A marca associada a esse modelo está inativa. " +
                        "Por favor, verifique o status da marca e tente novamente.");

    }

    /**
     * Verifica se as informações de um modelo a ser excluído estão corretas.
     *
     * @param id O ID do modelo a ser validado.
     * @throws IllegalArgumentException Se o ID do modelo não existir no banco de dados.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteModelo(Long id) {

        // Verificar se o ID do modelo existe no banco de dados
        Assert.isTrue(modeloRepository.existsById(id),
                "O ID do modelo especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }

}






