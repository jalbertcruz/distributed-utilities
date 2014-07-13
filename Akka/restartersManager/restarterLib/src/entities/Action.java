package entities;

import java.io.File;
import java.io.IOException;

/**
 * Clase que encapsula la semántica de ejecución de los scripts.
 * 
 * @author jalbert
 */
public class Action {

    /**
     * Fichero ejecutable.
     */
    private File actionName;
    
    /**
     * Directorio de trabajo.
     */
    private File wdir;
    
    private boolean called;

    public Action(File actionName, File wdir) {
        this.actionName = actionName;
        this.wdir = wdir;
        called = false;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public File getActionName() {
        return actionName;
    }

    public void setActionName(File actionName) {
        this.actionName = actionName;
    }

    public File getWdir() {
        return wdir;
    }

    public void setWdir(File wdir) {
        this.wdir = wdir;
    }

    @Override
    public String toString() {
        return "Action{" + "actionName=" + actionName + ", wdir=" + wdir.getAbsolutePath() + '}';
    }

    /**
     * Ejecución del script en el directorio de trabajo especificado.
     * 
     * @throws IOException 
     */
    public void excecute() throws IOException {
        
        System.out.println("Ejecutando: " + actionName + ", en :" + wdir.getAbsolutePath());
        
        ProcessBuilder pb = new ProcessBuilder(actionName.getAbsolutePath());
        
        pb.directory(wdir);
        
        pb.start();
        
    }
}
