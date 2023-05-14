//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/*
- Essa classe é responsável por realizar validações de dados relacionados a marcas.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de marca é solicitado.
*/
@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    /**
     * Valida o cadastro de uma nova Marca.
     *
     * @param marca objeto Marca a ser validado
     * @throws IllegalArgumentException caso o nome da Marca já exista no banco de dados ou não tenha sido informada
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMarca(Marca marca) {

        // Verifica se a data de cadastro foi informada
        Assert.notNull(marca.getCadastro(),
                "O cadastro da marca não pode ser nulo. " +
                        "Verifique se todas as informações foram preenchidas corretamente.");

        // Verificar se o nome da marca foi informado
        Assert.notNull(marca.getNome(), "O nome da marca não pode ser nula.");

        // Verificar se a marca já existe no banco de dados
        List<Marca> marcasByNome = marcaRepository.findByNome(marca.getNome());
        Assert.isTrue(marcasByNome.isEmpty(),
                "Uma marca já está registrada com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verificar se o campo nome foi preenchido
        Assert.hasText(marca.getNome(), "O nome do marca não pode ser vazio.");

        // Verifica se o nome do condutor contém apenas letras, espaços e hífens
        final String nomeFormat = "^[a-zA-ZÀ-ÿ0-9\\\\s\\\\-]+$";
        Assert.isTrue(marca.getNome().matches(nomeFormat),
                "O nome da marca deve conter apenas letras, espaços e hífens. " +
                        "Por favor, verifique e tente novamente.");

    }

    /**
     * Valida a atualização de uma Marca existente.
     *
     * @param marca objeto Marca a ser validado
     * @throws IllegalArgumentException caso o ID da Marca não tenha sido informado ou o nome já exista no banco de dados
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMarca(Marca marca) {

        // Verifica se a data de cadastro foi informada
        Assert.notNull(marca.getCadastro(),
                "O cadastro da marca não pode ser nulo. " +
                        "Verifique se todas as informações foram preenchidas corretamente.");

        // Verificar se o marca existe no banco de dados
        Assert.notNull(marca.getId(),
                "O ID da marca fornecido é nulo. " +
                        "Certifique-se de que o objeto da marca tenha um ID válido antes de realizar essa operação.");

        // Verifica se ID do condutor existe no banco de dados
        Assert.isTrue(marcaRepository.existsById(marca.getId()),
                "O ID da marca especificadoa não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

        // Verificar se o nome da marca foi informado
        Assert.notNull(marca.getNome(), "O nome da marca não pode ser nula.");

        // Verificar se a marca existe no banco de dados
        Assert.notNull(marca.getId(),
                "O ID da marca fornecida é nulo. " +
                        "Certifique-se de que o objeto de marca tenha um ID válido antes de realizar essa operação.");

        // Verificar se já existe uma marca com o mesmo nome no banco de dados
        List<Marca> marcasByNome = marcaRepository.findByNome(marca.getNome());
        Assert.isTrue(marcasByNome.isEmpty(),
                "Uma marca já está registrada com o nome informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verificar se o campo nome foi preenchido
        Assert.hasText(marca.getNome(), "O nome do marca não pode ser vazio.");

        // Verifica se o nome do condutor contém apenas letras, espaços e hífens
        final String nomeFormat = "^[a-zA-ZÀ-ÿ0-9\\\\s\\\\-]+$";
        Assert.isTrue(marca.getNome().matches(nomeFormat),
                "O nome da marca deve conter apenas letras, espaços e hífens. " +
                        "Por favor, verifique e tente novamente.");

    }

    /**
     * Valida a exclusão de uma Marca existente.
     *
     * @param id ID da Marca a ser validada
     * @throws IllegalArgumentException caso o ID não exista no banco de dados
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteMarca(Long id) {

        // Verificar se a marca com o ID fornecido existe no banco de dados
        Assert.isTrue(marcaRepository.existsById(id),
                "O ID da marca especificada não foi encontrada na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }

}
