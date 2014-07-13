package entities;

import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * Mensaje que se le envía en el proceso de inicialización al acator de una
 * estación.
 *
 * Semántica: respuesta a un mensaje ´MsgConnect´.
 *
 * @author jalbert
 */
public class MsgInit extends Msg {

    ArrayList<Action> actions;
    final TrayIcon trayIcon;
    ActionListener exitListener;

    public ActionListener getExitListener() {
        return exitListener;
    }

    public void setExitListener(ActionListener exitListener) {
        this.exitListener = exitListener;
    }

    public MsgInit(ArrayList<Action> actions, TrayIcon trayIcon, ActionListener exitListener) {
        this.trayIcon = trayIcon;
        this.actions = actions;
        this.exitListener = exitListener;
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
