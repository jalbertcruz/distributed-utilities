package restartersmanager;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import entities.MsgConnect;
import entities.MsgConnectResponse;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Actor de la app administradora, encargado de la inicialización.
 *
 * @author jalbert
 */
public class ManagerActor extends UntypedActor {

    /**
     * Layout en el que aparecerán los botones que identifican cada acción a
     * realizar.
     */
    VBox vb;
    /**
     * Actor de la estación controlada asociada.
     */
    ActorRef client;

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {

       log.debug("Entrando msg de ManagerActor: " + message.getClass().getSimpleName());
        /**
         * Procesamiento del mensaje "MsgConnectResponse":
         *
         * - Se recibe la respuesta con el conjunto de acciones que se pueden
         * realizar en la estación asociada.
         *
         * - Se procede a llenar la interfaz con los botones correspondientes.
         */
        switch (message.getClass().getSimpleName()) {
            case "MsgConnectResponse":
                {
                    final MsgConnectResponse resp = (MsgConnectResponse) message;
                    for (String cname : resp.commands) {

                       log.debug("Comando: " + cname);

                        final Button bt = new Button();

                        bt.setText(cname);

                        bt.setOnAction(new ManagerHandler(client, cname));

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                vb.getChildren().add(bt);
                            }
                        });

                    }
                    break;
                }
            case "ManagerActorData":
                {
                    ManagerActorData resp = (ManagerActorData) message;
                    vb = resp.getVb();
                    client = resp.getClient();
                    client.tell(new MsgConnect(), getSelf());
                    
        //            try{
        //                client.tell(new MsgConnect(), getSelf());
        //            }
        //            catch(Exception ex){}
                    break;
                }
        }

    }
//    private static SupervisorStrategy strategy = new OneForOneStrategy(-1, Duration.Inf(),
//            new Function<Throwable, Directive>() {
//                @Override
//                public Directive apply(Throwable t) {
//                    return stop();
//                }
//            });
//
//    @Override
//    public SupervisorStrategy supervisorStrategy() {
//        return strategy;
//    }
}
