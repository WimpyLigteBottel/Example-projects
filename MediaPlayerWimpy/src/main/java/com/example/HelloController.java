package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class HelloController {

  public static boolean isPlaying = false;
  public static boolean isPaused = false;

  public MediaPlayer mediaPlayer;

  @FXML public TextField fieldLocationTextField;

  @FXML public ProgressBar progressBar;

  @FXML public Button playStopButton;
  @FXML private Label fieldLocation;
  @FXML public Slider volumeSlider;

  public HelloController() throws MalformedURLException {
    fieldLocationTextField = new TextField("C:/temp/Skillet-hero.mp3");
    mediaPlayer = loadMediaPlayer();
    volumeSlider = new Slider(0, 0.11, 0.0555);

    mediaPlayer.totalDurationProperty().addListener((observableValue, duration, t1) -> {});
  }

  @FXML
  public void onPlayButtonClick() throws MalformedURLException {

    if (isPlaying && isPaused) {
      mediaPlayer.play();
      isPaused = false;
    } else{

      mediaPlayer.stop();
      mediaPlayer = loadMediaPlayer();
      isPlaying = true;
      mediaPlayer.play();
      updateVolume();
      mediaPlayer.currentTimeProperty().addListener(ov -> updatesValues());
    }
  }

  public MediaPlayer loadMediaPlayer() throws MalformedURLException {
    File music = new File(fieldLocationTextField.getText());
    Media media = new Media(music.toURI().toURL().toString());

    return new MediaPlayer(media);
  }

  @FXML
  public void onStopButtonClick(ActionEvent actionEvent) {
    if (!isPaused && isPlaying) {
      isPaused = true;
      mediaPlayer.pause();
    }
  }

  @FXML
  public void updateVolume() {

    double value = volumeSlider.getValue()+.01;

    System.out.println(value);
    mediaPlayer.setVolume(value);
  }

  // Outside the constructor
  protected void updatesValues() {
    Platform.runLater(
        () -> {
          // Updating to the new time value
          // This will move the slider while running your video
          double currentTime = mediaPlayer.getCurrentTime().toMillis();
          double totalTime = mediaPlayer.getTotalDuration().toMillis();
          progressBar.setProgress(currentTime / totalTime);

          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
  }
}
