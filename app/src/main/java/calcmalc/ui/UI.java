package calcmalc.ui;

import calcmalc.App;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.nio.file.Path;

public class UI extends Application {

    @Override
    public void start(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-font-size: 1.25em;");
        textArea.setEditable(false);
        textArea.setMinHeight(450);
        textArea.setMaxHeight(450);
        textArea.setMinWidth(640);
        textArea.appendText(App.getGreeting() + "\n\n\n>>> type exit to exit the program\n");

        TextArea history = new TextArea();
        history.setEditable(false);
        history.setMinWidth(350);

        TextField text = new TextField();
        text.setMaxWidth(640);
        text.setMinWidth(640);
        text.setPadding(new Insets(5, 0, 5, 10));

        text.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        text.setOnKeyPressed((ev) -> {
            if (ev.getCode().equals(KeyCode.ENTER)) {
                String input = text.getText().replaceAll("\\s", "");
                if (input.equals("exit")) {
                    System.exit(0);
                }

                App.interpret(input);
                history.appendText(text.getText() + '\n');
                if (App.output.errorOutput != null) {
                    textArea.appendText(">>> " + App.output.errorOutput + '\n');
                } else if (App.output.numOutput != null) {
                    textArea.appendText(">>> " + App.output.numOutput + '\n');
                } else {
                    textArea.appendText(">>> " + App.output.variableOutput + '\n');
                }
                text.clear();
            }
        });

        EventHandler<ActionEvent> eventHandler = (ev) -> {
            Button b = (Button) ev.getSource();
            text.appendText(b.getText() + "( )");
        };

        EventHandler<ActionEvent> eventHandlerA = (ev) -> {
            Button b = (Button) ev.getSource();
            text.appendText(b.getText());
        };

        Button fileDialog = new Button("Select file");

        fileDialog.setOnAction((ev) -> {
            Path file = fileChooser.showOpenDialog(stage).toPath();
            try {
                App.read(file);
                if (App.output.errorOutput != null) {
                    textArea.appendText(">>> " + App.output.errorOutput + '\n');
                } else if (App.output.numOutput != null) {
                    textArea.appendText(">>> " + App.output.numOutput + '\n');
                } else {
                    textArea.appendText(">>> " + App.output.variableOutput + '\n');
                }
                history.appendText("FILE: " + file.getFileName() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        Button button1 = new Button("max");
        Button button2 = new Button("min");
        Button button3 = new Button("log");

        Button button1a = new Button("*");
        Button button2a = new Button("+");
        Button button3a = new Button("-");

        Button button5 = new Button("sqrt");
        Button button6 = new Button("abs");

        Button button4a = new Button("/");
        Button button5a = new Button("^");
        Button button6a = new Button("%");

        Button button7 = new Button("cos");
        Button button8 = new Button("sin");
        Button button9 = new Button("tan");

        button1.setOnAction(eventHandler);
        button2.setOnAction(eventHandler);
        button3.setOnAction(eventHandler);
        button1a.setOnAction(eventHandlerA);
        button2a.setOnAction(eventHandlerA);
        button3a.setOnAction(eventHandlerA);
        button5.setOnAction(eventHandler);
        button6.setOnAction(eventHandler);
        button4a.setOnAction(eventHandlerA);
        button5a.setOnAction(eventHandlerA);
        button6a.setOnAction(eventHandlerA);
        button7.setOnAction(eventHandler);
        button8.setOnAction(eventHandler);
        button9.setOnAction(eventHandler);

        String btnSty = "-fx-font-size: 1.1em; -fx-padding: 3 8; -fx-min-width: 50;";

        HBox btnRow1 = new HBox();

        button1.setStyle(btnSty);
        button2.setStyle(btnSty);
        button3.setStyle(btnSty);
        button1a.setStyle(btnSty);
        button2a.setStyle(btnSty);
        button3a.setStyle(btnSty);

        btnRow1.getChildren().addAll(button1, button2, button3, button1a, button2a, button3a);
        btnRow1.setSpacing(10);
        btnRow1.setPadding(new Insets(10,0,10,0));

        HBox btnRow2 = new HBox();

        button5.setStyle(btnSty);
        button6.setStyle(btnSty);
        button4a.setStyle(btnSty);
        button5a.setStyle(btnSty);
        button6a.setStyle(btnSty);

        btnRow2.getChildren().addAll(button5, button6, button7, button4a, button5a, button6a);
        btnRow2.setSpacing(10);
        btnRow2.setPadding(new Insets(0,0,10,0));

        HBox btnRow3 = new HBox();

        button7.setStyle(btnSty);
        button8.setStyle(btnSty);
        button9.setStyle(btnSty);

        btnRow3.getChildren().addAll(button8, button9, fileDialog);
        btnRow3.setSpacing(10);
        VBox vbBtn = new VBox();

        fileDialog.setStyle(btnSty);

        vbBtn.getChildren().addAll(btnRow1, btnRow2, btnRow3);

        VBox vb1 = new VBox();
        VBox vb2 = new VBox();

        vb1.setMargin(text, new Insets(0, 0, 0, 1));
        vb1.getChildren().addAll(textArea, text);

        vb2.getChildren().addAll(vbBtn, history);
        vb2.setSpacing(10);

        HBox hb = new HBox();
        hb.getChildren().addAll(vb1,vb2);
        hb.setSpacing(5);
        
        Scene scene = new Scene(hb, 1000, 480);
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }
}
