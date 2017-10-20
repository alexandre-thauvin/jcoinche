package server;

import io.netty.channel.ChannelHandlerContext;

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
    public Client getClientByChannel(ChannelHandlerContext ctx)
    {
        for (Client clt: lclient)
        {
            if (clt.ctx == ctx)
                return clt;
        }
        return null;
    }
    public Client getClientByBegin()
    {
        for (Client clt: lclient)
        {
            if (clt.starter)
                return clt;
        }
        return null;
    }
    public void add(Client client) {lclient.add(client);}
    public void remove(Client client) {lclient.remove(client);}
    public void copy(Client newClient, Client oldClient) {newClient = oldClient;}
}
