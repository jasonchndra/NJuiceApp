package main;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;

public class Login {
	Stage primaryStage;
	BorderPane bp = new BorderPane();
	GridPane gp = new GridPane();
	Scene sc = new Scene(bp, 800, 600);
	Label titleLabel = new Label("Login");
	Label subtitleLabel = new Label("NJuice");
	Label usernameLabel = new Label("Username");
	Label passwordLabel = new Label("Password");
	Label passwordLabel2 = new Label("Enter new password");

	TextField usernameField = new TextField();
	PasswordField passwordField = new PasswordField();

	HBox hb = new HBox();
	VBox vb = new VBox();
	VBox vb2 = new VBox();
	Button loginButton = new Button("Login");

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

	Label errorLabel = new Label();

	public void setComponent() {
		GridPane.setValignment(usernameLabel, VPos.CENTER);
		GridPane.setValignment(subtitleLabel, VPos.CENTER);
		GridPane.setValignment(titleLabel, VPos.CENTER);
		GridPane.setValignment(passwordLabel, VPos.CENTER);

		hb.getChildren().addAll(loginButton);
		gp.add(hb, 0, 5);

		vb.getChildren().addAll(titleLabel, subtitleLabel);
		vb2.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField);
		gp.add(vb2, 0, 3);
		gp.add(vb, 0, 0);
		gp.add(errorLabel, 0, 4);
	}

	public void setAlignment() {
		passwordField.setPromptText("Enter Password..");
		usernameField.setMinWidth(250);
		passwordField.setMinWidth(250);

		gp.setVgap(8);
		VBox.setMargin(usernameLabel, new Insets(0, 5, 10, 0));
		VBox.setMargin(passwordLabel, new Insets(10, 5, 10, 0));
		gp.setAlignment(Pos.CENTER);
		hb.setAlignment(Pos.TOP_CENTER);
		vb.setAlignment(Pos.CENTER);
	}

	void style() {
		titleLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold;");
		subtitleLabel.setStyle("-fx-font-size: 20px");
	}

	public void eventHandler() {
		loginButton.setOnAction((e) -> {
			String username = usernameField.getText();
			String password = passwordField.getText();

			if (username.isEmpty() || password.isEmpty()) {
				errorLabel.setText("Username and password must be filled in");
			}

			List<User> allUsers = Connect.getInstance().getUsers();
			User currentUser = null;
			for (User user : allUsers) {
				if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
					currentUser = user;
				}
			}

			if (currentUser != null) {
				if (currentUser.getRole().equals("Admin")) {
					ViewTransaction vt = new ViewTransaction(primaryStage);
					vt.show();
				} else {
					Home home = new Home(primaryStage, currentUser);
					home.display();
				}
			} else {
				errorLabel.setText("Credentials failed!");
				errorLabel.setTextFill(Color.RED);
			}
		});
	}

	public Login(Stage primaryStage) {
		setAlignment();
		setComponent();
		addMenu();
		arrange();
		eventHandler();
		style();
		primaryStage.setScene(sc);
		this.primaryStage = primaryStage;
	}

	public void show() {
//		primaryStage.show();
	}
}