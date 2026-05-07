package main;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Register {
	Stage primaryStage;

	// Layout
	BorderPane bp = new BorderPane();
	GridPane gp = new GridPane();
	Scene sc = new Scene(bp, 800, 600);

	// Label
	Label titleLabel = new Label("Register");
	Label subtitleLabel = new Label("NJuice");
	Label usernameLabel = new Label("Username");
	Label passwordLabel = new Label("Password");

	// TextField
	TextField usernameField = new TextField();

	// PasswordField
	PasswordField passwordField = new PasswordField();

	// HBox
	HBox hb = new HBox();

	// VBox
	VBox vb = new VBox();
	VBox vb2 = new VBox();
	Button registerButton = new Button("Register");

	CheckBox agreeBox = new CheckBox("I agree to the terms and conditions of NJuice!");

	Label errorRegist = new Label();

	Connect connect;

	public void setComponent() {
		vb.getChildren().addAll(titleLabel, subtitleLabel);
		vb2.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField);

		gp.add(agreeBox, 0, 2);
		gp.add(hb, 0, 4);
		gp.add(vb, 0, 0);
		gp.add(vb2, 0, 1);
		gp.add(errorRegist, 0, 3);
		hb.getChildren().addAll(registerButton);
	}

	public void initialize() {
		registerButton.setOnAction((e) -> {
			if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
				errorRegist.setText("Please input all the field");
				errorRegist.setTextFill(Color.RED);
			} else if (!agreeBox.isSelected()) {
				errorRegist.setText("You must agree with the terms and conditions");
				errorRegist.setTextFill(Color.RED);
			} else {
				addData();
			}
		});
	}

	void style() {
		titleLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold;");
		subtitleLabel.setStyle("-fx-font-size: 20px");
		passwordField.setPromptText("Enter new password..");

	}

	public void setAlignment() {

		titleLabel.setFont(new Font(20));
		usernameField.setMinWidth(250);
		passwordField.setMinWidth(250);

		VBox.setMargin(usernameLabel, new Insets(0, 5, 10, 0));
		VBox.setMargin(passwordLabel, new Insets(10, 5, 10, 0));

		vb.setAlignment(Pos.CENTER);

		hb.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		gp.setVgap(8);
	}

	MenuBar menuBar = new MenuBar();
	Menu menu = new Menu("Dashboard");

	MenuItem menuItem = new MenuItem("Login");
	MenuItem menuItem1 = new MenuItem("Register");

	void addMenu() {
		menuBar.getMenus().addAll(menu);
		menu.getItems().addAll(menuItem, menuItem1);

		menuItem.setOnAction(e -> {
			Login login = new Login(primaryStage);
			login.show();
		});
		menuItem1.setOnAction(e -> {
			Register regist = new Register(primaryStage);
			regist.show();
		});
	}

	void arrange() {
		bp.setTop(menuBar);
		bp.setCenter(gp);
	}

	public Register(Stage primaryStage) {
		arrange();
		addMenu();
		setComponent();
		setAlignment();
		style();
		initialize();
		primaryStage.setScene(sc);
		this.primaryStage = primaryStage;
		this.connect = Connect.getInstance();
	}

	public void show() {
		primaryStage.show();
	}

	public void clearForm() {
		usernameField.clear();
		passwordField.clear();
	}

	public void addData() {
		// Masukkin data ke database
		String name = usernameField.getText();
		String password = passwordField.getText();
		String role = "Customer";

		// Buat check udah ada atau belom di db
		if (isUsernameTaken(name)) {
			errorRegist.setText("Username is already taken");
			errorRegist.setTextFill(Color.RED);
			return;
		}
		String query = String.format("INSERT INTO msuser VALUES ('%s', '%s', '%s')", name, password, role);
		connect.runUpdate(query);
		new Login(primaryStage);
	}

	private boolean isUsernameTaken(String name) {
		List<User> users = Connect.getInstance().getUsers();
		for (User user : users) {
			if (user.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}
}