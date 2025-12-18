package com.thefourrestaurant.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefourrestaurant.model.PhieuDatBan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class CountdownController {

    private static CountdownController instance;

    private final Map<String, Timeline> timelines = new HashMap<>();
    private final Map<String, List<Label>> labels = new HashMap<>();

    private static final int THOI_GIAN_PHUC_VU_PHUT = 120;

    private CountdownController() {}

    public static CountdownController getInstance() {
        if (instance == null) {
            instance = new CountdownController();
        }
        return instance;
    }

    // 🔥 HÀM CHÍNH
    public void startDangPhucVu(PhieuDatBan pdb, Label lbl) {
    	if (pdb == null || lbl == null || pdb.getNgayDat() == null) return;

        String maPDB = pdb.getMaPDB();
        labels.computeIfAbsent(maPDB, k -> new ArrayList<>()).add(lbl);

        if (timelines.containsKey(maPDB)) return;

        LocalDateTime endTime = pdb.getNgayDat()
                .plusMinutes(THOI_GIAN_PHUC_VU_PHUT);

        Timeline timeline = new Timeline(
            new KeyFrame(javafx.util.Duration.seconds(1), e -> {
                long seconds =
                    Duration.between(LocalDateTime.now(), endTime).getSeconds();

                Platform.runLater(() -> {
                    if (seconds <= 0) {
                        updateLabels(maPDB, "Hết giờ");
                        stop(maPDB);
                        return;
                    }

                    long h = seconds / 3600;
                    long m = (seconds % 3600) / 60;
                    long s = seconds % 60;

                    updateLabels(maPDB,
                        String.format("%02d:%02d:%02d", h, m, s)
                    );
                });
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        timelines.put(maPDB, timeline);
    }

    private void updateLabels(String maPDB, String text) {
        List<Label> list = labels.get(maPDB);
        if (list != null) {
            for (Label lbl : list) {
                lbl.setText(text);
            }
        }
    }

    private void stop(String maPDB) {
        Timeline t = timelines.remove(maPDB);
        if (t != null) t.stop();
        labels.remove(maPDB);
    }
    
    public void unregisterLabel(String maPDB, Label lbl) {
        List<Label> list = labels.get(maPDB);
        if (list != null) {
            list.remove(lbl);
            if (list.isEmpty()) {
                labels.remove(maPDB);
            }
        }
    }

}

