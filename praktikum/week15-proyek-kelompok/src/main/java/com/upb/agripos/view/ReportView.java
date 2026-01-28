package com.upb.agripos.view;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.User;
import com.upb.agripos.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportView extends BorderPane {
    private TransactionService service = new TransactionService();

    public ReportView(User user, Stage stage) {
        setPadding(new Insets(20));

        // 1. Judul
        Label title = new Label("Laporan Riwayat Transaksi");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 2. Tabel Laporan
        TableView<Transaction> table = new TableView<>();
        
        TableColumn<Transaction, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Transaction, String> colDate = new TableColumn<>("Tanggal");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setMinWidth(150);
        
        TableColumn<Transaction, Double> colTotal = new TableColumn<>("Total (Rp)");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        TableColumn<Transaction, String> colMethod = new TableColumn<>("Metode");
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        
        TableColumn<Transaction, String> colKasir = new TableColumn<>("Kasir");
        colKasir.setCellValueFactory(new PropertyValueFactory<>("cashierName"));

        table.getColumns().addAll(colId, colDate, colTotal, colMethod, colKasir);

        // Load Data dari Database
        try {
            table.getItems().addAll(service.getSalesReport());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Gagal ambil data: " + e.getMessage()).show();
        }

        // 3. Tombol Kembali
        Button btnBack = new Button("Kembali ke Kasir");
        btnBack.setOnAction(e -> stage.setScene(new Scene(new PosView(user, stage), 900, 600)));

        // Susun Layout
        VBox topBox = new VBox(10, title, btnBack);
        topBox.setPadding(new Insets(0,0,10,0));
        
        setTop(topBox);
        setCenter(table);
    }
}