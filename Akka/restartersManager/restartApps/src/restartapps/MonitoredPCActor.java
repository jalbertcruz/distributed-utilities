package restartapps;

import akka.actor.UntypedActor;
import entities.Action;
import entities.MsgAct;
import entities.MsgConnectResponse;
import entities.MsgInit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Actor de la app en la estación controlada.
 *
 * @author jalbert
 */
public class MonitoredPCActor extends UntypedActor {

    /**
     * Conjunto de acciones (scripts a ejecutar) de la estación.
     */
    HashMap<String, Action> actions;
    TrayIcon trayIcon;
    ActionListener exitListener;

    public MonitoredPCActor() {
        actions = new HashMap<>();
        trayIcon = null;
    }
    /**
     * Notificación de cada acción que se realice en la estación.
     */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            trayIcon.displayMessage("Ejecutando script:",
                    e.getActionCommand(),
                    TrayIcon.MessageType.INFO);
        }
    };

    @Override
    public void onReceive(Object message) throws IOException {
        //        System.out.println("Entrando msg: " + message.getClass().getSimpleName());
        /**
         * Procesamiento del mensaje "MsgConnect":
         *
         * - Con él se recibe la solicitud del listado de acciones que él es
         * capaz de ejecutar.
         *
         * - Como respuesta se envían dicjas acciones.
         */
        switch (message.getClass().getSimpleName()) {
            case "MsgConnect":
                MsgConnectResponse resp = new MsgConnectResponse();
                for (Action a : actions.values()) {
                    resp.commands.add(a.getActionName().getName());
                }
                System.out.println("me lo envio: " + getSender().toString());
                getSender().tell(resp);
                break;
            case "MsgInit":
                MsgInit mi = (MsgInit) message;
                trayIcon = mi.getTrayIcon();
                trayIcon.addActionListener(actionListener);
                exitListener = mi.getExitListener();
                actions.clear();
                for (Action a : mi.getActions()) {
                    actions.put(a.getActionName().getName(), a);
                }
                break;
            case "MsgAct":
                MsgAct ma = (MsgAct) message;
                String command = ma.getCommand();
                if (actionListener != null) {
                    actionListener.actionPerformed(new ActionEvent(this, 0, command));
                }
                System.out.println("Comando: " + command);
                Action a = actions.get(command);
                if (a.isCalled()) {
                    boolean f = true;
                    for (Entry<String, Action> en : actions.entrySet()) {
                        Action v = en.getValue();
                        if (!v.isCalled()) {
                            f = false;
                            System.out.println("Una false...");
                        }
                    }

                    if (f) {
                        System.out.println("Todos son true...");
                        for (Entry<String, Action> en : actions.entrySet()) {
                            Action v = en.getValue();
                            v.setCalled(false);
                        }
                        a.excecute();
                        a.setCalled(true);

                    }
                } else {
                    a.excecute();
                    a.setCalled(true);
                }

    //                imprimirDict();
                break;
            case "MsgEnd":
                exitListener.actionPerformed(null);
                break;

        }


    }
//    private void imprimirDict() {
//
//        System.out.println("Imprimiendo dic:");
//
//        Iterator<String> iterator = actions.keySet().iterator();
//        int i = 0;
//        while (iterator.hasNext()) {
//            System.out.println("Iterando " + i);
//            i++;
//            String string = iterator.next();
//            System.out.println(string + " : " + actions.get(string));
//        }
//
//        System.out.println("Acabe con i = " + i);
//    }
}
