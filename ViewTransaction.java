package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewTransaction implements EventHandler<ActionEvent> {

	Connect connect = Connect.getInstance(); // Menggantinya sesuai dengan kebutuhan aplikasi Anda

	ObservableList<TransactionAdmDetail> juiceObj1 = FXCollections.observableArrayList();

	Stage primaryStage;
	BorderPane bp = new BorderPane();
	Scene sc = new Scene(bp, 800, 600);
	VBox vbox = new VBox();
	Label viewTransaction;
	MenuBar menuBar = new MenuBar();
	Menu adminsDash = new Menu("Admin's Dashboard");
	Menu logout = new Menu("Logout");
	MenuItem viewTrans = new MenuItem("View Transactions");
	MenuItem manageProd = new MenuItem("Manage Products");
	MenuItem logoutAdm = new MenuItem("Logout from admin");
	TableView<TransactionAdm> transactionTable = new TableView<>();
	TableColumn<TransactionAdm, String> transactionID, paymentType, username;
	TableView<TransactionAdmDetail> transactionDetailTable = new TableView<>();
	// TableColumn<TransactionAdmDetail, String> juiceID, juiceName, quantity;
	ScrollPane sp1 = new ScrollPane(transactionTable);
	ScrollPane sp2 = new ScrollPane(transactionDetailTable);

	public void initialize() {
		viewTransaction = new Label("View Transaction");
		vbox.getChildren().addAll(viewTransaction, transactionTable, transactionDetailTable);
		viewTransaction.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	}

	@SuppressWarnings("unchecked")
	public void table1() {
		transactionID = new TableColumn<>("Transaction ID");
		transactionID.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		transactionID.setMinWidth(100);

		paymentType = new TableColumn<>("Payment Type");
		paymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
		paymentType.setMinWidth(100);

		username = new TableColumn<>("Username");
		username.setCellValueFactory(new PropertyValueFactory<>("username"));
		username.setMinWidth(100);

		transactionTable.getColumns().addAll(transactionID, paymentType, username);
		transactionTable.setMaxWidth(300);
		transactionTable.setMaxHeight(220);
	}

	@SuppressWarnings("unchecked")
	public void table2() {

		TableColumn<TransactionAdmDetail, String> transactionIdColumn = new TableColumn<>("Transaction ID");
		transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		transactionIdColumn.setMinWidth(100);

		TableColumn<TransactionAdmDetail, String> juiceIdColumn = new TableColumn<>("Juice ID");
		juiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("juiceId"));
		juiceIdColumn.setMinWidth(100);

		TableColumn<TransactionAdmDetail, String> juiceNameColumn = new TableColumn<>("Juice Name");
		juiceNameColumn.setCellValueFactory(new PropertyValueFactory<>("juiceName"));
		juiceNameColumn.setMinWidth(220);

		TableColumn<TransactionAdmDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setMinWidth(100);

		transactionDetailTable.getColumns().addAll(transactionIdColumn, juiceIdColumn, juiceNameColumn, quantityColumn);
		transactionDetailTable.setMaxWidth(520);
		transactionDetailTable.setMaxHeight(220);
		transactionDetailTable.setItems(juiceObj1);

	}

	public void menubar() {
		menuBar.getMenus().add(adminsDash);
		adminsDash.getItems().add(viewTrans);
		adminsDash.getItems().add(manageProd);

		menuBar.getMenus().add(logout);
		logout.getItems().add(logoutAdm);
	}

	public void positioning() {
		bp.setTop(menuBar);
		bp.setCenter(vbox);

		vbox.setPadding(new Insets(20));
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
	}

	private void setEvent() {
		logoutAdm.setOnAction(e -> {
			new Login(primaryStage);
		});

		manageProd.setOnAction(this);

		// Menambahkan listener untuk merespons perubahan pemilihan di tabel transaksi
		transactionTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TransactionAdm>() {
			@Override
			public void changed(ObservableValue<? extends TransactionAdm> observable, TransactionAdm oldValue,
					TransactionAdm newValue) {
				if (newValue != null) {
					String selectedTransactionId = newValue.getTransactionId();
					showTransactionDetails(selectedTransactionId);
				}
			}
		});
	}

	// Metode untuk menampilkan detail transaksi
	private void showTransactionDetails(String transactionId) {
		Connect connect = Connect.getInstance();
		juiceObj1.clear();

		// Ganti 'transactiondetail' dengan nama tabel detail transaksi yang sesuai di
		// database Anda
		String query = "SELECT td.JuiceId, JuiceName, Quantity FROM transactiondetail td JOIN msjuice mj ON td.JuiceId = mj.JuiceId WHERE TransactionId = '"
				+ transactionId + "'";

		try {
			connect.runQuery(query);
			ResultSet resultSet = connect.getResultSet();

			// Membuat daftar detail transaksi
			ObservableList<TransactionAdmDetail> transactionDetailList = FXCollections.observableArrayList();

			while (resultSet.next()) {
				String juiceId = resultSet.getString("JuiceId");
				String juiceName = resultSet.getString("juiceName");
				int quantity = resultSet.getInt("Quantity");

				TransactionAdmDetail transactionDetail = new TransactionAdmDetail(transactionId, juiceId, juiceName,
						quantity);
				transactionDetailList.add(transactionDetail);
				juiceObj1.add(new TransactionAdmDetail(transactionId, juiceId, juiceName, quantity));
			}

			// Menampilkan data detail transaksi pada tabel
			transactionDetailTable.setItems(transactionDetailList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		transactionDetailTable.setItems(juiceObj1);
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == manageProd) {
			AdminManage am = new AdminManage(primaryStage);
			am.show();
		} else if (e.getSource() == logoutAdm) {
			Login login = new Login(primaryStage);
			login.show();
		}
	}

	public ViewTransaction(Stage primaryStage) {
		menubar();
		positioning();
		initialize();
		table1();
		table2();
		setEvent();
		primaryStage.setScene(sc);
		this.primaryStage = primaryStage;

		// Menampilkan data transaksi saat inisialisasi kelas
		retrieveTransactionData();
	}

	// Metode untuk mengambil dan menampilkan data transaksi
	// Metode untuk mengambil dan menampilkan data transaksi
	private void retrieveTransactionData() {
		// Ganti 'transactionheader' dengan nama tabel transaksi yang sesuai di database
		// Anda
		String query = "SELECT TransactionId, PaymentType, Username FROM transactionheader";

		try {
			connect.runQuery(query);
			ResultSet resultSet = connect.getResultSet();
			// Membuat daftar transaksi
			ObservableList<TransactionAdm> transactionList = FXCollections.observableArrayList();

			while (resultSet.next()) {
				String transactionId = resultSet.getString("TransactionId");
				String paymentType = resultSet.getString("PaymentType");
				String username = resultSet.getString("Username");

				TransactionAdm transaction = new TransactionAdm(transactionId, paymentType, username);
				transactionList.add(transaction);
			}

			// Menampilkan data transaksi pada tabel
			transactionTable.setItems(transactionList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void show() {
//		primaryStage.show();
	}
}
