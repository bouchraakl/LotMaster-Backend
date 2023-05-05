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
public class MarcaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroMarca(Marca marca) {
        // Verificar se o nome está informado
        Assert.notNull(marca.getNome(), "Nome do marca não informado!");

        // Verificar se o nome do marca já existe
        List<Marca> marcasByNome = marcaRepository.findByNome(marca.getNome());
        Assert.isTrue(marcasByNome.isEmpty(), "Nome do marca já cadastrado.");

        // Verificar se a marca já foi cadastrada
        Marca marcaBanco = marcaRepository.findById(marca.getId()).orElse(null);
        Assert.notNull(marcaBanco, "Objeto marca já cadastrado no banco de dados.");
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateMarca(Marca marca) {
        // Verificar se o ID do marca não é nulo
        Assert.notNull(marca.getId(), "Objeto marca não encontrado no banco de dados.");

        // Verificar se os campos obrigatórios foram preenchidos
        Assert.notNull(marca.getNome(), "Nome do marca não informado.");
    }

}
