//------------------Package----------------------
package br.com.uniamerica.estacionamento.service;

//------------------Imports----------------------

import br.com.uniamerica.estacionamento.entity.Cor;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Tipo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Year;
import java.util.Arrays;
import java.util.regex.Pattern;

//------------------------------------------------
@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarCadastroVeiculo(Veiculo veiculo) {

        // Verificar se a id veiculo já existe
        Assert.isTrue(veiculoRepository.existsById(veiculo.getId()),
                "ID já existe no banco de dados : " + veiculo.getId());

        // Verificar se a placa não está vazia e é válida
        Assert.notNull(veiculo.getPlaca(), "Placa não informada.");
        validarPlaca(veiculo.getPlaca());

        // Verificar se o modelo do veículo não é nulo.
        Assert.notNull(veiculo.getModelo(), "Objeto modelo não informado.");

        // Verificar se o tipo do veículo não é nulo.
        Assert.notNull(veiculo.getTipo(), "Tipo do veículo não informado.");
        validarTipoVeiculo(veiculo.getTipo());

        // Verificar se a cor do veículo não é nula.
        Assert.notNull(veiculo.getCor(), "Cor do veículo não informada.");
        validarCorVeiculo(veiculo.getCor());

        // Verificar se o ano do veículo é válido
        validarAnoVeiculo(veiculo.getAno());
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void validarUpdateVeiculo(Veiculo veiculo) {
        /* Verificar se o ID do veiculo não é nulo*/
        Assert.notNull(veiculo.getId(), "Veículo não cadastrado no banco de dados.");

        /* Verificar se os campos obrigatórios são preenchidos */
        Assert.notNull(veiculo.getPlaca(), "Placa não informada.");
        validarPlaca(veiculo.getPlaca());

        // Verificar se o modelo do veículo não é nulo.
        Assert.notNull(veiculo.getModelo(), "Objeto modelo não informado.");

        // Verificar se a marca associada para o veículo não é nula.
        Assert.notNull(veiculo.getModelo().getMarca(), "Objeto modelo não informado.");

        validarTipoVeiculo(veiculo.getTipo());
        validarCorVeiculo(veiculo.getCor());
        validarAnoVeiculo(veiculo.getAno());
    }

    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public void validarDeleteVeiculo(Veiculo veiculo){

        // Verificar se o ID do veiculo existe
        Assert.notNull(veiculo.getId(),"ID marca não existe no banco de dados");
    }

    public void validarPlaca(String placa) {
        final String PADRAO_REGEX_PLACA_BRASILEIRA = "^[A-Z]{3}\\d{4}$";
        Assert.isTrue(placa.matches(PADRAO_REGEX_PLACA_BRASILEIRA), "Placa inválida.");
    }

    public void validarAnoVeiculo(int ano) {
        Year thisYear = Year.now();
        int currentYear = thisYear.getValue();
        Range<Integer> rangeAno = Range.closed(2008, currentYear);
        Assert.isTrue(rangeAno.contains(ano), "Ano do veículo está fora do range permitido.");
    }

    public void validarTipoVeiculo(Tipo tipo) {
        Tipo[] tiposValidos = {Tipo.Carro, Tipo.Van, Tipo.Moto};
        Assert.isTrue(Arrays.asList(tiposValidos).contains(tipo), "Tipo de veículo inválido.");
    }

    public void validarCorVeiculo(Cor cor) {
        Cor[] coresValidas = {Cor.Azul, Cor.Branco, Cor.Cinza, Cor.Prata, Cor.Rosa, Cor.Preto};
        Assert.isTrue(Arrays.asList(coresValidas).contains(cor), "Cor de veículo inválida.");
    }

}
