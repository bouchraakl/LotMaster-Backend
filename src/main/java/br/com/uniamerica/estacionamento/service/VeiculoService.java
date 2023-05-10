//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.*;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.time.Year;
import java.util.List;


//------------------------------------------------
@Service
public class VeiculoService {

    private static final int MIN_ALLOWED_YEAR = 2008;
    int currentYear = Year.now().getValue();
    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroVeiculo(Veiculo veiculo) {

        Assert.notNull(veiculo.getCadastro(), "Data de cadastro não informada.");

        Range<Integer> rangeAno = Range.closed(MIN_ALLOWED_YEAR, currentYear);
        Assert.isTrue(rangeAno.contains(veiculo.getAno()), "Ano do veículo está fora do range permitido.");

        Assert.notNull(veiculo.getCor(), "Cor do veículo não informada.");

        // Verificar se a placa não está vazia e é válida

        final List<Veiculo> veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculoByPlaca.isEmpty(), "Placa existe no banco de dados.");

        Assert.notNull(veiculo.getPlaca(), "Placa não informada.");

        final String placaFormat = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(veiculo.getPlaca().matches(placaFormat), "Placa em formato inválido.");

        Assert.notNull(veiculo.getTipo(), "Tipo do veículo não informado.");

        // Verificar se o modelo do veículo não é nulo.
        Assert.notNull(veiculo.getModelo(), "Objeto modelo não informado.");

        // Verificar se o ID do modelo do veículo não é nulo.
        Assert.notNull(veiculo.getModelo().getId(), "ID modelo não informado.");

    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateVeiculo(Veiculo veiculo) {
        /* Verificar se o ID do veiculo não é nulo*/
        Assert.notNull(veiculo.getId(), "Veículo não existe no banco de dados.");

        // Verificar se a placa não está vazia e é válida

//        final List<Veiculo> veiculoByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
//        Assert.isTrue(veiculoByPlaca.isEmpty(), "Placa existe no banco de dados.");

        Assert.notNull(veiculo.getPlaca(), "Placa não informada.");

        final String placaFormat = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(veiculo.getPlaca().matches(placaFormat), "Placa em formato inválido.");

        // Verificar se o modelo do veículo não é nulo.
        Assert.notNull(veiculo.getModelo(), "Objeto modelo não informado.");

        Assert.notNull(veiculo.getModelo().getId(), "ID modelo não informado.");
        Assert.isTrue(this.modeloRepository.existsById(veiculo.getModelo().getId()),
                "Modelo não existe no banco de dados");

        Range<Integer> rangeAno = Range.closed(MIN_ALLOWED_YEAR, currentYear);
        Assert.isTrue(rangeAno.contains(veiculo.getAno()), "Ano do veículo está fora do range permitido.");
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarDeleteVeiculo(Long id) {
        Assert.isTrue(veiculoRepository.existsById(id), "ID veiculo não existe");
    }


}
