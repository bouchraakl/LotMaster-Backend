package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.service.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private ConfiguracaoService configuracaoService;

    /**
     * Retrieves a Configuracao by ID.
     *
     * @param id The ID of the Configuracao to retrieve.
     * @return ResponseEntity with the Configuracao if found, otherwise a bad request response.
     */
    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id) {
        final Configuracao configuracao = configuracaoRepository.findById(id).orElse(null);
        return configuracao == null
                ? ResponseEntity.badRequest().body("ID não encontrado")
                : ResponseEntity.ok(configuracao);
    }

    @GetMapping("/last")
    public ResponseEntity<?> getLastRequest() {
        return ResponseEntity.ok(configuracaoRepository.ultimaConfiguracao());
    }

    /**
     * Retrieves all Configuracoes.
     *
     * @return ResponseEntity with a list of all Configuracoes.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllRequest() {
        return ResponseEntity.ok(configuracaoRepository.findAll());
    }

    /**
     * Registers a new Configuracao.
     *
     * @param configuracao The Configuracao object to register.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PostMapping
    public ResponseEntity<?> registerConfiguracao(@RequestBody @Validated final Configuracao configuracao) {
        try {
            configuracaoService.validarCadastroConfiguracao(configuracao);
            return ResponseEntity.ok("Configuracao registrada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates an existing Configuracao.
     *
     * @param configuracao The updated Configuracao object.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @PutMapping
    public ResponseEntity<?> editarConfiguracao(@RequestBody @Validated final Configuracao configuracao) {
        try {
            configuracaoService.validarUpdateConfiguracao(configuracao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
