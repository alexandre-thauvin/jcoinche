package server;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    List<Client> lclient = new ArrayList<Client>();

    public Client getClientById(int id)
    {
        for (Client clt: lclient)
        {
            if (clt.id == id)
                return clt;
        }
        return null;

    }
    public void add(Client client) {lclient.add(client);}
    public void remove(Client client) {lclient.remove(client);}
    public void copy(Client newClient, Client oldClient) {newClient = oldClient;}
}
