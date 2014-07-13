package restartapps;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.google.gson.Gson;
import com.typesafe.config.ConfigFactory;
import entities.Action;
import entities.MsgAct;
import entities.MsgInit;
import entities.RestartersData;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestartApps extends javax.swing.JFrame implements Bootable {

    private final TrayIcon trayIcon;
    private ActorRef actor;
    private ActorSystem system;

    @Override
    public void startup() {
    }

    @Override
    public void shutdown() {
        system.shutdown();
    }

    class CommandActionListener implements ActionListener {

        String command;

        public CommandActionListener(String command) {
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            MsgAct msgAct = new MsgAct(command);

            System.out.println("Enviando el mensaje: " + msgAct);

            actor.tell(msgAct);

        }
    }

    public RestartApps() throws Exception {

        initComponents();

        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");

        /**
         * Evento a ejecutar cuando se ordene salir de la estación.
         */
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Saliendo...");

                shutdown();

                System.exit(0);

            }
        };

        /**
         * PopupMenu usado para controlar directamente la app.
         */
        PopupMenu popup = new PopupMenu();

        MenuItem defaultItem = new MenuItem("Salir");

        defaultItem.addActionListener(exitListener);

        popup.add(defaultItem);

        trayIcon = new TrayIcon(image, "Restarter", popup);

        trayIcon.setImageAutoSize(true);

        tray.add(trayIcon);
        RestartersData d;
        try (FileReader fr = new FileReader(new File("config.json"))) {
            Gson g = new Gson();
            d = g.fromJson(fr, RestartersData.class);
            System.out.println("Objeto de configración cargado: " + d);
        }

        Map<String, Object> map = new HashMap<>();

        map.put("akka.remote.netty.port", d.getRestarterPort());
//        map.put("akka.remote.netty.hostname", InetAddress.getLocalHost().getHostAddress());
        map.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");

        /**
         * Obtención del sistema de actors en la app.
         */
        system = ActorSystem.create("restarter-app", ConfigFactory.parseMap(map));


        ArrayList<Action> actions = new ArrayList<>();


        /**
         * Inicio del actor ´MonitoredPCActor´
         */
        actor = system.actorOf(new Props(MonitoredPCActor.class), d.getName());

        File file = new File(d.getDir());

        for (File f : file.listFiles()) {

            if (f.isFile() && f.getName().endsWith(".bat")) {

                actions.add(new Action(f, file));

            }
        }

        actor.tell(new MsgInit(actions, trayIcon, exitListener));

        for (Action a : actions) {
            MenuItem mItem = new MenuItem(
                    a.getActionName().getName().substring(0, a.getActionName().getName().length() - 4));
            mItem.addActionListener(new CommandActionListener(a.getActionName().getName()));
            popup.add(mItem);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setType(java.awt.Window.Type.UTILITY);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RestartApps.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                RestartApps restartApps;
                try {
                    restartApps = new RestartApps();
//                    restartApps.setVisible(true);
                    restartApps.setVisible(false);
                } catch (Exception ex) {
                    Logger.getLogger(RestartApps.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}