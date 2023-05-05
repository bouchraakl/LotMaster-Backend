//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


//------------------------------------------------
@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroCondutor(Condutor condutor) {
        // Verificar se o nome está informado
        Assert.notNull(condutor.getNome(), "Nome do condutor não informado!");

        // Verificar se o CPF está informado
        Assert.notNull(condutor.getCpf(), "CPF não informado!");

        // Verificar se o CPF está no padrão correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat), "CPF inválido!");

        // Verificar se o telefone está válido
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat), "Telefone inválido!");

        // Verificar se o condutor já foi cadastrado
        final Condutor condutorBanco = this.condutorRepository.findById(condutor.getId()).orElse(null);
        Assert.isNull(condutorBanco, "Condutor já cadastrado no banco de dados.");
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateCondutor(Condutor condutor) {
        // Verificar se o condutor com o ID informado existe no banco de dados
        Assert.notNull(condutor.getId(), "Objeto condutor não encontrado no banco de dados.");

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(condutor.getNome(), "Nome do condutor não informado.");
        Assert.notNull(condutor.getCpf(), "CPF do condutor não informado.");
        Assert.notNull(condutor.getTelefone(), "Telefone do condutor não informado.");

        // Verificar se o CPF está no padrão correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat), "CPF inválido!");

        // Verificar se o telefone está válido
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat), "Telefone inválido!");
    }

}
