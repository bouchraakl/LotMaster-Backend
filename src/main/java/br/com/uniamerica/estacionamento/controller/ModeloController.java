/* -------------------Package--------------------------- */
package br.com.uniamerica.estacionamento.controller;

/* -------------------Imports--------------------------- */

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* ----------------------------------------------------- */
@Controller
@RequestMapping(value = "/api/modelo")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

//    public ModeloController(ModeloRepository modeloRepository){
//        this.modeloRepository = modeloRepository;
//    }

    /*
    modelo/id/1
    */
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> findByIdPath(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(new Modelo());
    }


    /*
    modelo?id=1&...
    */
    @GetMapping
    public ResponseEntity<Modelo> findByIdRequest(@RequestParam("id") final Long id) {
        return ResponseEntity.ok(new Modelo());
    }


//    @PostMapping
//    @PutMapping
//    @DeleteMapping
}
