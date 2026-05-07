package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminManage implements EventHandler<ActionEvent> {

	ObservableList<JuiceInfo> juiceObj = FXCollections.observableArrayList();

	Connect connect = Connect.getInstance();

	Stage primaryStage;

	BorderPane bp = new BorderPane();
	BorderPane bp2 = new BorderPane();
	Scene scene = new Scene(bp, 800, 600);

	TableView<JuiceInfo> juiceTable = new TableView<>();

	GridPane gp = new GridPane();
	GridPane gp2 = new GridPane();

	MenuBar mb = new MenuBar();

	Menu menu1 = new Menu("Admin's Dashboard");
	Menu menu2 = new Menu("Logout");

	MenuItem viewTransaction = new MenuItem("View Transaction");
	MenuItem manageProducts = new MenuItem("Manage Products");
	MenuItem logout = new MenuItem("Logout from admin");

	Label title = new Label("Manage Products");
	Label productId = new Label("Product ID           to delete/remove:");
	Label price = new Label("Price:");
	Label productName = new Label("Product Name: ");

	HBox hb1 = new HBox();
	HBox hb2 = new HBox();
	HBox hb3 = new HBox();
	HBox hb4 = new HBox();
	HBox hb5 = new HBox();

	VBox vb = new VBox();
	VBox vb2 = new VBox();

	private ComboBox<String> product;

	TextField productNm = new TextField();

	TextArea desc = new TextArea();

	Spinner<Integer> priceSp;

	Alert emptyField = new Alert(AlertType.ERROR);

	Button insert = new Button("Insert Juice");
	Button update = new Button("Update Price");
	Button remove = new Button("Remove Juice");

	TableColumn<JuiceInfo, String> juiceIdCol, juiceNameCol, juiceDescriptionCol;
	TableColumn<JuiceInfo, Integer> juicePriceCol;

	// SQL
	ArrayList<JuiceInfo> juiceList = new ArrayList<JuiceInfo>();

	public void initializeMenuBar() {
		menu1.getItems().addAll(viewTransaction, manageProducts);
		menu2.getItems().addAll(logout);
		mb.getMenus().addAll(menu1, menu2);

		emptyField.setHeaderText("Error");
		emptyField.setContentText("Please fill all the fields");
	}

	public void initializeTable() {
		hb1.getChildren().add(title);

		juiceTable = new TableView<JuiceInfo>();
		juiceIdCol = new TableColumn<>("Juice ID");
		juiceNameCol = new TableColumn<>("Juice Name");
		juicePriceCol = new TableColumn<>("Price");
		juiceDescriptionCol = new TableColumn<>("Juice Description");

		juiceIdCol.setCellValueFactory(new PropertyValueFactory<JuiceInfo, String>("JuiceId"));
		juiceNameCol.setCellValueFactory(new PropertyValueFactory<JuiceInfo, String>("JuiceName"));
		juicePriceCol.setCellValueFactory(new PropertyValueFactory<JuiceInfo, Integer>("Price"));
		juiceDescriptionCol.setCellValueFactory(new PropertyValueFactory<JuiceInfo, String>("JuiceDescription"));
		juiceTable.getColumns().addAll(juiceIdCol, juiceNameCol, juicePriceCol, juiceDescriptionCol);

		juiceIdCol.setMinWidth(80);
		juiceNameCol.setMinWidth(140);
		juicePriceCol.setMinWidth(80);
		juiceDescriptionCol.setMinWidth(200);

		juiceTable.setMaxWidth(500);
		juiceTable.setMaxHeight(280);

		juiceTable.setItems(juiceObj);

		gp.add(hb1, 0, 0);
		gp.setMargin(hb1, new Insets(5, 0, 15, 0));

		gp.add(juiceTable, 0, 2);

		// juiceId
		product = new ComboBox<>();

	}

	public void update() {
		productId.setWrapText(true);
		productId.setPrefSize(100, 60);
		hb2.getChildren().addAll(productId, product);
		hb2.setMargin(product, new Insets(10, 0, 0, 27));
		priceSp = new Spinner<>();
		priceSp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10000, 1000000000, 10000));
		price.setPrefSize(100, 0);
		hb3.getChildren().addAll(price, priceSp);
		hb3.setMargin(priceSp, new Insets(10, 0, 0, 27));

		productNm.setPromptText("Insert product name to be created");
		productNm.setPrefSize(350, 10);
		hb4.getChildren().addAll(productName, productNm);
		hb4.setMargin(productName, new Insets(10, 0, 0, 0));
		hb4.setMargin(productNm, new Insets(10, 0, 0, 43));

		desc.setPrefSize(350, 80);
		hb5.getChildren().add(desc);
		desc.setPromptText("Insert the new product's text description, min. 10 & max.100");
		hb5.setMargin(desc, new Insets(10, 0, 0, 100));

		vb.getChildren().addAll(insert, update, remove);
		vb.setPadding(new Insets(30, 0, 0, 0));
		vb2.getChildren().addAll(hb2, hb3, hb4, hb5);
		gp.add(vb2, 0, 3);
		gp.add(vb, 1, 3);
		hb5.setAlignment(Pos.CENTER);

		vb.setSpacing(10);
	}

	public void eventHandler() {
		insert.setOnAction(this);
		update.setOnAction(this);
		remove.setOnAction(this);
	}

	public void arrangement() {

		bp.setTop(mb);
		bp.setCenter(gp);
		bp.setBottom(gp2);

		hb1.setMargin(title, new Insets(10, 0, 0, 0));

		hb2.setAlignment(Pos.CENTER_LEFT);
		hb1.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.TOP_CENTER);

	}

	public void style() {
		title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		insert.setPrefSize(80, 30);
		update.setPrefSize(85, 30);
		remove.setPrefSize(90, 30);
	}

	public void setEvent() {
		logout.setOnAction(e -> {
			Login login = new Login(primaryStage);
		});

		viewTransaction.setOnAction(this);
	}

	private void getJuiceData() {
		juiceTable.getItems().clear();

		try {
			String query = "SELECT * FROM msjuice";
			connect.runQuery(query);
			ResultSet resultSet = connect.getResultSet();

			while (resultSet.next()) {
				String Juiceid = resultSet.getString("JuiceId");
				String JuiceName = resultSet.getString("JuiceName");
				Integer price = resultSet.getInt("Price");
				String Description = resultSet.getString("JuiceDescription");

				juiceObj.add(new JuiceInfo(Juiceid, JuiceName, price, Description));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		juiceTable.setItems(juiceObj);
	}

	public void buttons() {
		insert.setOnAction(event -> {
			if (validateJuiceInput()) {
				String newJuiceId = generateAndInsertNewJuiceId();
				if (newJuiceId != null) {
					insertJuiceIntoDatabase(newJuiceId);
					refreshComboBox();
					getJuiceData();
				}
			}
		});

		update.setOnAction(event -> {
			if (validateUpdate()) {
				String selectedJuiceId = product.getValue();
				if (selectedJuiceId != null) {
					updateJuiceInDatabase(selectedJuiceId);
					getJuiceData();
				} else {
					displayError("Missing Juice ID", "Please select a Juice ID to update.");

					System.out.println("Please select a Juice ID to update.");
				}
			}
		});

		remove.setOnAction(event -> {
			if (validateDelete()) {
				String selectedJuiceId = product.getValue();
				deleteJuiceFromDatabase(selectedJuiceId);
				getJuiceData();
			} else {
				displayError("No Juice ID Selected", "Please select a Juice ID to remove.");

			}
		});
	}

	private boolean validateJuiceInput() {
		String productName = productNm.getText();
		String description = desc.getText();

		int priceText = priceSp.getValue();

		if (productName.isEmpty() || description.isEmpty()) {
			displayError("Missing Fields", "Please fill in all the required fields.");
			return false;
		} else if (priceText < 10000) {
			displayError("Invalid Price", "Price must be above 10000.");
			return false;
		} else if (description.length() < 10 || description.length() > 100) {
			displayError("Invalid Description Length", "Description length should be between 10 and 100 characters.");
			return false;

		} else {
			return true;
		}
	}

	private void insertJuiceIntoDatabase(String newJuiceId) {
		String productName = productNm.getText();
		String productDescription = desc.getText();
		int priceText = priceSp.getValue();

		String insertQuery = String.format(
				"INSERT INTO msjuice (JuiceId, JuiceName, JuiceDescription, Price) VALUES ('%s', '%s', '%s', %d)",
				newJuiceId, productName, productDescription, priceText);
		connect.runUpdate(insertQuery);
	}

	private void displayError(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();

	}

	private boolean validateUpdate() {
		int priceText = priceSp.getValue();
		String selectedJuiceId = product.getValue();

		if (priceText < 10000) {
			System.out.println("Price must be above 10000 for update.");
			return false;
		} else if (selectedJuiceId == null || selectedJuiceId.isEmpty()) {
			System.out.println("Please select a Juice ID for update.");
			displayError("Missing Juice ID", "Please select a Juice ID to update.");
			return false;
		}

		return true;
	}

	private void updateJuiceInDatabase(String selectedJuiceId) {
		int priceText = priceSp.getValue();

		String updateQuery = String.format("UPDATE msjuice SET Price = %d WHERE JuiceId = '%s'", priceText,
				selectedJuiceId);
		connect.runUpdate(updateQuery);
	}

	private boolean validateDelete() {
		String selectedJuiceId = product.getValue();

		if (selectedJuiceId == null || selectedJuiceId.isEmpty()) {
			System.out.println("Please select a Juice ID to delete.");
			displayError("No Juice ID Selected", "Please select a Juice ID to remove.");
			return false;
		}

		return true;
	}

	private void deleteJuiceFromDatabase(String selectedJuiceId) {
		String deleteQuery = "DELETE FROM msjuice WHERE JuiceId = '" + selectedJuiceId + "'";
		connect.runUpdate(deleteQuery);
	}

	public void getJuiceid() {
		String query = "SELECT JuiceId FROM msjuice";
		connect.runQuery(query);
		ResultSet resultSet = connect.getResultSet();

		try {
			while (resultSet.next()) {
				String juiceName = resultSet.getString("JuiceId");
				if (product != null) {
					product.getItems().add(juiceName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getLatestJuiceIdFromDatabase() {
		String latestJuiceId = null;

		try {
			String query = "SELECT JuiceId FROM msjuice ORDER BY JuiceId DESC LIMIT 1";
			connect.runQuery(query);

			ResultSet resultSet = connect.getResultSet();

			if (resultSet.next()) {
				latestJuiceId = resultSet.getString("JuiceId");
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return latestJuiceId;
	}

	private String generateNextJuiceId(String latestJuiceId) {
		String newJuiceId = null;

		if (latestJuiceId != null && latestJuiceId.matches("JU\\d{3}")) {
			String numericPart = latestJuiceId.substring(2);
			int numericValue = Integer.parseInt(numericPart);
			numericValue++;

			newJuiceId = "JU" + String.format("%03d", numericValue);
		}

		return newJuiceId;
	}

	private String generateAndInsertNewJuiceId() {
		String latestJuiceId = getLatestJuiceIdFromDatabase();
		String newJuiceId = null;

		if (latestJuiceId != null) {
			newJuiceId = generateNextJuiceId(latestJuiceId);
		}

		return newJuiceId;
	}

	private void refreshComboBox() {
		product.getItems().clear();
		getJuiceid();
	}

	public AdminManage(Stage primaryStage) {
		initializeMenuBar();
		initializeTable();
		arrangement();

		getJuiceid();
		buttons();
		update();
		style();
		setEvent();
		getJuiceData();

		primaryStage.setScene(scene);
		this.primaryStage = primaryStage;
	}

	public void show() {
		primaryStage.show();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == viewTransaction) {
			ViewTransaction vw = new ViewTransaction(primaryStage);
			vw.show();
		}
	}
}