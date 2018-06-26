//package application;
// 
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.collections.ObservableList;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
// 
//public class NoThreadSafeJavaFXUI extends Application {
// 
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            AnchorPane root = new AnchorPane();
//            Label label = new Label("旧的值");
//            Button button = new Button();
// 
//            label.setLayoutX(100);
//            label.setLayoutY(100);
//            label.setPrefWidth(100);
// 
//            button.setLayoutX(100);
//            button.setLayoutY(150);
//            button.setText("开始");
// 
//            button.setOnAction((event) -> {
//                Thread thread = new Thread() {
//                	@Override
//                	public void run() {
//                	    Platform.runLater(() -> {
//                	        label.setText("新的值");
//                	    });
//                	}
//                };
//                thread.setName("thread1");
//                thread.start();
// 
//            });
// 
//            ObservableList<Node> children = root.getChildren();
//            children.add(label);
//            children.add(button);
// 
//            Scene scene = new Scene(root, 400, 400);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
// 
//    public static void main(String[] args) {
//        launch(args);
//    }
//}