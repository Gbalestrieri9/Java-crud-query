package projeto.com.impacta.cadastro.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import projeto.com.impacta.cadastro.model.Pessoa;
import projeto.com.impacta.cadastro.service.PessoaService;

import java.net.URL;
import java.util.ResourceBundle;

public class pessoaController implements Initializable {

    @FXML
    private TextField ImputCpf;

    @FXML
    private Button buttonExcluir;

    @FXML
    private Button buttoonAdicionar;

    @FXML
    private Button buttoonAlterar;

    @FXML
    private TableColumn<Pessoa,String> columnCpf;

    @FXML
    private TableColumn<Pessoa,String> columnNome;

    @FXML
    private TableColumn<Pessoa,String> columnidPessoa;

    @FXML
    private TableView<Pessoa> table;

    @FXML
    private TextField imputNome;

    PessoaService pessoaService;
    int indexTabela;
    int idPessoa;

    @FXML
    void Adicionar(ActionEvent event) {
        atualizarTabela();

        String nome = imputNome.getText();
        String cpf = ImputCpf.getText();

        Pessoa pessoa = new Pessoa(nome,cpf);
        Pessoa pessoaEntity = pessoaService.salvar(pessoa);

        Alert alert;
        if (pessoaEntity != null){
            atualizarTabela();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Cadastro");
            alert.setTitle("Cadastro ok");
            alert.setContentText("Cadastro com sucesso");
            alert.showAndWait();
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Cadastro");
            alert.setTitle("Cadastro nok");
            alert.setContentText("Cadastro não realizado");
            alert.showAndWait();

        }

    }

    @FXML
    void Alterar(ActionEvent event) {
        atualizarTabela();
        String nome = imputNome.getText();
        String cpf = ImputCpf.getText();

        Pessoa pessoa = new Pessoa(idPessoa,nome,cpf);
        Pessoa pessoaEntity = pessoaService.salvar(pessoa);

        Alert alert;
        if (pessoaEntity != null){
            atualizarTabela();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Alteração");
            alert.setTitle("Alteração ok");
            alert.setContentText("Alteração com sucesso");
            alert.showAndWait();
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Alteração");
            alert.setTitle("Alteração nok");
            alert.setContentText("Alteração não realizado");
            alert.showAndWait();

        }

    }

    @FXML
    void Excluir(ActionEvent event) {
        System.out.println("excluindo");

        pessoaService.excluir(idPessoa);

        atualizarTabela();

    }

    void atualizarTabela() {
        try {
            ObservableList<Pessoa> pessoas = FXCollections.observableArrayList();
            pessoas.addAll(pessoaService.buscarTodos());

            table.setItems(pessoas);

            columnidPessoa.setCellValueFactory(pessoa -> {
                return new SimpleStringProperty(String.valueOf(pessoa.getValue().getIdPessoa()));
            });

            columnNome.setCellValueFactory(pessoa -> {
                return new SimpleStringProperty(pessoa.getValue().getNome());
            });

            columnCpf.setCellValueFactory(pessoa -> {
                return new SimpleStringProperty(pessoa.getValue().getCpf());
            });

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    void linhaSelecionadaTabela() {
        table.setRowFactory(pessoaTableView -> {
            TableRow<Pessoa> linha = new TableRow<>();
            linha.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 1 && !linha.isEmpty()) {

                    indexTabela = table.getSelectionModel().getSelectedIndex();

                    idPessoa = table.getItems().get(indexTabela).getIdPessoa();

                    String nome = table.getItems().get(indexTabela).getNome();
                    imputNome.setText(nome);

                    String cpf = table.getItems().get(indexTabela).getCpf();
                    ImputCpf.setText(cpf);

                    System.out.printf(idPessoa + nome + cpf);
                }
            });
            return linha;
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pessoaService = new PessoaService();
        atualizarTabela();
    }
}
