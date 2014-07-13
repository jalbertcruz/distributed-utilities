package entities;

/**
 * Parte de la configuración de una estación controlada.
 *
 * @author jalbert
 */
public class RestartersData {

    Integer restarterPort;

    public Integer getRestarterPort() {
        return restarterPort;
    }

    public void setRestarterPort(Integer restarterPort) {
        this.restarterPort = restarterPort;
    }

    /**
     * El directorio en el que se encuentran los scripts que se quieren ejecutar
     * remoto.
     */
    private String Dir;
    /**
     * El nombre bajo el que escuchará el proceso en la estación controlada.
     */
    private String name;

    public RestartersData() {
    }

    public String getDir() {
        return Dir;
    }

    public void setDir(String Dir) {
        this.Dir = Dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RestartersData{" + "Dir=" + Dir + ", name=" + name + '}';
    }
}
