package main;

import java.util.List;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

public class Home implements EventHandler<ActionEvent> {
	private Stage primaryStage;
	private User user;
	private List<CartItem> cartItems;
	private ListView<CartItem> cartList = new ListView<>();
	Window window;

	Connect connect = Connect.getInstance();

	// Layout
	BorderPane bp = new BorderPane();
	GridPane gp = new GridPane();
	Scene scene = new Scene(bp, 800, 600);

	// Menu bang
	MenuBar menuBar = new MenuBar();
	Menu menu = new Menu("Logout");

	// Button
	Button add, delete, checkout, logout;

	// Label
	Label yourCart = new Label("Your Cart");
	Label yourCartInfo = new Label("Your cart is empty, try adding items!");
	Label username;
	Label totalPriceLabel = new Label("Total Price: ");

	String tempid;

	// HBox
	HBox hb = new HBox();

	// VBox
	VBox vb = new VBox();
	VBox vb2 = new VBox();

	public void initialize() {
		username = new Label("Hi, " + user.getUsername());
		username.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
		add = new Button("Add new Item to Cart");
		delete = new Button("Delete Item From Cart");
		checkout = new Button("Checkout");
		logout = new Button("Logout");

		logout.setPrefSize(55, 30);
		Region spacer = new Region();
		spacer.setMinWidth(640);

		ToolBar toolBar = new ToolBar(logout, spacer, username);

		bp.setTop(toolBar);

		vb2.getChildren().addAll(yourCart, totalPriceLabel);
	}

	public void setArrangements() {
		menuBar.getMenus();

		bp.setCenter(gp);

		yourCart.setStyle("-fx-font-size: 45px; -fx-font-weight: bold;");
		vb.getChildren().addAll(yourCart);
		if (cartItems.isEmpty()) {
			vb.getChildren().addAll(yourCartInfo);

		} else {
			vb.getChildren().add(cartList);
		}
		hb.getChildren().addAll(add, delete, checkout);

		gp.add(vb, 0, 0);
		gp.add(vb2, 0, 1);
		gp.add(hb, 0, 2);

		vb2.setAlignment(Pos.CENTER);
		vb2.setPadding(new Insets(10));

		cartList.setMaxHeight(200);

	}

	public void updateCart() {
		cartList.getItems().clear();
		cartItems = Connect.getInstance().getUserCartItems(user.getUsername());
		ObservableList<CartItem> items = FXCollections.observableArrayList();
		for (CartItem cartItem : cartItems) {
			items.add(cartItem);
		}

		// Set the ObservableList to the cartList
		cartList.setItems(items);

		// Update total price label
		double total = calculateTotalPrice(cartItems);
		totalPriceLabel.setText("Total Price: " + total);
	}

	private double calculateTotalPrice(List<CartItem> cartItems) {
		double total = 0;
		for (CartItem cartItem : cartItems) {
			total += cartItem.getJuicePrice() * cartItem.getQty();
		}
		return total;
	}

	public void deleteItem() {
		delete.setOnAction(e -> {
			if (tempid == null) {
				Alert notSelected = new Alert(AlertType.ERROR, "Please choose which juice to delete");
				notSelected.show();
				return;
			}

			String query = String.format("DELETE FROM cartdetail WHERE JuiceId = '%s'", tempid);
			connect.runUpdate(query);
			new Home(primaryStage, user).display();
		});
	}

	public void checkOutItem() {
		checkout.setOnAction(e -> {
			if (cartItems.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Your cart is empty");
				alert.show();
			} else {
				CustCheckOut custCheckOut = new CustCheckOut(primaryStage, user);
				custCheckOut.display();
			}
		});
	}

	public EventHandler<MouseEvent> tableMouseEvent() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cartList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				CartItem selectedItem = cartList.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					tempid = selectedItem.getJuiceId();
				}
			}
		};
	}

	public void setAlignment() {
		hb.setSpacing(10);

		add.setMinSize(150, 50);
		delete.setMinSize(155, 50);
		checkout.setMinSize(85, 50);

		VBox.setMargin(yourCart, new Insets(0, 0, 10, 20));
		VBox.setMargin(yourCartInfo, new Insets(10, 10, 20, 20));

		vb.setAlignment(Pos.CENTER);
		hb.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
	}

	public void eventHandler() {
		add.setOnAction(this);
		delete.setOnAction(this);
		checkout.setOnAction(this);
		logout.setOnAction(this);
	}

	public Home(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		updateCart();
	}

	public void display() {
		initialize();
		setArrangements();
		eventHandler();
		setAlignment();
		cartList.setOnMouseClicked(tableMouseEvent());
		deleteItem();
		updateCart();
		checkOutItem();
		primaryStage.setScene(scene);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == add) {
			Add addCart = new Add(primaryStage, user);
			addCart.display();
			updateCart();
		} else if (event.getSource() == logout) {
			Login login = new Login(primaryStage);
			login.show();
		}
	}
}