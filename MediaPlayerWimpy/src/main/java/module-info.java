module com.example.mediaplayerwimpy {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.bootstrapfx.core;
  requires javafx.media;

  opens com.example.mediaplayerwimpy to
      javafx.fxml;

  exports com.example.mediaplayerwimpy;
}
