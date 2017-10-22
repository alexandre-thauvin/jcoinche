package server;

import io.netty.channel.Channel;
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
    public Client getClientByChannel(Channel ctx)
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

    public void resetStarter()
    {
        for (Client clt: lclient)
        {
            clt.starter = false;
        }
    }
    public void resetWinFolds()
    {
        for (Client clt: lclient)
        {
            clt.winFolds = false;
        }
    }
    public void add(Client client) {lclient.add(client);}
    public void remove(Client client) {lclient.remove(client);}
}
