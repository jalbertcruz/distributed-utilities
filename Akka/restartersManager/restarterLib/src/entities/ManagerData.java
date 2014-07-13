package entities;

import java.util.ArrayList;

/**
 * Configuración de la aplicación controladora.
 * 
 * @author jalbert
 */
public class ManagerData {

    ArrayList<ConfPCData> pcs;

    Integer managerPort;

    public Integer getManagerPort() {
        return managerPort;
    }

    public void setManagerPort(Integer managerPort) {
        this.managerPort = managerPort;
    }

    public ManagerData() {
        pcs = new ArrayList<ConfPCData>();
    }

    public ArrayList<ConfPCData> getPcs() {
        return pcs;
    }

    public void setPcs(ArrayList<ConfPCData> pcs) {
        this.pcs = pcs;
    }

    @Override
    public String toString() {

        String res = "";

        for (ConfPCData c : pcs) {
            res += c + ", ";
        }

        return "ManagerData{" + "pcs=" + res.substring(0, res.length() - 2) + '}';
    }

    
   
   
}
