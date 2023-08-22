package com.example.controleprodutosms.view.controler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.controleprodutosms.model.Produto;
import com.example.controleprodutosms.model.exception.ResourceNotFoundException;
import com.example.controleprodutosms.service.ProdutoService;


@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping(value = "/status")
    public String statusServico(@Value("${local.server.port}") String porta) {
        return String.format("Serviço ativo e executando na porta %s", porta);
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        ModelMapper mapper = new ModelMapper();
        Produto dto = mapper.map(produto, Produto.class);
        dto = service.criaProduto(dto);
        return new ResponseEntity<>(mapper.map(dto, Produto.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> obterTodos() {
        List<Produto> dtos = service.obterTodos();

        if(dtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        ModelMapper mapper = new ModelMapper();
        List<Produto> resp = dtos.stream()
                    .map(dto -> mapper.map(dto, Produto.class))
                    .collect(Collectors.toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Produto> obterPorId(@PathVariable Integer id) {
        Optional<Produto> produto = service.obterPorId(id);

        if(produto.isPresent()) {
            return new ResponseEntity<Produto>(
                new ModelMapper().map(produto.get(), 
                Produto.class), HttpStatus.OK
            );
        }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Integer id,
        @Valid @RequestBody Produto produto) {
            Optional<Produto> dto = service.atualizarProduto(id, produto);

            if(dto.isEmpty()) {
                throw new ResourceNotFoundException("Produto não encontrado!");
            }
            return new ResponseEntity<Produto>(
                new ModelMapper().map(dto.get(), Produto.class), HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Integer id) {
        service.removerProduto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
