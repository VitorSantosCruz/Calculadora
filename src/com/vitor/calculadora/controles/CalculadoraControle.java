package com.vitor.calculadora.controles;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculadoraControle {
	private double resultado = 0.0;
	private double valorAtual = 0.0;
	private String operacaoPendente = "";
	private boolean terminouComIgual = false;
	private boolean reiniciarValorAtual = false;
	private Stage stage = new Stage();

	@FXML
	private Button botaoMudaSinal;

	@FXML
	private Button botao0;

	@FXML
	private Button botaoVirgula;

	@FXML
	private Button botaoIgual;

	@FXML
	private Button botao1;

	@FXML
	private Button botao2;

	@FXML
	private Button botao3;

	@FXML
	private Button botaoSoma;

	@FXML
	private Button botao4;

	@FXML
	private Button botao5;

	@FXML
	private Button botao6;

	@FXML
	private Button botaoSubtracao;

	@FXML
	private Button botao7;

	@FXML
	private Button botao8;

	@FXML
	private Button botao9;

	@FXML
	private Button botaoMultiplicacao;

	@FXML
	private Button botaoCe;

	@FXML
	private Button botaoC;

	@FXML
	private Button botaoApagar;

	@FXML
	private Button botaoDivisao;

	@FXML
	private Label labelResultado;

	@FXML
	private Label labelCalculo;

	@FXML
	private Button botaoHistoricoCalculo;

	public CalculadoraControle() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../modelos/CalculadoraModelo.fxml"));
			fxmlLoader.setController(this);
			Scene scene = new Scene(fxmlLoader.load());

			stage.setTitle("Calculadora");
			stage.resizableProperty().setValue(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mudarTamanhoLetra() {
		int tamanho;

		if (this.labelResultado.getText().trim().length() >= 10 && this.labelResultado.getText().trim().length() < 15) {
			tamanho = 35;
		} else if (this.labelResultado.getText().trim().length() >= 15
				&& this.labelResultado.getText().trim().length() < 20) {
			tamanho = 30;
		} else if (this.labelResultado.getText().trim().length() >= 20) {
			tamanho = 25;
		} else {
			tamanho = 40;
		}

		this.labelResultado.setFont(new Font("Arial Bold", tamanho));
	}

	private String formataNumero(double numero) {
		String numeroFormatado = String.valueOf(numero);

		if (this.temDecimais(numero)) {
			numeroFormatado = numeroFormatado.replace(".", ",");
		} else {
			String valor = numeroFormatado;
			valor = valor.substring(0, valor.indexOf("."));
			numeroFormatado = valor;
		}

		return numeroFormatado;
	}

	private boolean temDecimais(double numero) {
		String valor = String.valueOf(numero);
		valor = valor.substring(valor.indexOf(".") + 1);

		return !valor.equals("0");
	}

	private void atualizarLabelCalculo() {
		String labelResultado = this.labelResultado.getText().trim();
		int indiceVirgula = labelResultado.indexOf(",");

		if (indiceVirgula == labelResultado.length() - 1) {
			labelResultado = labelResultado.substring(0, indiceVirgula);
		}

		if (this.labelCalculo.getText().trim().isEmpty()) {
			this.labelCalculo.setText(labelResultado + " " + this.operacaoPendente);
		} else {
			this.labelCalculo
					.setText(this.labelCalculo.getText().trim() + " " + labelResultado + " " + this.operacaoPendente);
		}
	}

	private void adicionarNumero(String numero) {
		String valor = this.labelResultado.getText().trim();

		if (this.terminouComIgual) {
			clicarEmC(null);
			valor = "0";
		}

		if (valor.length() <= 21 || this.reiniciarValorAtual) {
			if (valor.equals("0") || this.reiniciarValorAtual) {
				this.reiniciarValorAtual = false;
				this.labelResultado.setText(numero);
			} else {
				this.labelResultado.setText(valor + numero);
			}

			valor = this.labelResultado.getText().trim();
			valor = valor.replace(".", "");

			try {
				this.valorAtual = Double.valueOf(valor.replace(',', '.'));
			} catch (Exception e) {
				clicarEmC(null);
				this.labelResultado.setText("NAN");
			}

			this.mudarTamanhoLetra();
		}
	}

	private double calculadora(String operacao) {
		return switch (operacao) {
		case "+":
			yield this.resultado + this.valorAtual;
		case "-":
			yield this.resultado - this.valorAtual;
		case "*":
			yield this.resultado * this.valorAtual;
		case "/":
			yield this.resultado / this.valorAtual;
		default:
			throw new IllegalArgumentException("Operação inesperada " + operacao);
		};
	}

	@FXML
	void clicarEm0(ActionEvent event) {
		adicionarNumero("0");
	}

	@FXML
	void clicarEm1(ActionEvent event) {
		adicionarNumero("1");
	}

	@FXML
	void clicarEm2(ActionEvent event) {
		adicionarNumero("2");
	}

	@FXML
	void clicarEm3(ActionEvent event) {
		adicionarNumero("3");
	}

	@FXML
	void clicarEm4(ActionEvent event) {
		adicionarNumero("4");
	}

	@FXML
	void clicarEm5(ActionEvent event) {
		adicionarNumero("5");
	}

	@FXML
	void clicarEm6(ActionEvent event) {
		adicionarNumero("6");
	}

	@FXML
	void clicarEm7(ActionEvent event) {
		adicionarNumero("7");
	}

	@FXML
	void clicarEm8(ActionEvent event) {
		adicionarNumero("8");
	}

	@FXML
	void clicarEm9(ActionEvent event) {
		adicionarNumero("9");
	}

	@FXML
	void clicarEmApagar(ActionEvent event) {
		String valor = this.labelResultado.getText().trim();

		if (terminouComIgual) {
			this.labelCalculo.setText("");
		} else if (valor != "0" || this.reiniciarValorAtual || this.terminouComIgual) {
			if (valor.length() == 1 || (valor.length() == 2 && valor.indexOf("-") > -1)) {
				this.labelResultado.setText("0");
			} else {
				this.labelResultado.setText(valor.substring(0, valor.length() - 1));
			}

			this.reiniciarValorAtual = false;
			this.valorAtual = Double.valueOf(this.labelResultado.getText().replace(',', '.').trim());
			this.mudarTamanhoLetra();
		}
	}

	@FXML
	void clicarEmC(ActionEvent event) {
		this.labelCalculo.setText("");
		this.labelResultado.setText("0");
		this.valorAtual = 0;
		this.resultado = 0;
		this.operacaoPendente = "";
		this.terminouComIgual = false;
		this.reiniciarValorAtual = false;
		this.mudarTamanhoLetra();
	}

	@FXML
	void clicarEmCe(ActionEvent event) {
		this.labelResultado.setText("0");
		this.valorAtual = 0;

		if (this.terminouComIgual) {
			this.resultado = 0;
			this.operacaoPendente = "";
			this.terminouComIgual = false;
			this.labelCalculo.setText("");
			this.reiniciarValorAtual = false;
		}

		this.mudarTamanhoLetra();
	}

	@FXML
	void clicarEmDivisao(ActionEvent event) {
		if (terminouComIgual) {
			this.labelCalculo.setText("");
			this.valorAtual = this.resultado;
			this.terminouComIgual = false;
			this.reiniciarValorAtual = false;
			this.mudarTamanhoLetra();
		}

		if (this.reiniciarValorAtual) {
			String labelCalculo = this.labelCalculo.getText().trim();
			this.operacaoPendente = "/";
			this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + this.operacaoPendente);
		} else {
			if (this.labelCalculo.getText().trim().equals("")) {
				this.resultado = this.valorAtual;
			} else {
				this.resultado = this.calculadora(this.operacaoPendente);
			}

			this.operacaoPendente = "/";
			this.reiniciarValorAtual = true;
			this.atualizarLabelCalculo();

			if (this.temDecimais(this.resultado)) {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			} else {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			}

			this.mudarTamanhoLetra();
		}

		this.valorAtual = this.resultado;
	}

	@FXML
	void clicarEmMultiplicacao(ActionEvent event) {
		if (terminouComIgual) {
			this.labelCalculo.setText("");
			this.valorAtual = this.resultado;
			this.terminouComIgual = false;
			this.reiniciarValorAtual = false;
			this.mudarTamanhoLetra();
		}

		if (this.reiniciarValorAtual) {
			String labelCalculo = this.labelCalculo.getText().trim();
			this.operacaoPendente = "*";
			this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + this.operacaoPendente);
		} else {
			if (this.labelCalculo.getText().trim().equals("")) {
				this.resultado = this.valorAtual;
			} else {
				this.resultado = this.calculadora(this.operacaoPendente);
			}

			this.operacaoPendente = "*";
			this.reiniciarValorAtual = true;
			this.atualizarLabelCalculo();

			if (this.temDecimais(this.resultado)) {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			} else {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			}

			this.mudarTamanhoLetra();
		}

		this.valorAtual = this.resultado;
	}

	@FXML
	void clicarEmSoma(ActionEvent event) {
		if (terminouComIgual) {
			this.labelCalculo.setText("");
			this.valorAtual = this.resultado;
			this.terminouComIgual = false;
			this.reiniciarValorAtual = false;
			this.mudarTamanhoLetra();
		}

		if (this.reiniciarValorAtual) {
			String labelCalculo = this.labelCalculo.getText().trim();
			this.operacaoPendente = "+";
			this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + this.operacaoPendente);
		} else {
			if (this.labelCalculo.getText().trim().equals("")) {
				this.resultado = this.valorAtual;
			} else {
				this.resultado = this.calculadora(this.operacaoPendente);
			}

			this.operacaoPendente = "+";
			this.reiniciarValorAtual = true;
			this.atualizarLabelCalculo();

			if (this.temDecimais(this.resultado)) {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			} else {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			}

			this.mudarTamanhoLetra();
		}

		this.valorAtual = this.resultado;
	}

	@FXML
	void clicarEmSubtracao(ActionEvent event) {
		if (terminouComIgual) {
			this.labelCalculo.setText("");
			this.valorAtual = this.resultado;
			this.terminouComIgual = false;
			this.reiniciarValorAtual = false;
			this.mudarTamanhoLetra();

		}

		if (this.reiniciarValorAtual) {
			String labelCalculo = this.labelCalculo.getText().trim();
			this.operacaoPendente = "-";
			this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + this.operacaoPendente);
		} else {
			if (this.labelCalculo.getText().trim().equals("")) {
				this.resultado = this.valorAtual;
			} else {
				this.resultado = this.calculadora(this.operacaoPendente);
			}

			this.operacaoPendente = "-";
			this.reiniciarValorAtual = true;
			this.atualizarLabelCalculo();

			if (this.temDecimais(this.resultado)) {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			} else {
				String resultado = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultado);
			}

			this.mudarTamanhoLetra();
		}

		this.valorAtual = this.resultado;
	}

	@FXML
	void clicarEmIgual(ActionEvent event) {
		String resultado = formataNumero(this.resultado);
		String valorAtual = formataNumero(this.valorAtual);

		if (!this.operacaoPendente.isEmpty()) {
			String valor;

			this.resultado = this.calculadora(this.operacaoPendente);

			if (!terminouComIgual) {
				this.atualizarLabelCalculo();
			}

			if (this.temDecimais(this.resultado)) {
				valor = formataNumero(this.resultado);
			} else {
				valor = formataNumero(this.resultado);
			}

			this.labelResultado.setText("" + valor);

			if (!terminouComIgual) {
				String labelCalculo = this.labelCalculo.getText().trim();
				this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + " =");
			} else {
				this.labelCalculo.setText(resultado + " " + this.operacaoPendente + " " + valorAtual + " =");
			}
		} else {
			this.resultado = this.valorAtual;

			this.labelCalculo.setText(valorAtual + " =");
		}

		this.mudarTamanhoLetra();
		this.terminouComIgual = true;
	}

	@FXML
	void clicarEmMudaSinal(ActionEvent event) {
		String valor = this.labelResultado.getText().trim();

		if (!valor.endsWith("0")) {
			if (valor.substring(0, 1).equals("-")) {
				valor = valor.substring(1, valor.length());
			} else {
				valor = "-" + valor;
			}

			this.valorAtual *= -1;
			this.labelResultado.setText(valor);
		}
	}

	@FXML
	void clicarEmVirgula(ActionEvent event) {
		String valor = this.labelResultado.getText().trim();
		boolean temVirgula = valor.indexOf(",") > -1 ? true : false;

		if (this.reiniciarValorAtual) {
			valor = "0";
			temVirgula = false;
			this.reiniciarValorAtual = false;
		}

		if (!temVirgula) {
			this.labelResultado.setText(valor + ",");
			this.mudarTamanhoLetra();
		}
	}

	@FXML
	void mostrarHistorico(ActionEvent event) {
		new HistoricoControle(this.labelCalculo.getText());
	}

	@FXML
	void pressionarTeclado(KeyEvent event) {
		System.out.println(event.getCode());
		switch (event.getCode()) {
		case NUMPAD0, DIGIT0:
			this.clicarEm0(null);
			break;
		case NUMPAD1, DIGIT1:
			this.clicarEm1(null);
			break;
		case NUMPAD2, DIGIT2:
			this.clicarEm2(null);
			break;
		case NUMPAD3, DIGIT3:
			this.clicarEm3(null);
			break;
		case NUMPAD4, DIGIT4:
			this.clicarEm4(null);
			break;
		case NUMPAD5, DIGIT5:
			this.clicarEm5(null);
			break;
		case NUMPAD6, DIGIT6:
			this.clicarEm6(null);
			break;
		case NUMPAD7, DIGIT7:
			this.clicarEm7(null);
			break;
		case NUMPAD8, DIGIT8:
			this.clicarEm8(null);
			break;
		case NUMPAD9, DIGIT9:
			this.clicarEm9(null);
			break;
		case F9:
			this.clicarEmMudaSinal(null);
			break;
		case COMMA, DECIMAL:
			this.clicarEmVirgula(null);
			break;
		case ENTER, EQUALS:
			this.clicarEmIgual(null);
			break;
		case ADD:
			this.clicarEmSoma(null);
			break;
		case SUBTRACT, MINUS:
			this.clicarEmSubtracao(null);
			break;
		case MULTIPLY:
			this.clicarEmMultiplicacao(null);
			break;
		case DIVIDE, UNDEFINED:
			this.clicarEmDivisao(null);
			break;
		case BACK_SPACE:
			this.clicarEmApagar(null);
			break;
		case DELETE:
			this.clicarEmCe(null);
			break;
		case ESCAPE:
			this.clicarEmC(null);
			break;
		case H:
			this.mostrarHistorico(null);
			break;
		default:
		}
	}
}
