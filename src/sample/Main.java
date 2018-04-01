package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import javax.sound.sampled.*;
import java.io.File;


public class Main extends Application {
    final Button recordBtn = new Button("Record");
    final Button stopBtn = new Button("Stop");
    final Button closeBtn = new Button("Close");

    final HBox hbrecordBtn = new HBox(5);
    final HBox hbstopBtn = new HBox(5);
    final HBox hbcloseBtn = new HBox(5);

    final Text actiontarget = new Text();

    final GridPane grid = new GridPane();

    AudioFormat audioFormat;
    TargetDataLine targetDataLine;

    public Main() {// constructor
        recordBtn.setDisable(false);
        stopBtn.setDisable(true);

        recordBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordBtn.setDisable(true);
                stopBtn.setDisable(false);
                //Capture input data from the microphone until the stop button is clicked
                captureAudio();
            }//ends handle
        });//ends EventHandler and setOnAction()

        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordBtn.setDisable(false);
                stopBtn.setDisable(true);
                //Terminate the capturing of input data from the microphone
                targetDataLine.stop();
                targetDataLine.close();
            }//ends handle
        });//ends EventHandler and setOnAction()

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Exiting UI with 0 status
                System.exit(0);
            }//ends handle
        });//ends EventHandler and setOnAction()

    }//ends constructor

    public static void main(String[] args) {
        new Main();
        launch(args);
    }//ends main

    //JavaFX start method to set all participants of UI
    @Override
    public void start(Stage primaryStage) throws Exception {
//      JavaFx Stage class is the top-level JavaFX container
//      Setting UI Title
        primaryStage.setTitle("Speech Recognition for Azerbaijan Language");

//      Making grid lines visible to ease the alignment
//      grid.setGridLinesVisible(true);

//      Setting position of grid layout in the center
        grid.setAlignment(Pos.CENTER);
//      Setting horizontal gap to 10
        grid.setHgap(10);
//      Setting vertical gap to 10
        grid.setVgap(10);
//      Setting 25 padding in each side
        grid.setPadding(new Insets(25, 25, 25, 25));

//      Setting width to recordBtn
        recordBtn.setPrefWidth(75);
//      Setting location of HBox called hbrecordBtn to bottom_left of grid panel
        hbrecordBtn.setAlignment(Pos.BOTTOM_LEFT);
//      Adding recordBtn as a child to hbrecordBtn
        hbrecordBtn.getChildren().add(recordBtn);
//      Adding hbrecordBtn to the panel in 4th row and 0th column
        grid.add(hbrecordBtn, 0, 4);

//      Same comments above suit here as well with minor modification
        stopBtn.setPrefWidth(75);
        hbstopBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbstopBtn.getChildren().add(stopBtn);
        grid.add(hbstopBtn, 1, 4);

//      Same comments above suit here as well with minor modifications
        closeBtn.setPrefWidth(75);
        hbcloseBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbcloseBtn.getChildren().add(closeBtn);
        grid.add(hbcloseBtn, 2, 4);

//      Text area where output can be directed
        grid.add(actiontarget, 1, 6);

//      The JavaFX Scene class is the container for all content
//      Setting width and height of scene and adding grid layout to it
        Scene scene = new Scene(grid, 450, 300);

//      Adding scene to stage
        primaryStage.setScene(scene);

//      Setting title for scene, applying some css properties and adding to the grid
//      The colpan/rowspan property specifies how many columns/rows an element should span across
        Text scenetitle = new Text("Capturing and Processing Voice");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

//      Showing the UI
        primaryStage.show();

    }//ends start method of JavaFX

    private void captureAudio() {
        try {
            //Getting things set up for capture
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat);
            targetDataLine = (TargetDataLine)
                    AudioSystem.getLine(dataLineInfo);

            //Creating a thread to capture the microphone data into an audio file
            //and starting the thread running. It will run until the stop button
            //is clicked. This method will return after starting the thread.
            new CaptureThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }//ends catch
    }//ends captureAudio method


    // This method creates and returns an AudioFormat object for a given set of format
//parameters.
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }//ends getAudioFormat

    //Inner class to capture data from microphone and writ it to an output audio file.
    class CaptureThread extends Thread {
        public void run() {
            final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            final File audioFile = new File("JunkyFunky.wav");

            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                //Writing captured data to a wav file
                AudioSystem.write(
                        new AudioInputStream(targetDataLine),
                        fileType,
                        audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }//ends catch

        }//ends run
    }//ends inner class CaptureThread

/************************************************************/

}//ends outer class Main.java
