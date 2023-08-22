package com.example.controleprodutosms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.controleprodutosms.model.Produto;
import com.example.controleprodutosms.repository.ProdutoRepository;

@Service
public class ProdutoServiceImpl implements ProdutoService{
    
    @Autowired
    private ProdutoRepository  repo;


    @Override
    public Produto criaProduto(Produto produto) {
        return salvaProduto(produto);
    }





    private Produto salvaProduto (Produto produto) {
        ModelMapper mapper = new ModelMapper();
        Produto produtoEntidade = mapper.map(produto, Produto.class);
        produtoEntidade = repo.save(produtoEntidade);

        return mapper.map(produtoEntidade, Produto.class);
    }





    @Override
    public List<Produto> obterTodos() {
        List<Produto> produtos = repo.findAll();

        return produtos.stream()
            .map(produto -> new ModelMapper().map(produto, Produto.class))
            .collect(Collectors.toList());
    }





    @Override
    public Optional<Produto> obterPorId(Integer id) {
       Optional<Produto> produto = repo.findById(id);

       if(produto.isPresent()) {
        Produto dto = new ModelMapper().map(produto.get(), Produto.class);

        return Optional.of(dto);
       }
    return Optional.empty();
    }

 @Override
    public Optional<Produto> atualizarProduto(Integer id, Produto produto) {

        Optional<Produto> produtoSearch = repo.findById(id);
    
       if(produtoSearch.isPresent()) {
        produto.setId(id);
       return Optional.of(salvaProduto(produto));
       } 

       return Optional.empty();
       
    }



    @Override
    public void removerProduto(Integer id) {
       repo.deleteById(id);
    }


   

    
}
