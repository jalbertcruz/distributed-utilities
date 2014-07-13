package entities;

/**
 * Entrada de configuración para cada estación a ser controlada.
 * 
 * @author jalbert
 */
public class ConfPCData {

    /**
     * Puerto por el que escucha la aplicación.
     */
    private int port;
    
    /**
     * IP de la estación en que se encuentra.
     */
    private String IP;
    
    /**
     * Identificador de la aplicación en dicho IP.
     */
    private String listenName;
    
    /**
     * Nombre bajo el que aparecerán las acciones en la GUI.
     */
    private String name;

    public ConfPCData(int port, String IP, String listenName, String name) {
        this.port = port;
        this.IP = IP;
        this.listenName = listenName;
        this.name = name;
    }

    public ConfPCData() {
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getListenName() {
        return listenName;
    }

    public void setListenName(String listenName) {
        this.listenName = listenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
