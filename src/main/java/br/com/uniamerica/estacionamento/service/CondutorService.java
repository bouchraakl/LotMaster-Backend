//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
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
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Valida o cadastro de um novo condutor.
     *
     * @param condutor O condutor a ser validado.
     */
    @Transactional
    public void validarCadastroCondutor(Condutor condutor) {

        // Verifica se não há nenhum outro condutor com o mesmo CPF cadastrado
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(),
                "Um condutor já está registrado com o CPF informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");


        // Verifica se o telefone do condutor está no formato correto
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat) ,
                "O número de telefone fornecido não está no formato válido. " +
                        "O formato deve seguir o padrão: +55(xx)xxxxxxxxx " +
                        "Por favor, corrija o número de telefone e tente novamente.");

        this.condutorRepository.save(condutor);

    }

    /**
     * Valida a atualização de um condutor existente.
     *
     * @param condutor O condutor a ser validado.
     */
    @Transactional
    public void validarUpdateCondutor(Condutor condutor) {

        // Verificar se o condutor existe no banco de dados
        Assert.notNull(condutor.getId(),
                "O ID do condutor fornecido é nulo. " +
                        "Certifique-se de que o objeto do condutor tenha um ID válido antes de realizar essa operação.");

        // Verifica se ID do condutor existe no banco de dados
        Assert.isTrue(condutorRepository.existsById(condutor.getId()),
                "O ID do condutor especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");


        // Verifica se não há nenhum outro condutor com o mesmo CPF cadastrado
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(),
                "Um condutor já está registrado com o CPF informado. " +
                        "Por favor, verifique os dados informados e tente novamente.");


        // Verifica se o telefone do condutor está no formato correto
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat),
                "O número de telefone fornecido não está no formato válido. " +
                        "O formato deve seguir o padrão: +55(xx)xxxxxxxxx " +
                        "Por favor, corrija o número de telefone e tente novamente.");

        this.condutorRepository.save(condutor);

    }

    /**
     * Método responsável por validar a exclusão de um condutor através do seu ID.
     *
     * @param id O ID do condutor a ser excluído.
     * @throws IllegalArgumentException caso o ID do condutor não exista no repositório.
     */
    @Transactional
    public void validarDeleteCondutor(Long id) {

        // Verifica se ID do condutor existe no banco de dados
        Assert.isTrue(condutorRepository.existsById(id),
                "O ID do condutor especificado não foi encontrado na base de dados. " +
                        "Por favor, verifique se o ID está correto e tente novamente.");

    }

}