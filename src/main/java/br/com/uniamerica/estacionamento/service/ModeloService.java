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

        // Verificar se o nome do modelo já existe
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(), "nome do modelo já cadastrado");

        // Verificar se o nome do marca já existe
        final List<Marca> marcasByNome = this.marcaRepository.findByNome(modelo.getMarca().getNome());
        Assert.isTrue(marcasByNome.isEmpty(), "nome da marca já cadastrado");

        // Verificar se a marca já foi cadastrada
        final Marca marca = this.marcaRepository.findById(modelo.getMarca().getId()).orElse(null);
        Assert.isNull(marca, "Objeto marca já cadastrado no banco de dados");
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateModelo(Modelo modelo) {

//        // Verificar se o ID do modelo não é nulo
//        Assert.notNull(modelo.getId(), "Objeto modelo não encontrado no banco de dados");

        // Verificar se o nome do modelo já existe
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        Assert.isTrue(modelosByNome.isEmpty(), "nome do modelo já cadastrado");

        // Verificar se o nome do marca já existe
        final List<Marca> marcasByNome = this.marcaRepository.findByNome(modelo.getMarca().getNome());
        Assert.isTrue(marcasByNome.isEmpty(), "nome da marca já cadastrado");

        // Verificar se o ID da marca não é nulo
        Assert.notNull(modelo.getMarca().getId(), "Objeto marca não encontrado no banco de dados");

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(modelo.getNome(), "Nome do modelo não informado");
        Assert.notNull(modelo.getCadastro(), "Data de cadastro do modelo não informada");
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public void validarDeleteModelo(Modelo modelo){

        // Verificar se o ID do modelo existe
        Assert.notNull(modelo.getId(),"ID modelo não existe no banco de dados");
    }


}





