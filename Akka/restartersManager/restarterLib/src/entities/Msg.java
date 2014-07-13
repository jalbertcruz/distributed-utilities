package entities;

import java.io.Serializable;

/**
 * Clase base de todos los mensajes usados en el sistema.
 * 
 * @author jalbert
 */
public class Msg implements Serializable{
    
    @Override
    public String toString() {
        return "Msg{" + "type=" + this.getClass().getName() + '}';
    }
    
}
