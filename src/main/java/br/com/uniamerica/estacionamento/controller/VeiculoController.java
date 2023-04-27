/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping("api/veiuclo")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    /* -------------------get by id--------------------------- */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Veiculo veiculo = this.veiculoRepository.findById(id).orElse(null);
        return veiculo == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(veiculo);
    }

    /* -------------------get by all--------------------------- */
    @GetMapping("/all")
    public ResponseEntity<?> findByAllRequest() {
        return ResponseEntity.ok(this.veiculoRepository.findAll());
    }

    /* -------------------get by ativo--------------------------- */
    @GetMapping("/ativos")
    public ResponseEntity<?> findVeiculosAtivas() {
        return ResponseEntity.ok(this.veiculoRepository.findAllByActive(true));
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerVeiculos(@RequestBody final Veiculo veiculo) {
        try {
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarVeiculo(
            @RequestParam("id") final Long id,
            @RequestBody final Veiculo veiculo
    ) {

        try {
            final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);
            if (veiculoBanco == null || veiculoBanco.getId().equals(veiculo.getId())) {
                throw new RuntimeException("Nao foi possivel identificar o registro informado");
            }
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getCause().getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }

    }

    /* -------------------delete--------------------------- */

}
