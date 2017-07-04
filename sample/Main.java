package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Stage stage;
    private Menu mFile;
    private MenuItem iImport, iExport;
    private Canvas importCanvas, resultsCanvas;
    private CheckBox[] checkBoxes;

    private Image importImage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        BorderPane mainLayout = new BorderPane();

        mFile = new Menu("File");
        iImport = new MenuItem("Import");
        iExport = new MenuItem("Export");
        mFile.getItems().addAll(iImport, iExport);
        MenuBar menuBar = new MenuBar(mFile);

        iImport.setOnAction(event -> importImageFromFile());
        iExport.setOnAction(event -> exportImages());

        mainLayout.setTop(menuBar);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setVgap(30);

        importCanvas = initImportImageCanvas();
        gridPane.add(importCanvas, 1, 1);

        VBox optionsBox = initOptionsBox();
        gridPane.add(optionsBox, 2, 1);

        resultsCanvas = initResultsCanvas();
        gridPane.add(resultsCanvas, 1, 2, 2, 1);

        mainLayout.setCenter(gridPane);


        stage.setTitle("Android Icon Exporter");
        stage.setResizable(false);
        stage.setScene(new Scene(mainLayout, 720, 720));
        stage.show();

    }

    private Canvas initImportImageCanvas() {
        Canvas canvas = new Canvas(300, 300);
        canvas.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return canvas;
    }

    private VBox initOptionsBox() {
        checkBoxes = new CheckBox[6];
        checkBoxes[0] = new CheckBox("ldpi");
        checkBoxes[1] = new CheckBox("mdpi");
        checkBoxes[2] = new CheckBox("hdpi");
        checkBoxes[3] = new CheckBox("xhdpi");
        checkBoxes[4] = new CheckBox("xxhdpi");
        checkBoxes[5] = new CheckBox("xxxhdpi");
        for (CheckBox cb : checkBoxes) {
            cb.setSelected(true);
            cb.setOnAction(this);
        }
        drawSelectedSizes();
//        FlowPane pane = new FlowPane(10, 10, cbLDPI, cbMDPI, cbHDPI, cbXHDPI, cbXXHDPI, cbXXXHDPI);
//        pane.setPadding(new Insets(50));
//        pane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.MEDIUM)));
//        GridPane.setMargin(pane, new Insets(0, 30, 0, 0));
        VBox box = new VBox(10, checkBoxes);
        box.setPadding(new Insets(30));
        box.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.MEDIUM)));
        GridPane.setMargin(box, new Insets(0, 30, 0, 0));
        return box;
    }

    private Canvas initResultsCanvas() {
        Canvas canvas = new Canvas(660, 300);
        clearCanvas(canvas, Color.WHITE, true);
        return canvas;
    }

    private void importImageFromFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(stage);
        if (file.isFile()) {
            importImage = new Image(file.toURI().toString());
            if (importImage.getWidth() != importImage.getHeight()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Image file must be a square!");
                alert.show();
                return;
            }
            drawImportImage();
            drawSelectedSizes();
        }
    }

    private void drawImportImage() {
        clearCanvas(importCanvas, Color.LIGHTGRAY, false);
        if (importImage == null) return;
        GraphicsContext g = importCanvas.getGraphicsContext2D();
        double x = 10;
        double y = 10;
        double w = importCanvas.getWidth() - 2 * x;
        double h = importCanvas.getHeight() - 2 * y;
        g.drawImage(importImage, x, y, w, h);
    }

    private void clearCanvas(Canvas canvas, Color color, boolean border) {
        canvas.getGraphicsContext2D().setFill(color);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (border) {
            canvas.getGraphicsContext2D().setStroke(Color.LIGHTGRAY);
            canvas.getGraphicsContext2D().setLineWidth(5);
            canvas.getGraphicsContext2D().strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    private void drawSelectedSizes() {
        if (importImage == null) return;
        ArrayList<CheckBox> selected = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) selected.add(cb);
        }
        double offset = 10, padding = 10;
        GraphicsContext g = resultsCanvas.getGraphicsContext2D();
        clearCanvas(resultsCanvas, Color.WHITE, true);
        for (CheckBox cb : selected) {
            switch (cb.getText()) {
                case "ldpi":
                    g.drawImage(importImage, offset, padding, 36, 36);
                    offset += 36 + padding;
                    break;
                case "mdpi":
                    g.drawImage(importImage, offset, padding, 48, 48);
                    offset += 48 + padding;
                    break;
                case "hdpi":
                    g.drawImage(importImage, offset, padding, 72, 72);
                    offset += 72 + padding;
                    break;
                case "xhdpi":
                    g.drawImage(importImage, offset, padding, 96, 96);
                    offset += 96 + padding;
                    break;
                case "xxhdpi":
                    g.drawImage(importImage, offset, padding, 144, 144);
                    offset += 144 + padding;
                    break;
                case "xxxhdpi":
                    g.drawImage(importImage, offset, padding, 192, 192);
                    offset += 192 + padding;
                    break;
            }
        }
    }

    private void exportImages() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(stage);
        if (directory == null) return;
        String path = directory.getAbsolutePath();

        if (importImage == null) return;
        ArrayList<CheckBox> selected = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isSelected()) selected.add(cb);
        }
        File file;
        BufferedImage image = SwingFXUtils.fromFXImage(importImage, null);
        for (CheckBox cb : selected) {
            switch (cb.getText()) {
                case "ldpi":
                    file = new File(path + File.separator + "drawable-ldpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 36, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
                case "mdpi":
                    file = new File(path + File.separator + "drawable-mdpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 48, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
                case "hdpi":
                    file = new File(path + File.separator + "drawable-hdpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 72, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
                case "xhdpi":
                    file = new File(path + File.separator + "drawable-xhdpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 96, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
                case "xxhdpi":
                    file = new File(path + File.separator + "drawable-xxhdpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 144, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
                case "xxxhdpi":
                    file = new File(path + File.separator + "drawable-xxxhdpi");
                    if (file.exists() || file.mkdir()) {
                        exportSingleImage(image, 192, file.getAbsolutePath() + File.separator + "ic_launcher.png");
                    } else {
                        System.err.println("Not a directory!");
                    }
                    break;
            }
        }
    }

    private void exportSingleImage(BufferedImage image, int size, String path) {
        BufferedImage newImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
        g.dispose();
        try {
            ImageIO.write(newImage, "png", new File(path));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Couldn't export image!");
            alert.show();
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent event) {
        drawSelectedSizes();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
