package com.thefourrestaurant.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockText extends Text {

    public ClockText() {

        setFill(Color.WHITE);
        setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Cập nhật mỗi giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Cập nhật ngay khi khởi tạo
        updateTime();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        setText(date + "\n" + time);
    }
}
