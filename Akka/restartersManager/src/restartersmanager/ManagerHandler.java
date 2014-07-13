package restartersmanager;

import akka.actor.ActorRef;
import entities.MsgAct;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ManagerHandler implements EventHandler<ActionEvent> {
    
    ActorRef client;
    MsgAct command;
    
    public ManagerHandler(ActorRef client, String command) {
        this.client = client;
        this.command = new MsgAct(command);
    }
    
    @Override
    public void handle(ActionEvent ae) {
        client.tell(command);
    }
}
