package entities;

import java.util.ArrayList;

/**
 *
 * Mensaje que le envía una estación al controlador.
 *
 * Semántica: respuesta a un mensaje ´MsgConnect´.
 *
 * @author jalbert
 */
public class MsgConnectResponse extends Msg {

    public ArrayList<String> commands = new ArrayList<String>();
}
