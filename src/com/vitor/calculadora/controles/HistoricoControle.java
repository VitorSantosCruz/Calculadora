package com.vitor.calculadora.controles;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HistoricoControle {
	@FXML
	private TextArea textAreaHistorico;

	private Stage stage = new Stage();

	public HistoricoControle(ArrayList<String> historico) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../modelos/HistoricoModelo.fxml"));
			fxmlLoader.setController(this);
			Scene scene = new Scene(fxmlLoader.load());

			stage.setTitle("Histórico");
			stage.resizableProperty().setValue(false);
			stage.getIcons().add(new Image("icone.png"));
			stage.setScene(scene);

			historico.forEach((calculo) -> {
				textAreaHistorico.appendText(calculo + "\n");
			});

			textAreaHistorico.appendText("_____________________");

			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
