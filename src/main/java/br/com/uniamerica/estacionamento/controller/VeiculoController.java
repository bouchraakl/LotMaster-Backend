package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/veiculo")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Retrieves a Veiculo by ID.
     *
     * @param id The ID of the Veiculo to retrieve.
     * @return ResponseEntity with the Veiculo if found, otherwise a bad request response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdRequest(@PathVariable("id") Long id)  {
        final Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        return veiculo == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(veiculo);
    }

    /**
     * Retrieves all Veiculos.
     *
     * @return ResponseEntity with a list of all Veiculos.
     */
    @GetMapping
    public ResponseEntity<Page<Veiculo>> getAllRequest(Pageable pageable) {
        return ResponseEntity.ok(this.veiculoService.listAll(pageable));
    }
    /**
     * Retrieves active Veiculos.
     *
     * @return ResponseEntity with a list of active Veiculos.
     */
    @GetMapping("/ativos")
    public ResponseEntity<?> findVeiculosAtivos() {
        List<Veiculo> veiculoList = veiculoRepository.findAllAtivo();
        if (veiculoList == null || veiculoList.isEmpty()) {
            return ResponseEntity.badRequest().body("Não tem nenhum veiculo ativo");
        } else {
            return ResponseEntity.ok(veiculoList);
        }
    }

    /**
     * Registers a new Veiculo.
     *
     * @param veiculo The Veiculo object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerVeiculo(@RequestBody @Validated final Veiculo veiculo) {
        try {
            veiculoService.validarCadastroVeiculo(veiculo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Veiculo.
     *
     * @param veiculo The updated Veiculo object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarVeiculo(@RequestBody @Validated final Veiculo veiculo) {
        try {
            veiculoService.validarUpdateVeiculo(veiculo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Deletes a Veiculo by ID.
     *
     * @param id The ID of the Veiculo to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirCondutor(@RequestParam("id") final Long id) {
        try {
            veiculoService.validarDeleteVeiculo(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

}
