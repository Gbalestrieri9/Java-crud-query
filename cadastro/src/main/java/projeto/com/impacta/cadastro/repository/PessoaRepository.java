package projeto.com.impacta.cadastro.repository;

import projeto.com.impacta.cadastro.model.Pessoa;

import java.util.List;

public interface PessoaRepository {
    //create
    Pessoa save(Pessoa pessoa);

    //Read
    Pessoa findByIdPessoa(int idPessoa);
    Pessoa findByCpf(String cpf);
    List<Pessoa> findByNome(String nome);
    List<Pessoa> findByAll();

    //update
    Pessoa update(Pessoa pessoa);

    //delete
    int delete(int idPessoa);

}
