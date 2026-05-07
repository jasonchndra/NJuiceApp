package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

public class Add {
	Connect connect = Connect.getInstance();
	private List<CartItem> cartItems;
	private ListView<String> cartList = new ListView<>();
	private User user;
	private String tempJuice;
	private String tempUser;
	Stage primaryStage = new Stage();

	// Untuk membuat Layout
	StackPane popUp = new StackPane();
	BorderPane bp = new BorderPane();
	Scene scene = new Scene(bp, 800, 600);

	GridPane gp = new GridPane();

	// Button
	Button addItem;

	// Dropdown - Juice
	ComboBox<String> juiceBox;

	// Spinner - Quantity
	Spinner<Integer> qtySpinner;
	SpinnerValueFactory<Integer> qtyFactory;

	// Label
	Label juiceLabel, juicePriceLabel, descLabel, qtyLabel, totalPriceLabel;

	String Username;

	// HBox
	HBox hb = new HBox();
	HBox hb2 = new HBox();
	HBox hb3 = new HBox();

	// VBox
	VBox vb = new VBox();

	// Window
	Window popUpWindow = new Window();

	ObservableList<CartItem> cart;
	Stage homeStage;
	Vector<CartItem> cartList2 = new Vector<>();

	public Add(Stage home, User user) {
		this.homeStage = home;
		this.user = user;
		this.Username = user.getUsername();
	}

	public void init() {
		// Label
		juiceLabel = new Label("Juice:");
		juicePriceLabel = new Label("Price: ");
		descLabel = new Label("Description:");
		qtyLabel = new Label("Quantity");
		totalPriceLabel = new Label("Total Price: ");

		// Button
		addItem = new Button("Add Item");

		// Spinner - Quantity
		qtySpinner = new Spinner<>();
		qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

		juiceBox = new ComboBox<>();
		List<String> juiceNames = connect.getJuiceNames();
		juiceBox.getItems().addAll(juiceNames);
		juiceBox.setMaxWidth(200);

		descLabel.setStyle("-fx-font-size: 15px;");
		descLabel.setAlignment(Pos.CENTER);
		descLabel.setWrapText(true);

		// Set event handler untuk menampilkan popUp Window saat item dipilih
		add();
	}

	void setArrangements() {
		hb.getChildren().add(juiceLabel);

		// HBox untuk menampung juiceBox dan juicePriceLabel
		HBox juiceBoxContainer = new HBox();
		juiceBoxContainer.getChildren().addAll(juiceBox, juicePriceLabel);
		hb2.getChildren().add(juiceBoxContainer);

		descLabel.setMaxSize(400, 200);
		hb3.getChildren().add(descLabel);
		gp.add(hb, 0, 0);
		gp.add(hb2, 0, 1);
		gp.add(hb3, 0, 2);

		vb.getChildren().addAll(qtyLabel, qtySpinner, totalPriceLabel, addItem);
		gp.add(vb, 0, 3);

		hb2.setAlignment(Pos.CENTER);
		hb.setAlignment(Pos.CENTER);
		hb3.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		vb.setAlignment(Pos.CENTER);

		hb2.setMargin(juicePriceLabel, new Insets(0, 0, 0, 15));
		vb.setSpacing(5);
		VBox.setMargin(qtyLabel, new Insets(5, 0, 0, 0));
		HBox.setMargin(juiceBoxContainer, new Insets(10, 0, 0, 0));
		HBox.setMargin(descLabel, new Insets(10, 0, 0, 0));

		gp.setHgap(10);
		gp.setVgap(10);
		bp.setCenter(gp);
	}

	void display() {
		init();
		setArrangements();

		popUpWindow.setTitle("Add new item");
		popUpWindow.getContentPane().setStyle("-fx-background-color: white;");
		popUpWindow.getContentPane().getChildren().add(bp);

		popUp.getChildren().add(popUpWindow);

		Scene scene1 = new Scene(popUp, 450, 400);
		primaryStage.setScene(scene1);
		primaryStage.show();

	}

	public void updateCart() {
		cartList.getItems().clear();
		cartItems = Connect.getInstance().getUserCartItems(user.getUsername());
		ObservableList<String> items = FXCollections.observableArrayList();
		for (CartItem cartItem : cartItems) {
			items.add(cartItem.getInfo());
		}

		// Untuk setting ObservableList to the cartList
		cartList.setItems(items);
	}

	void addData(String username, String juiceId, Integer quantity) {
		String query = "SELECT COUNT(*) FROM cartdetail WHERE Username = ? AND JuiceId = ?";

		try {
			PreparedStatement checkStatement = connect.prepareStatement(query);
			checkStatement.setString(1, username);
			checkStatement.setString(2, juiceId);

			var resultSet = checkStatement.executeQuery();

			if (resultSet.next()) {
				int count = resultSet.getInt(1);

				if (count > 0) {
					// Kalau juice sudah ada, jadinya update quantity aja
					updateQuantity(username, juiceId, quantity);
				} else {
					// Kalau juice belom ada
					insertNewCartItem(username, juiceId, quantity);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void updateQuantity(String username, String juiceId, Integer quantity) {
		String updateQuery = "UPDATE cartdetail SET Quantity = Quantity + ? WHERE Username = ? AND JuiceId = ?";

		try {
			PreparedStatement updateStatement = connect.prepareStatement(updateQuery);
			updateStatement.setInt(1, quantity);
			updateStatement.setString(2, username);
			updateStatement.setString(3, juiceId);

			updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void fetchJuice(String selectedJuice) {
		String query = "SELECT JuiceId, Price, JuiceDescription FROM msjuice WHERE JuiceName = ?";
		try {
			PreparedStatement preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, selectedJuice);
			var resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				double price = resultSet.getDouble("Price");
				String description = resultSet.getString("JuiceDescription");
				int qty = qtySpinner.getValue();
				double totalPrice = price * qty;
				tempJuice = resultSet.getString("JuiceId");

				// Untuk menampilkan harga di juicePriceLabel dan deskripsi di descLabel
				juicePriceLabel.setText("Price: " + price);
				descLabel.setText(description);
				totalPriceLabel.setText("Total Juice Price: " + totalPrice);
			} else {
				System.out.println("Juice not found in the database: " + selectedJuice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void insertNewCartItem(String username, String juiceId, Integer quantity) {
		String insertQuery = "INSERT INTO cartdetail (Username, JuiceId, Quantity) VALUES (?, ?, ?)";

		try {
			PreparedStatement insertStatement = connect.prepareStatement(insertQuery);

			insertStatement.setString(1, username);
			insertStatement.setString(2, juiceId);
			insertStatement.setInt(3, quantity);

			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void add() {
		juiceBox.setOnAction(e -> {
			String selectedJuice = juiceBox.getValue();
			fetchJuice(selectedJuice);
		});

		qtySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
			String selectedJuice = juiceBox.getValue();
			fetchJuice(selectedJuice);
		});
		addItem.setOnAction(e -> {
			Integer qty = qtySpinner.getValue();
			tempUser = Username;
			if (juiceBox.getValue() == null) {
				return;
			}

			addData(tempUser, tempJuice, qty);
			updateCart();
			new Home(homeStage, user).display();

			popUpWindow.close();
			primaryStage.close();

		});
	}

}