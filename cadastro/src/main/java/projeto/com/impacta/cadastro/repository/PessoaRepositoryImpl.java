package projeto.com.impacta.cadastro.repository;

import projeto.com.impacta.cadastro.Exception.PessoaException;
import projeto.com.impacta.cadastro.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepositoryImpl  implements  PessoaRepository{

    @Override
    public Pessoa save(Pessoa pessoa) {

        String query = "INSERT INTO impacta.pessoa (nome, cpf) VALUES (? ,?);";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, pessoa.getNome());
            preparedStatement.setString(2, pessoa.getCpf());

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("salvo com sucesso...");
                ResultSet primaryKeys = preparedStatement.getGeneratedKeys();
                if (primaryKeys.next()){
                    int idPessoa = primaryKeys.getInt(1);
                    return new Pessoa(idPessoa,pessoa.getNome(), pessoa.getCpf());
                }
            }
            throw new PessoaException("Não foi possivel salvar objeto: " + pessoa);
        } catch (SQLException | PessoaException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Pessoa findByIdPessoa(int idPessoa) {

        String query = "select * from impacta.pessoa where idPessoa = ?";

        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1,idPessoa);

            ResultSet resultSet = preparedStatement.executeQuery();

            return getPessoa (resultSet);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Pessoa findByCpf(String cpf) {

        String query = "SELECT * FROM impacta.pessoa where cpf = ?";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cpf);

            ResultSet resultSet = preparedStatement.executeQuery();

            return getPessoa(resultSet);

        }catch (SQLException e ){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pessoa> findByNome(String nome) {
        return null;
    }

    @Override
    public List<Pessoa> findByAll() {

        List<Pessoa> pessoas = new ArrayList<>();

        String query = "SELECT * FROM impacta.pessoa";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);

            boolean isNotNullpessoa;
            do {
                Pessoa pessoa = getPessoa(resultSet);
                isNotNullpessoa = pessoa != null;
                if (isNotNullpessoa){
                    pessoas.add(pessoa);
                }
            }while (isNotNullpessoa);



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pessoas;
    }

    @Override
    public Pessoa update(Pessoa pessoa) {

        String query = "update impacta.pessoa set nome = ?, cpf = ?  where (idpessoa = ?)";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1,pessoa.getNome());
            preparedStatement.setString(2,pessoa.getCpf());
            preparedStatement.setInt(3,pessoa.getIdPessoa());

            if (preparedStatement.executeUpdate() > 0){
                return findByIdPessoa(pessoa.getIdPessoa());
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(int idPessoa) {

        String query = "delete from impacta.pessoa where idpessoa = ?;";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

                preparedStatement.setInt(1,idPessoa);

                int linhasExcluidas = preparedStatement.executeUpdate();
                if (linhasExcluidas > 0){
                    return linhasExcluidas;
                }

        }catch (SQLException e){
                e.printStackTrace();
        }


        return 0;
    }

    private Connection getConnection() throws SQLException {

        String usuario = "root";
        String senha = "root";
        String urlconexao = "jdbc:mysql://localhost:3306/impacta?useTimezone=true&serverTimezone=UTC";

        return DriverManager.getConnection(urlconexao,usuario,senha);

    }

    private Pessoa getPessoa(ResultSet resultSet) throws SQLException{
        if (resultSet.next()){
            int idPessoa = resultSet.getInt("idpessoa");
            String nome = resultSet.getString("nome");
            String cpf = resultSet.getString("cpf");

            return new Pessoa(idPessoa,nome,cpf);
        }
        return null;
    }
}
