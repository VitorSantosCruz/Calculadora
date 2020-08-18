package com.vitor.calculadora.controles;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HistoricoControle {
	@FXML
	private TextArea textAreaHistorico;

	private Stage stage = new Stage();

	public HistoricoControle(String historico) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../modelos/HistoricoModelo.fxml"));
			fxmlLoader.setController(this);
			Scene scene = new Scene(fxmlLoader.load());

			stage.setTitle("Histórico");
			stage.resizableProperty().setValue(false);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			System.out.println(historico);
			textAreaHistorico.setText(historico);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
