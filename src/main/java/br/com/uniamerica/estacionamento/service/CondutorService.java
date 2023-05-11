//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;


//------------------------------------------------
@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroCondutor(Condutor condutor) {

        // Verificar se o nome está informado
        Assert.notNull(condutor.getNome(), "Nome do condutor não informado!");
        Assert.hasText(condutor.getNome(),"Campo nome não preenchido");

        // Verificar se o CPF is unique
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(), "CPF existe no banco de dados");

        // Verificar se o CPF está informado
        Assert.notNull(condutor.getCpf(), "CPF não informado!");
        Assert.hasText(condutor.getCpf(),"Campo CPF não preenchido");

        // Verificar se o CPF está no padrão correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat), "CPF em formato inválido.");

        // Verificar se o telefone está válido
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat), "Telefone em formato inválido.");
        Assert.hasText(condutor.getTelefone(),"Campo Telefone não preenchido");

    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateCondutor(Condutor condutor) {

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(condutor.getNome(), "Nome do condutor não informado.");
        Assert.notNull(condutor.getCpf(), "CPF do condutor não informado.");
        Assert.notNull(condutor.getTelefone(), "Telefone do condutor não informado.");

        // Verificar se o CPF informado e unique
        final List<Condutor> condutorbyCPF = this.condutorRepository.findbyCPF(condutor.getCpf());
        Assert.isTrue(condutorbyCPF.isEmpty(), "CPF existe no banco de dados");

        // Verificar se o CPF está no padrão correto
        final String cpfFormat = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(cpfFormat), "CPF em formato inválido.");

        // Verificar se o telefone está válido
        final String telefoneFormat = "\\+55\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(telefoneFormat), "Telefone em formato inválido.");

        Assert.hasText(condutor.getTelefone(),"Campo Telefone não preenchido");
        Assert.hasText(condutor.getCpf(),"Campo CPF não preenchido");
        Assert.hasText(condutor.getNome(),"Campo nome não preenchido");

    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteCondutor(Long id) {

        Assert.isTrue(condutorRepository.existsById(id), "ID condutor não existe.");

    }

}
