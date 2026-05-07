package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustCheckOut {
	private Stage primaryStage;
	private User user;
	private List<CartItem> cartItems;
	private ListView<CartItem> cartList = new ListView<>();
	Connect connect = Connect.getInstance();

	// Layout
	BorderPane bp = new BorderPane();
	Scene scene = new Scene(bp, 800, 600);
	GridPane gp = new GridPane();

	// MenuBar
	MenuBar menuBar = new MenuBar();
	Menu menu = new Menu("Logout");

	// Button
	Button cancel, checkout, logout;

	// Label
	Label paymentTypeLabel = new Label("Payment Type:");
	Label checkOutLabel = new Label("Check Out");
	Label yourCartInfo = new Label("Your cart is empty, try adding items!");
	Label username;

	String tempid;

	// HBox
	HBox hb = new HBox();
	HBox hb1 = new HBox();

	// VBox
	VBox vb = new VBox();
	VBox vb1 = new VBox();

	// Alert
	Alert noJuiceSelected = new Alert(AlertType.ERROR);
	Alert emptyCart = new Alert(AlertType.ERROR);

	// RadioButton
	RadioButton cash, debit, credit;
	ToggleGroup paymentGroup;

	public void initialize() {
		username = new Label("Hi, " + user.getUsername());
		username.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
		checkout = new Button("Checkout");
		logout = new Button("Logout");
		cancel = new Button("Cancel");

		cash = new RadioButton("Cash");
		debit = new RadioButton("Debit");
		credit = new RadioButton("Credit");

		paymentGroup = new ToggleGroup();

		logout.setPrefSize(55, 30);
		Region spacer = new Region();
		spacer.setMinWidth(640);
		ToolBar toolBar = new ToolBar(logout, spacer, username);

		bp.setTop(toolBar);

		noJuiceSelected.setHeaderText("Error");
		noJuiceSelected.setContentText("Please choose which juice to delete");
		emptyCart.setHeaderText("Error");
		emptyCart.setContentText("Your cart is empty");
	}

	void initForm() {
		// masukkin RadioButton paymentType
		cash.setToggleGroup(paymentGroup);
		debit.setToggleGroup(paymentGroup);
		credit.setToggleGroup(paymentGroup);
	}

	public void setArrangements() {
		menuBar.getMenus();

		bp.setCenter(gp);

		checkOutLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold;");
		vb.getChildren().addAll(checkOutLabel);
		if (cartItems.isEmpty()) {
			vb.getChildren().addAll(yourCartInfo);
		} else {
			int total = 0;
			int grandTotal = 0;
			for (CartItem cartItem : cartItems) {
				vb.getChildren()
						.add(new Label(String.format("%dx - %s - [%d xRp. %d,- = Rp. %d,-]", cartItem.getQty(),
								cartItem.getJuiceName(), cartItem.getQty(), cartItem.getJuicePrice(),
								cartItem.getQty() * cartItem.getJuicePrice())));
				total += (cartItem.getQty() * cartItem.getJuicePrice());
			}
			Label totalPriceLabel = new Label("Total price: " + total);
			vb.getChildren().add(totalPriceLabel);
		}
		hb.getChildren().addAll(cancel, checkout);
		hb1.getChildren().addAll(cash, debit, credit);

		gp.add(vb, 0, 0);
		gp.add(paymentTypeLabel, 0, 3);
		gp.add(hb1, 0, 4);
		gp.add(hb, 0, 7);

		hb.setMargin(cancel, new Insets(10, 0, 0, 0));
		hb.setMargin(checkout, new Insets(10, 0, 0, 0));

		hb.setSpacing(10);
		hb1.setSpacing(30);

		hb1.setMargin(cash, new Insets(10, 0, 0, 0));
		hb1.setMargin(debit, new Insets(10, 0, 0, 0));
		hb1.setMargin(credit, new Insets(10, 0, 0, 0));

	}

	public void updateCart() {
		cartList.getItems().clear();
		cartItems = Connect.getInstance().getUserCartItems(user.getUsername());
		ObservableList<CartItem> items = FXCollections.observableArrayList();
		for (CartItem cartItem : cartItems) {
			items.add(cartItem);
		}

		cartList.setItems(items);
	}

	public void setAlignment() {
		hb.setSpacing(10);

		cancel.setMinSize(85, 50);
		checkout.setMinSize(85, 50);

		VBox.setMargin(checkOutLabel, new Insets(0, 0, 10, 20));
		VBox.setMargin(yourCartInfo, new Insets(10, 10, 20, 20));

		vb.setAlignment(Pos.CENTER);
		hb.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);

	}

	public CustCheckOut(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		updateCart();
	}

	public void display() {
		initialize();
		initForm();
		setArrangements();
		setAlignment();
		events();
		updateCart();
		primaryStage.setScene(scene);
	}

	private void events() {
		cancel.setOnAction(e -> {
			Home home = new Home(primaryStage, user);
			home.display();
		});
		logout.setOnAction(e -> {
			Login login = new Login(primaryStage);
		});

		checkout.setOnAction(e -> {
			if (paymentGroup.getSelectedToggle() == null) {
				Alert noPaymentTypeSelected = new Alert(AlertType.ERROR, "Please select payment type");
				noPaymentTypeSelected.showAndWait();
			} else {
				// Ambil tipe pembayaran yang dipilih
				String selectedPaymentType = ((RadioButton) paymentGroup.getSelectedToggle()).getText();

				// Lakukan pembaruan pada database
				checkout(selectedPaymentType);

				Alert checkoutSuccess = new Alert(AlertType.INFORMATION);
				checkoutSuccess.setHeaderText("Success");
				checkoutSuccess.setContentText("All items checked out successfully, please proceed your...");
				checkoutSuccess.showAndWait();

				Home home = new Home(primaryStage, user);
				home.display();
			}
		});

	}

	// Metode untuk memperbarui tipe pembayaran pada database
	private void checkout(String paymentType) {
		String query = "SELECT MAX(TransactionId) FROM transactionheader";
		connect.runQuery(query);

		ResultSet rs = connect.getResultSet();
		String id = "TR001";
		try {
			if (rs.next()) {
				String newest = rs.getString(1);
				String num = newest.substring(2);
				id = String.format("TR%03d", Integer.parseInt(num) + 1);
			}

			query = String.format("INSERT INTO transactionheader VALUES('%s', '%s', '%s')", id, user.getUsername(),
					paymentType);
			connect.runUpdate(query);

			for (CartItem cartItem : cartItems) {
				query = String.format("INSERT INTO transactiondetail VALUES('%s', '%s', %d)", id, cartItem.getJuiceId(),
						cartItem.getQty());
				connect.runUpdate(query);
			}

			query = String.format("DELETE FROM cartdetail WHERE username = '%s'", user.getUsername());
			connect.runUpdate(query);
			new Home(primaryStage, user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}