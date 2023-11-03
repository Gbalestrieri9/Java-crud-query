package projeto.com.impacta.cadastro.service;

import projeto.com.impacta.cadastro.Exception.PessoaException;
import projeto.com.impacta.cadastro.model.Pessoa;
import projeto.com.impacta.cadastro.repository.PessoaRepositoryImpl;

import java.util.List;

public class PessoaService {

    private final PessoaRepositoryImpl pessoaRepository;

    public PessoaService(){
        pessoaRepository = new PessoaRepositoryImpl();
    }

    public Pessoa salvar(Pessoa pessoa){
        if (pessoa.getNome() != null && pessoa.getCpf() != null){
            System.out.println("pessoa cadastrada com sucesso");

            return pessoaRepository.save(pessoa);
        }
        throw new PessoaException("não foi possivel salvar o objeto, +" +
                " porque contem dados nulos: " + "Nome:" + pessoa.getNome() + "cpf " + pessoa.getCpf());

    }

    public Pessoa buscarPorIdPessoa(int idPessoa){
        return pessoaRepository.findByIdPessoa(idPessoa);
    }

    public Pessoa buscarPorCpf(String cpf){
        return pessoaRepository.findByCpf(cpf);
    }

    public List<Pessoa> buscarTodos(){
        return pessoaRepository.findByAll();
    }

    public void excluir(int idPessoa) {
        int linhasExcluidas = pessoaRepository.delete(idPessoa);
        if(linhasExcluidas == 0){
            throw new PessoaException("não foi possivel excluir pessoa com o id: " + idPessoa);
        }
    }

    public Pessoa atualizar(Pessoa pessoa) {

        // TODO: 27/10/2023 recuperar o objeto do banco
        Pessoa pessoaEntity = buscarPorIdPessoa(pessoa.getIdPessoa());

        if (pessoaEntity != null) {
            Pessoa pessoaCpf = this.buscarPorCpf(pessoa.getCpf());

            if (pessoaCpf == null || pessoaCpf.getIdPessoa() == pessoaEntity.getIdPessoa()) {
                return pessoaRepository.update(pessoa);
            }
            return pessoaCpf;
        }
        return null;
    }

}
