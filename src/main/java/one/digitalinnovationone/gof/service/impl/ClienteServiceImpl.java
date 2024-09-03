package one.digitalinnovationone.gof.service.impl;

import one.digitalinnovationone.gof.model.Cliente;
import one.digitalinnovationone.gof.model.ClienteRepository;
import one.digitalinnovationone.gof.model.Endereco;
import one.digitalinnovationone.gof.model.EnderecoRepository;
import one.digitalinnovationone.gof.service.ClienteService;
import one.digitalinnovationone.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl extends ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    //@Override
    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }
    //@Override
    public Cliente buscarPorId(Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }
    //@Override
    public void inserir(Cliente cliente){
        salvarCleinteComCep(cliente);

    }
    //@Override
    public void atualizar(Long id, Cliente cliente){
        //Buscar Cliente por ID, caso exista
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()){
            salvarCleinteComCep(cliente);
        }
    }
    //@Override
    public void deletar(Long id){
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);

    }

    private void salvarCleinteComCep(Cliente cliente){
        //Verificar se o Endereço do Cliente já existe pelo CEP
        String cep = cliente.getEndereco().getCep();
        enderecoRepository.findById(cep).orElseGet(() -> {
            //Caso exista, integrar com o ViaCep e persistir o retorno
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        Endereco endereco = null;
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

}
