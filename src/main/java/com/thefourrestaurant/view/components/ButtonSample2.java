package com.thefourrestaurant.view.components;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ButtonSample2 extends Button {

	public enum Variant { YELLOW, DARK_YELLOW }

	public ButtonSample2(String text, Variant variant) {
		this(text, variant, 100, 40);
	}

	public ButtonSample2(String text, Variant variant, double prefWidth) {
		this(text, variant, prefWidth, 40);
	}

	public ButtonSample2(String text, Variant variant, double prefWidth, double prefHeight) {
		super(text);
		String bg = switch (variant) {
			case DARK_YELLOW -> "#C6991D";
			case YELLOW -> "#DDB248";
		};

		setStyle(
				"-fx-background-color: " + bg + ";" +
						"-fx-text-fill: #1E424D;" +
						"-fx-font-weight: bold;" +
						"-fx-font-size: 14px;" +
						"-fx-background-radius: 10;" +
						"-fx-cursor: hand;"
		);
		setPrefHeight(prefHeight);
		setPrefWidth(prefWidth);

		DropShadow shadow = new DropShadow();
		shadow.setRadius(4);
		shadow.setOffsetX(0);
		shadow.setOffsetY(4);
		shadow.setColor(Color.rgb(0, 0, 0, 0.25));
		setEffect(shadow);

		setOnMouseEntered(e -> {
			ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
			st.setToX(1.05);
			st.setToY(1.05);
			st.play();
		});

		setOnMouseExited(e -> {
			ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
			st.setToX(1.0);
			st.setToY(1.0);
			st.play();
		});
	}
}
