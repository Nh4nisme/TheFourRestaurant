package com.thefourrestaurant.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ClockText extends Text {

    // Danh sách callback để UI đăng ký nhận tín hiệu mỗi giây
    private final List<Runnable> tickListeners = new ArrayList<>();

    // Singleton để các màn hình dùng chung
    private static ClockText instance;

    public static ClockText getInstance() {
        if (instance == null) instance = new ClockText();
        return instance;
    }

    private ClockText() {
        setFill(Color.WHITE);
        setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Cập nhật mỗi giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    updateTime();
                    notifyTickListeners();  // gọi update countdown toàn hệ thống
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
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

    // UI sử dụng countdown sẽ đăng ký vào đây
    public void addTickListener(Runnable r) {
        tickListeners.add(r);
    }

    private void notifyTickListeners() {
        for (Runnable r : tickListeners) r.run();
    }
}
