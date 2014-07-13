package restartersmanager;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.google.gson.Gson;
import com.typesafe.config.ConfigFactory;
import entities.ConfPCData;
import entities.ManagerData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RestartersManager extends Application implements Bootable {

    public static void main(String[] args) {
        launch(args);
    }
    ArrayList<ActorRef> actors = new ArrayList<>();

    private MenuBar createMenus() {
        MenuBar menuBar = MenuBarBuilder.create().menus(
                MenuBuilder.create().text("Archivo").items(
                MenuItemBuilder.create().text("Establecer conexión").accelerator(KeyCombination.keyCombination("Ctrl+C")).onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                System.out.println("Estableciendo conexión...");

                try {
                    hb = generateJFXBar(hb);
                } catch (Exception ex) {
                    Logger.getLogger(RestartersManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).build(), MenuItemBuilder.create().text("Salir").accelerator(KeyCombination.keyCombination("Ctrl+X")).onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

//                for (ActorRef a : actors) {
//                    a.tell(new MsgEnd());
//                }

                shutdown();

                System.exit(0);
            }
        }).build()).build(),
                MenuBuilder.create().text("Ayuda").items(
                MenuItemBuilder.create().text("Guía del usuario").accelerator(KeyCombination.keyCombination("F1")).onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {

                    // TODO: Guía del usuario

                    String f = "./help/ShowHelp.bat";
                    try (PrintWriter pw = new PrintWriter(new File(f))) {
                        pw.write("START ./help/Manual.doc");
                    }

                    ProcessBuilder pb = new ProcessBuilder(f);

                    pb.start();

                } catch (IOException ex) {
                    Logger.getLogger(RestartersManager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }).build(),
                MenuItemBuilder.create().text("Acerca de...").accelerator(KeyCombination.keyCombination("Ctrl+A")).onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                About a = new About(null, true);

                a.setVisible(true);

            }
        }).build()).build()).build();

        return menuBar;
    }
    HBox hb;

    @Override
    public void start(final Stage stage) throws Exception {

        try {

            System.out.println("Creando fichero de logs...");

            PrintStream fileStream = new PrintStream(new FileOutputStream("log.txt"));

            System.setOut(fileStream);

            System.setErr(fileStream);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RestartersManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }


        //        st.initStyle(StageStyle.UTILITY);
        hb = new HBox();

        Scene scene = SceneBuilder.create().
                width(750).
                height(120).
                root(
                BorderPaneBuilder.create().
                top(VBoxBuilder.create().
                children(createMenus()).build()).
                center(hb).build()).
                build();

        stage.setScene(scene);
        stage.setTitle("Administrador de ejecución remota de scripts");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                shutdown();
            }
        });
        stage.show();

    }

    public HBox generateJFXBar(HBox hb) throws Exception {

        hb.getChildren().clear();

        shutdown();

        ManagerData mData;
        try (FileReader fr = new FileReader(new File("config.json"))) {
            Gson g = new Gson();

            /**
             * Deserealización de la configuración del fichero "config.json"
             */
            mData = g.fromJson(fr, ManagerData.class);
        }

        Map<String, Object> map = new HashMap<>();

        map.put("akka.remote.netty.port", mData.getManagerPort());

        map.put("akka.remote.log-received-messages", "on");

        map.put("akka.loglevel", "DEBUG");
//        map.put("akka.log-config-on-start", "on");
        map.put("akka.actor.debug.autoreceive", "on");

//        map.put("akka.remote.netty.hostname", InetAddress.getLocalHost().getHostAddress());
        map.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");

        /**
         * Obtención del sistema de actors en la app.
         */
        system = ActorSystem.create("LookupApplication", ConfigFactory.parseMap(map));

        for (ConfPCData pcd : mData.getPcs()) {

            System.out.println("Cargando datos de la PC: " + pcd.getIP());

            /**
             * Establecimiento de la referencia al actor de la estación a
             * controlar.
             */
            ActorRef client = null;

            client = system.actorFor(
                    "akka://restarter-app@" + pcd.getIP() + ":" + pcd.getPort() + "/user/"
                    + pcd.getListenName());

            actors.add(client);
//            System.out.println("Datos cliente: " + pcd.getListenName() + " - " + pcd.getIP() + " - " + pcd.getPort());

            VBox vb = new VBox();

            /**
             * Actor local y temporal para la inicialización de la interfaz.
             */
            ActorRef ref = system.actorOf(new Props(ManagerActor.class));

            ref.tell(new ManagerActorData(vb, client));

            System.out.println("Creando panel con los procesos del cliente: " + pcd.getListenName());

            TitledPane tp = new TitledPane(pcd.getName(), vb);

            tp.expandedProperty().set(true);

//            node = tp;

//            }else{
//                node = new Label("Error conectando a: " + pcd.getName());
//            }

            hb.getChildren().add(tp);
        }

        return hb;
    }

    @Override
    public void startup() {
    }
    private ActorSystem system = null;

    @Override
    public void shutdown() {
        if (system != null) {
            system.shutdown();
        }
    }
}
