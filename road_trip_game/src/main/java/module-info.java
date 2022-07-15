/**
 * Module of the program (based on Java 9's modules)
 */
module edu.uoc.trip {
    requires javafx.fxml;
    requires transitive javafx.controls;

    exports com.game.view.gui to javafx.controls, javafx.fxml;
}
