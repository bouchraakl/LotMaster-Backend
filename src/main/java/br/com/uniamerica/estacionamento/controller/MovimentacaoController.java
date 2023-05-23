package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    /**
     * Retrieves a Movimentacao by ID.
     *
     * @param id The ID of the Movimentacao to retrieve.
     * @return ResponseEntity with the Movimentacao if found, otherwise a bad request response.
     */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Movimentacao movimentacao = movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("ID não encontrado") : ResponseEntity.ok(movimentacao);
    }

    /**
     * Retrieves all Movimentacoes.
     *
     * @return ResponseEntity with a list of all Movimentacoes.
     */
    @GetMapping("/all")
    public ResponseEntity<?> findAllRequest() {
        return ResponseEntity.ok(movimentacaoRepository.findAll());
    }

    /**
     * Retrieves open Movimentacoes.
     *
     * @return ResponseEntity with a list of open Movimentacoes.
     */
    @GetMapping("/abertas")
    public ResponseEntity<?> findMovimentacoesAbertas() {
        return ResponseEntity.ok(movimentacaoRepository.findAllAbertas());
    }

    /**
     * Registers a new Movimentacao.
     *
     * @param movimentacao The Movimentacao object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerMovimentacao(@RequestBody @Validated final Movimentacao movimentacao) {
        try {
            movimentacaoService.validarCadastroMovimentacao(movimentacao);
            return ResponseEntity.ok("Registro Cadastrado com Sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * Updates an existing Movimentacao.
     *
     * @param movimentacao The updated Movimentacao object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarMovimentacao(@RequestBody @Validated final Movimentacao movimentacao) {
        try {
            movimentacaoService.validarUpdateMovimentacao(movimentacao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Deletes a Movimentacao by ID.
     *
     * @param id The ID of the Movimentacao to delete.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @DeleteMapping
    public ResponseEntity<?> excluirCondutor(@RequestParam("id") final Long id) {
        try {
            movimentacaoService.validarDeleteMovimentacao(id);
            return ResponseEntity.ok("Registro apagado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
