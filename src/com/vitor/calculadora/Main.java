package com.vitor.calculadora;

import com.vitor.calculadora.controles.CalculadoraControle;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		new CalculadoraControle();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
