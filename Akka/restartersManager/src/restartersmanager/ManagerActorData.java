package restartersmanager;

import akka.actor.ActorRef;
import java.io.Serializable;
import javafx.scene.layout.VBox;

/**
 * Mensaje "ManagerActorData"
 * 
 * @author jalbert
 */
class ManagerActorData implements Serializable{

    VBox vb;
    ActorRef client;

    public ManagerActorData(VBox vb, ActorRef client) {
        this.vb = vb;
        this.client = client;
    }

    public ActorRef getClient() {
        return client;
    }

    public void setClient(ActorRef client) {
        this.client = client;
    }

    public VBox getVb() {
        return vb;
    }

    public void setVb(VBox vb) {
        this.vb = vb;
    }
    
}
