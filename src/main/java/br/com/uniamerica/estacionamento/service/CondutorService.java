//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/*
- Essa classe é responsável por realizar validações de dados relacionados a condutores.
- Todas as validações são realizadas através de métodos que são executados quando um
  cadastro, atualização ou exclusão de condutor é solicitado.
*/
@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    /**
     * Valida o cadastro de um novo condutor.
     *
     * @param condutor O condutor a ser validado.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroCondutor(Condutor condutor) {


        // Verifica se o nome do condutor não é nulo
        Assert.notNull(condutor.getNome(), "O nome do condutor não pode ser nulo.");

        // Verifica se o nome do condutor não é uma string vazia
        Assert.hasText(condutor.getNome(), "O nome do condutor não pode ser vazio.");

        // Verifica se não há nenhum outro condutor com o mesmo CPF cadastrado
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(),
                "Um condutor já está registrado com o CPF informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verifica se o CPF não é nulo
        Assert.notNull(condutor.getCpf(), "O CPF do condutor não pode ser nulo.");

        // Verifica se o CPF não é uma string vazia
        Assert.hasText(condutor.getCpf(), "O CPF do condutor não pode ser vazio.");

        // Verifica se o CPF está no formato correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat),
                "O CPF do condutor deve estar no formato xxx.xxx.xxx-xx. " +
                        "Por favor, verifique e tente novamente.");

        // Verifica se o telefone do condutor está no formato correto
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat),
                "O número de telefone fornecido não está no formato válido. " +
                        "O formato deve seguir o padrão: +55(xx)xxxxxxxxx, " +
                        "Por favor, corrija o número de telefone e tente novamente.");

        // Verifica se o telefone do condutor não é uma string vazia
        Assert.hasText(condutor.getTelefone(), "O telefone do condutor não pode ser vazio.");
    }

    /**
     * Valida a atualização de um condutor existente.
     *
     * @param condutor O condutor a ser validado.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateCondutor(Condutor condutor) {

        // Verifica se o nome do condutor não é nulo
        Assert.notNull(condutor.getNome(), "O nome do condutor não pode ser nulo.");

        // Verifica se o nome do condutor não é uma string vazia
        Assert.hasText(condutor.getNome(), "O nome do condutor não pode ser vazio.");

        // Verifica se não há nenhum outro condutor com o mesmo CPF cadastrado
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(),
                "Um condutor já está registrado com o CPF informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");

        // Verifica se o CPF está no formato correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat),
                "O CPF do condutor deve estar no formato xxx.xxx.xxx-xx. " +
                        "Por favor, verifique e tente novamente.");

        // Verifica se o CPF não é nulo
        Assert.notNull(condutor.getCpf(), "O CPF do condutor não pode ser nulo.");

        // Verifica se o CPF não é uma string vazia
        Assert.hasText(condutor.getCpf(), "O CPF do condutor não pode ser vazio.");

        // Verifica se o telefone do condutor está no formato correto
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat),
                "O número de telefone fornecido não está no formato válido. " +
                        "O formato deve seguir o padrão: +55(xx)xxxxxxxxx, " +
                        "Por favor, corrija o número de telefone e tente novamente.");

        // // Verifica se o telefone do condutor não é nulo
        Assert.notNull(condutor.getTelefone(), "O telefone do condutor não pode ser nulo.");

        // Verifica se o telefone do condutor não é uma string vazia
        Assert.hasText(condutor.getTelefone(), "O telefone do condutor não pode ser vazio.");

    }

    /**
     * Método responsável por validar a exclusão de um condutor através do seu ID.
     *
     * @param id O ID do condutor a ser excluído.
     * @throws IllegalArgumentException caso o ID do condutor não exista no repositório.
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteCondutor(Long id) {

        // Verifica se ID do condutor existe no banco de dados
        Assert.isTrue(condutorRepository.existsById(id),
                "O ID do condutor especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }

}
