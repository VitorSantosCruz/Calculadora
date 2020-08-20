package com.vitor.calculadora.controles;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculadoraControle {
	private double resultado = 0.0;
	private double valorAtual = 0.0;
	private String operacaoPendente = "";
	private boolean terminouComIgual = false;
	private boolean reiniciarValorAtual = false;
	private boolean houveErroDivisaoPor0 = false;
	private ArrayList<String> historico = new ArrayList<>();
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
			stage.getIcons().add(new Image("icone.png"));
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void mudarTamanhoLetra() {
		int tamanho;
		int tamanhoLabelResultado = this.labelResultado.getText().trim().length();

		if (tamanhoLabelResultado >= 10 && tamanhoLabelResultado < 15) {
			tamanho = 35;
		} else if (tamanhoLabelResultado >= 15 && tamanhoLabelResultado < 20) {
			tamanho = 30;
		} else if (tamanhoLabelResultado >= 20) {
			tamanho = 24;
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
			numeroFormatado = numeroFormatado.substring(0, numeroFormatado.indexOf("."));
		}

		return numeroFormatado;
	}

	private boolean temDecimais(double numero) {
		String valor = String.valueOf(numero);
		valor = valor.substring(valor.indexOf(".") + 1);

		return !valor.equals("0");
	}

	private void atualizarLabelCalculo(boolean igual) {
		String valorAtual = String.valueOf(this.valorAtual).replace('.', ',');
		String labelCalculo = this.labelCalculo.getText().trim();

		if (!temDecimais(this.valorAtual)) {
			valorAtual = valorAtual.substring(0, valorAtual.indexOf(","));
		}

		if (igual) {
			if (!this.terminouComIgual) {
				this.labelCalculo.setText(labelCalculo + " " + valorAtual + " =");
			} else {
				String resultado = String.valueOf(this.resultado).replace('.', ',');

				if (!temDecimais(this.valorAtual)) {
					resultado = resultado.substring(0, resultado.indexOf(","));
				}

				this.labelCalculo.setText(resultado + " " + this.operacaoPendente + " " + valorAtual + " =");
			}
		} else {
			if (labelCalculo.isEmpty()) {
				this.labelCalculo.setText(valorAtual + " " + this.operacaoPendente);
			} else {
				this.labelCalculo.setText(labelCalculo + " " + valorAtual + " " + this.operacaoPendente);
			}
		}

	}

	private void adicionarNumero(String numero) {
		String labelResultado = this.labelResultado.getText().trim();

		if (this.terminouComIgual) {
			clicarEmC(null);
			labelResultado = "0";
		}

		if (labelResultado.length() < 16 || this.reiniciarValorAtual || houveErroDivisaoPor0) {
			if (labelResultado.equals("0,")) {
				this.reiniciarValorAtual = false;
			} else if (labelResultado.equals("0") || this.reiniciarValorAtual || houveErroDivisaoPor0) {
				this.reiniciarValorAtual = false;
				labelResultado = "";
			}

			labelResultado = labelResultado + numero;
			this.labelResultado.setText(labelResultado);

			this.valorAtual = Double.valueOf(labelResultado.replace(',', '.'));
			this.mudarTamanhoLetra();
		}
	}

	private double calculadora(String operacao) throws Exception {
		BigDecimal resultado = new BigDecimal(this.resultado);
		BigDecimal valorAtual = new BigDecimal(this.valorAtual);

		return switch (operacao) {
		case "+":
			yield resultado.add(valorAtual).doubleValue();
		case "-":
			yield resultado.subtract(valorAtual).doubleValue();
		case "*":
			yield resultado.multiply(valorAtual).doubleValue();
		case "/":
			yield resultado.divide(valorAtual, 102400, RoundingMode.HALF_UP).doubleValue();
		default:
			throw new IllegalArgumentException("Operação inesperada " + operacao);
		};
	}

	private void mostrarErroDivisaoPor0() {
		this.clicarEmC(null);
		this.labelResultado.setText("Não é possivel dividir por 0");
		this.mudarTamanhoLetra();
		this.houveErroDivisaoPor0 = true;
	}

	private void fazerOperacao(String operacao) {
		if (terminouComIgual) {
			this.labelCalculo.setText("");
			this.valorAtual = this.resultado;
			this.terminouComIgual = false;
			this.reiniciarValorAtual = false;
			this.mudarTamanhoLetra();

		}

		if (houveErroDivisaoPor0) {
			return;
		}

		if (this.reiniciarValorAtual) {
			String labelCalculo = this.labelCalculo.getText().trim();
			this.operacaoPendente = operacao;
			this.labelCalculo.setText(labelCalculo.substring(0, labelCalculo.length() - 1) + this.operacaoPendente);
		} else {
			if (this.labelCalculo.getText().trim().equals("")) {
				this.resultado = this.valorAtual;
			} else {
				try {
					this.resultado = this.calculadora(this.operacaoPendente);
				} catch (Exception e) {
					this.mostrarErroDivisaoPor0();
					return;
				}

				String resultadoFinal = formataNumero(this.resultado);
				this.labelResultado.setText(" " + resultadoFinal);
			}

			this.operacaoPendente = operacao;
			this.reiniciarValorAtual = true;
			this.atualizarLabelCalculo(false);

			this.mudarTamanhoLetra();
		}

		this.valorAtual = this.resultado;
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
		String labelResultado = this.labelResultado.getText().trim();

		if (this.terminouComIgual) {
			this.labelCalculo.setText("");
		} else if (!labelResultado.equals("0") || this.reiniciarValorAtual) {
			if (labelResultado.length() == 1 || labelResultado.length() == 2 && labelResultado.startsWith("-")) {
				labelResultado = "0";
				this.labelResultado.setText(labelResultado);
			} else {
				labelResultado = labelResultado.substring(0, labelResultado.length() - 1);
				this.labelResultado.setText(labelResultado);
			}

			this.reiniciarValorAtual = false;
			this.valorAtual = Double.valueOf(labelResultado.replace(',', '.'));
			this.mudarTamanhoLetra();
		}
	}

	@FXML
	void clicarEmC(ActionEvent event) {
		this.labelCalculo.setText("");
		this.labelResultado.setText("0");
		this.valorAtual = 0.0;
		this.resultado = 0.0;
		this.operacaoPendente = "";
		this.terminouComIgual = false;
		this.reiniciarValorAtual = false;
		this.mudarTamanhoLetra();
	}

	@FXML
	void clicarEmCe(ActionEvent event) {
		this.labelResultado.setText("0");
		this.valorAtual = 0.0;

		if (this.terminouComIgual) {
			this.resultado = 0.0;
			this.operacaoPendente = "";
			this.terminouComIgual = false;
			this.labelCalculo.setText("");
			this.reiniciarValorAtual = false;
		}

		this.mudarTamanhoLetra();
	}

	@FXML
	void clicarEmDivisao(ActionEvent event) {
		fazerOperacao("/");
	}

	@FXML
	void clicarEmMultiplicacao(ActionEvent event) {
		fazerOperacao("*");
	}

	@FXML
	void clicarEmSoma(ActionEvent event) {
		fazerOperacao("+");
	}

	@FXML
	void clicarEmSubtracao(ActionEvent event) {
		fazerOperacao("-");
	}

	@FXML
	void clicarEmIgual(ActionEvent event) {
		String valorAtual = formataNumero(this.valorAtual);

		if (houveErroDivisaoPor0) {
			return;
		}

		if (!this.operacaoPendente.isEmpty()) {
			String valor;

			this.atualizarLabelCalculo(true);

			try {
				this.resultado = this.calculadora(this.operacaoPendente);
			} catch (Exception e) {
				this.mostrarErroDivisaoPor0();
				return;
			}

			valor = formataNumero(this.resultado);
			this.labelResultado.setText(valor);
		} else {
			this.resultado = this.valorAtual;
			this.labelCalculo.setText(valorAtual + " =");
		}

		this.historico.add(this.labelCalculo.getText().trim() + " " + this.labelResultado.getText().trim());
		this.mudarTamanhoLetra();
		this.terminouComIgual = true;
	}

	@FXML
	void clicarEmMudaSinal(ActionEvent event) {
		String valor = String.valueOf(this.resultado);
		String labelResultado = this.labelResultado.getText().trim();

		if (terminouComIgual) {
			clicarEmC(null);
			valorAtual = Double.valueOf(valor);
		}

		if (labelResultado.equals("0")) {
			return;
		}

		this.valorAtual *= -1;
		valor = this.formataNumero(this.valorAtual);
		this.labelResultado.setText(valor);
		this.reiniciarValorAtual = false;
		this.mudarTamanhoLetra();
	}

	@FXML
	void clicarEmVirgula(ActionEvent event) {
		String valor = this.labelResultado.getText().trim();

		if (houveErroDivisaoPor0) {
			return;
		}

		if (terminouComIgual) {
			this.clicarEmC(null);
			valor = "0";
		}

		if (this.reiniciarValorAtual) {
			valor = "0";
		}

		if (!valor.contains(",")) {
			this.labelResultado.setText(valor + ",");
			this.mudarTamanhoLetra();
		}
	}

	@FXML
	void mostrarHistorico(ActionEvent event) {
		new HistoricoControle(historico);
	}

	@FXML
	void pressionarTeclado(KeyEvent event) {
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
