//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
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
    @Transactional
    public void validarCadastroModelo(Modelo modelo) {

        modelo.setCadastro(LocalDateTime.now());

        // Verificar se o nome do modelo já existe no banco de dados
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(),
                "Um modelo já está registrado com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verificar se o ID da marca foi informado e se ele existe no banco de dados
        Assert.notNull(modelo.getMarca().getId(), "O ID da marca em modelo não pode ser nulo.");

        Assert.isTrue(marcaRepository.existsById(modelo.getMarca().getId()),
                "Não foi possível salvar o modelo, pois a marca associada não foi encontrada.");

        final List<Marca> isActive = marcaRepository.findActiveElement(modelo.getMarca().getId());
        Assert.isTrue(!isActive.isEmpty(), "A marca associada a esse modelo está inativa.");

        this.modeloRepository.save(modelo);

    }

    /**
     * Verifica se as informações de um modelo já existente foram corretamente atualizadas.
     *
     * @param modelo O modelo a ser validado.
     * @throws IllegalArgumentException Se as informações do modelo não estiverem corretas.
     */
    @Transactional
    public void validarUpdateModelo(Modelo modelo) {

        modelo.setAtualizacao(LocalDateTime.now());

        // Verificar se o modelo existe no banco de dados
        Assert.notNull(modelo.getId(),
                "O ID do modelo fornecido é nulo. " +
                        "Certifique-se de que o objeto do modelo tenha um ID válido antes de realizar essa operação.");

        // Verifica se ID do modelo existe no banco de dados
        Assert.isTrue(modeloRepository.existsById(modelo.getId()),
                "O ID do modelo especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

        // Verificar se o ID da marca foi informado e se ele existe no banco de dados
        Assert.notNull(modelo.getMarca().getId(), "ID marca não informado.");

        Assert.isTrue(marcaRepository.existsById(modelo.getMarca().getId()),
                "Não foi possível salvar o modelo, pois a marca associada não foi encontrada.");

        final List<Marca> isActive = marcaRepository.findActiveElement(modelo.getMarca().getId());
        Assert.isTrue(!isActive.isEmpty(), "A marca associada a esse modelo está inativa.");

        this.modeloRepository.save(modelo);

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






