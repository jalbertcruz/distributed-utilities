package entities;

/**
 * Mensaje que le envía el controlador a las estaciones.
 * 
 * Semántica: mandar a ejecutar un comando dado.
 * 
 * @author jalbert
 */
public class MsgAct extends Msg{

    /**
     * El comando a ejecutar.
     */
    private String command;

    public MsgAct(String command) {
        this.command = command;
    }

    
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "MsgAct{" + "command=" + command + '}';
    }
    
}
