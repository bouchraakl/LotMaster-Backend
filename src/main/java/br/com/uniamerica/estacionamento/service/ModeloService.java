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

import java.util.List;

//------------------------------------------------
@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroModelo(Modelo modelo) {

        // Verificar se o nome está informado
        Assert.notNull(modelo.getNome(), "Nome do modelo não informado!");

        // Verificar se a marca foi informada
        Assert.notNull(modelo.getMarca(), "Objeto marca não informado!");

        // Verificar se o ID da marca do modelo não é nulo.
        Assert.notNull(modelo.getMarca().getId(), "ID marca não informado.");

        // Verificar se o nome do modelo já existe
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(), "Nome do modelo existe no banco de dados");

        Assert.hasText(modelo.getNome(),"Campo nome não preenchido.");

        Assert.isTrue(modelo.getMarca().isAtivo(),"Marca inativa.");

    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateModelo(Modelo modelo) {

        // Verificar se o nome do modelo já existe
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(), "Nome do modelo já existe no banco de dados.");

        // Verificar se a marca foi informada
        Assert.notNull(modelo.getMarca(), "Objeto marca não informado!");

        // Verificar se o ID da marca não é nulo
        Assert.notNull(modelo.getMarca().getId(), "Objeto marca não encontrado no banco de dados.");

        // Verificar se a id marca já existe
        Assert.isTrue(marcaRepository.existsById(modelo.getMarca().getId()),
                "ID não existe no banco de dados : " + modelo.getMarca().getId());

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(modelo.getNome(), "Nome do modelo não informado.");
        Assert.notNull(modelo.getCadastro(), "Data de cadastro do modelo não informada.");

        Assert.hasText(modelo.getNome(),"Campo nome não preenchido.");
        Assert.isTrue(modelo.getMarca().isAtivo(),"Marca inativa.");
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteModelo(Long id) {
        Assert.isTrue(modeloRepository.existsById(id), "ID do modelo não existe");
    }


}





