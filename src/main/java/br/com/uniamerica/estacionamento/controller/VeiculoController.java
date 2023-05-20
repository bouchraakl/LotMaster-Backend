/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* ----------------------------------------------------- */
@RestController
@RequestMapping(value = "api/veiculo")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

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
        List<Veiculo> veiculoList = this.veiculoRepository.findAllAtivo();
        if (veiculoList == null || veiculoList.isEmpty()) {
            return ResponseEntity.badRequest().body("Não tem nem um veiculo ativo");
        } else {
            return ResponseEntity.ok(veiculoList);
        }
    }

    /* -------------------post--------------------------- */
    @PostMapping
    public ResponseEntity<?> registerVeiculos(@RequestBody @Validated final Veiculo veiculo) {
        try {
            this.veiculoService.validarCadastroVeiculo(veiculo);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* -------------------put--------------------------- */
    @PutMapping
    public ResponseEntity<?> editarVeiculo(
            @RequestBody @Validated final Veiculo veiculo
    ) {

        try {
            this.veiculoService.validarUpdateVeiculo(veiculo);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    /* -------------------delete--------------------------- */
    @DeleteMapping
    public ResponseEntity<?> exluirVeiculo(@RequestParam("id") final Long id) {
        try {
            this.veiculoService.validarDeleteVeiculo(id);
            final Veiculo veiculoBanco = this.veiculoRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));
            if (!this.movimentacaoRepository.findByCondutorId(id).isEmpty()) {
                veiculoBanco.setAtivo(false);
                this.veiculoRepository.save(veiculoBanco);
                return ResponseEntity.ok("Registro Desativado com sucesso!");
            } else {
                this.veiculoRepository.delete(veiculoBanco);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
