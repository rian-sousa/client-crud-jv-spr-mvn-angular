package javinha.rsc.clientes.rest;

import jakarta.validation.Valid;
import javinha.rsc.clientes.model.auxiliary.Mensagem;
import javinha.rsc.clientes.model.entity.Cliente;
import javinha.rsc.clientes.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin("http://localhost:4200")
public class ClienteController {

    private final ClienteRepository repository;

    @Autowired
    public ClienteController(ClienteRepository repository){
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar( @RequestBody @Valid Cliente cliente){
        return repository.save(cliente);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente acharPorId(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));  //Se não encontrar, vai retornar status 404
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCliente(@PathVariable Integer id){
        repository
                .findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return  Void.TYPE; //Para caso não seja encontrado o cliente, será retornado void
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void atualizarCliente(@PathVariable Integer id, @RequestBody @Valid Cliente clienteAtualizado){
        repository
                .findById(id)
                .map(cliente -> {
//                    clienteAtualizado.setId(cliente.getId()); //manter o id anterior
//                    clienteAtualizado.setDataCadastro(cliente.getDataCadastro()); //manter a data de cadastro
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setCpf(clienteAtualizado.getCpf());
                    return repository.save(cliente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
}
