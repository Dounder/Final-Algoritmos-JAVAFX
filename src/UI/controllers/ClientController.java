package UI.controllers;

import database.dao.ClienteDao;
import database.models.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private TableView<Cliente> tableClients;

    @FXML
    private TableColumn<Cliente, Integer> id;

    @FXML
    private TableColumn<Cliente, String> name;

    @FXML
    private TableColumn<Cliente, String> lastName;

    @FXML
    private TableColumn<Cliente, String> nit;

    @FXML
    private TableColumn<Cliente, String> direction;

    @FXML
    private TableColumn<Cliente, String> phone;

    @FXML
    private TextField idInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField nitInput;

    @FXML
    private TextArea directionInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private Button newButton;

    @FXML
    private Button createButton;

    List<Cliente> clienteList = database.service.ClienteService.listClient();
    ObservableList<Cliente> observableClientList = FXCollections.observableArrayList();


    public void addClientsToObservableList(){
        observableClientList.addAll(clienteList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idInput.setDisable(true);
        newButton.setDisable(true);
        createButton.setDisable(false);
        addClientsToObservableList();
        this.id.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("idCliente"));
        this.nit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
        this.name.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        this.lastName.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
        this.direction.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
        this.phone.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));

        tableClients.setItems(observableClientList);
    }


    public void rowSelected(MouseEvent mouseEvent) {
        newButton.setDisable(false);
        createButton.setDisable(true);
        Cliente cliente = tableClients.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            idInput.setText(Integer.toString(cliente.getIdCliente()));
            nameInput.setText(cliente.getNombre());
            lastNameInput.setText(cliente.getApellido());
            nitInput.setText(cliente.getNit());
            directionInput.setText(cliente.getDireccion());
            phoneInput.setText(cliente.getTelefono());
        }
    }

    public Cliente getClientInfo() {
        Cliente cliente = new Cliente();
        try {
            cliente.setIdCliente(0);
            cliente.setNit(nitInput.getText());
            cliente.setNombre(nameInput.getText());
            cliente.setApellido(lastNameInput.getText());
            cliente.setDireccion(directionInput.getText());
            cliente.setTelefono(phoneInput.getText());
        } catch (NumberFormatException error) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(error + " informacion del cliente incorrecta o vacia");
            alert.showAndWait();
            cliente = null;
        }
        return cliente;
    }

    public void createClient(MouseEvent mouseEvent) {
        Cliente cliente = getClientInfo();
        if (cliente != null) {
            database.service.ClienteService.createClient(cliente);
            cliente.setIdCliente(ClienteDao.idClient);
            observableClientList.add(cliente);
            tableClients.setItems(observableClientList);
            newClient();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Informacion");
            alert.setContentText("Cliente creado correctamente");
            alert.showAndWait();
        }
    }

    public void updateClient(MouseEvent mouseEvent) {
        Cliente cliente = tableClients.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Ningun cliente seleccionado");
            alert.showAndWait();
        } else {
            Cliente clienteAux = getClientInfo();
            clienteAux.setIdCliente(Integer.parseInt(idInput.getText()));
            if (!observableClientList.contains(clienteAux)) {
                cliente.setIdCliente(clienteAux.getIdCliente());
                cliente.setNombre(clienteAux.getNombre());
                cliente.setApellido(clienteAux.getApellido());
                cliente.setNit(clienteAux.getNit());
                cliente.setDireccion(clienteAux.getDireccion());
                cliente.setTelefono(clienteAux.getTelefono());

                database.service.ClienteService.updateClient(cliente);
                tableClients.refresh();
                newClient();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Informacion");
                alert.setContentText("Cliente modificada correctamente" +
                        "\nId modificado: " + cliente.getIdCliente());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("La persona seleccionada no ha sido modificada");
                alert.showAndWait();
            }
        }
    }

    public void deleteClient(MouseEvent mouseEvent) {
        Cliente cliente = tableClients.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Ningun cliente seleccionado");
            alert.showAndWait();
        } else {
            database.service.ClienteService.deleteClient(cliente.getNit());
            observableClientList.remove(cliente);
            tableClients.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Informacion");
            alert.setContentText("Cliente eliminado correctamente");
            alert.showAndWait();
        }
    }

    public void newClient() {
        createButton.setDisable(false);
        newButton.setDisable(true);
        idInput.setText("");
        nameInput.setText("");
        lastNameInput.setText("");
        nitInput.setText("");
        directionInput.setText("");
        phoneInput.setText("");
    }
}
