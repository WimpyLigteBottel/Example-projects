package com.example.mediaplayerwimpy;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class HelloController {

  public static boolean isPlay = false;
  public MediaPlayer mediaPlayer;

  @FXML public TextField fieldLocationTextField;

  @FXML public ProgressBar progressBar;

  @FXML public Button playStopButton;
  @FXML private Label fieldLocation;
  @FXML public Slider volumeSlider;

  public HelloController() throws MalformedURLException {
    fieldLocationTextField = new TextField("C:/temp/Skillet-hero.mp3");
    mediaPlayer = loadMediaPlayer();
    volumeSlider = new Slider(0, 0.1, 0.05);

    mediaPlayer.totalDurationProperty().addListener((observableValue, duration, t1) -> {});
  }

  @FXML
  public void onPlayButtonClick() throws MalformedURLException {

    if (isPlay) {
      mediaPlayer.stop();
      isPlay = false;
    } else {
      mediaPlayer = loadMediaPlayer();
      isPlay = true;
      mediaPlayer.play();
      updateVolume();
    }

    // Providing functionality to time slider
    mediaPlayer.currentTimeProperty().addListener(ov -> updatesValues());


  }

  public MediaPlayer loadMediaPlayer() throws MalformedURLException {
    File music = new File(fieldLocationTextField.getText());
    Media media = new Media(music.toURI().toURL().toString());

    return new MediaPlayer(media);
  }


  @FXML
  public void onStopButtonClick(ActionEvent actionEvent) {
    if (isPlay) {
      isPlay = false;
      mediaPlayer.stop();
    }
  }

  @FXML
  public void updateVolume() {

    double value = volumeSlider.getValue();

    System.out.println(value);
    mediaPlayer.setVolume(value);
  }

  // Outside the constructor
  protected void updatesValues()
  {
    Platform.runLater(() -> {
      // Updating to the new time value
      // This will move the slider while running your video
      double currentTime = mediaPlayer.getCurrentTime().toMillis();
      double totalTime = mediaPlayer.getTotalDuration().toMillis();
      progressBar.setProgress(currentTime / totalTime);
    });
  }
}
