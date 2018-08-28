import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {
    static TextArea textArea;
    static TextField textField;
    static Stage prStage;
    static ArrayList<String> vacancy = new ArrayList<>();
    static HashSet<String> demands = new HashSet<>();
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        prStage = primaryStage;
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,500,500);
        primaryStage.setScene(scene);

        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setMaxWidth(500);

        textField = new TextField();
        textField.setMaxWidth(150);

        Button btnShowVacancy = new Button("Show used vacancy");
        btnShowVacancy.setOnAction(e ->showVacancy());

        Button btnParse = new Button("Parse site");
        btnParse.setOnAction(e ->parseHTML());

        HBox hbox = new HBox();
        hbox.getChildren().addAll(textField,btnParse,btnShowVacancy);
        borderPane.setTop(hbox);
        borderPane.setCenter(textArea);
        primaryStage.setTitle("My HH Parser");
        primaryStage.show();
    }

    public static void parseHTML(){
        //final String request = "https://hh.ru/search/vacancy?text=java&area=1";
        final String request = search();
        try {

            Document doc = Jsoup.connect(request)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            Elements elements = doc.select("div[class = vacancy-serp-item ]");
            int i=0;
            for (Element el:elements){
                vacancy.add(el.select("a[class = bloko-link HH-LinkModifier]").attr("href"));
                Document tempDoc = Jsoup.connect(el.select("a[class = bloko-link HH-LinkModifier]").attr("href"))
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .referrer(request)
                        .get();
                Elements elements2 = tempDoc.select("div[class = g-user-content ]");
                    elements2 = elements2.select("ul:gt(1)");
                if (elements2.size()>0)
                {
                    Element element = elements2.first();
                    elements2 = element.select("li");
                    for (Element el2:elements2)
                        demands.add(el2.text());

                }else {
                    textArea.appendText("Can`t parse");
                    textArea.appendText(el.select("a[class = bloko-link HH-LinkModifier]").attr("href") + "\n");
                }
                i++;
            }
            for (String st :demands)
                textArea.appendText(st+"\n");
        }catch (Exception e){e.printStackTrace();}


    }
    public static String search(){
        String fromField = textField.getText();
        fromField = fromField.replace(' ','+');
        return "https://hh.ru/search/vacancy?text="+fromField+"&enable_snippets=true&clusters=true&area=1";
    }
    public static void showVacancy(){
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Vacancy links");
        dialogStage.initModality(Modality.NONE);
        TextArea textArea = new TextArea();
        printVacancy(textArea);
        textArea.setMaxWidth(500);
        textArea.setWrapText(true);
        dialogStage.initOwner(prStage);
        VBox vBox = new VBox();
        Scene scene = new Scene(textArea,500,500);
        dialogStage.setScene(scene);
        dialogStage.show();
    }
    public static void printVacancy(TextArea textArea){
        for (String s:vacancy)
            textArea.appendText(s+"\n");

    }
}
