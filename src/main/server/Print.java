package server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class Print {
    public Print() {}
    public void PrintAtAll(String msg, Channel CurrentChan, ClientManager clientManager)
    {
        for (Client clt: clientManager.lclient) {
            if (clt.ctx != CurrentChan)
                clt.ctx.writeAndFlush("Player " + clt.id + ": " + msg + '\n');

            else
                clt.ctx.writeAndFlush("You " + clt.id + ": " + msg + '\n');
        }

    }
    public void ServerToAll(String msg, ClientManager clientManager)
    {
        for (Client clt: clientManager.lclient) {
                clt.ctx.writeAndFlush("[SERVER]: " + msg + '\n');
        }
    }

    public void ServerToOne(String msg, Client clt)
    {
        clt.ctx.writeAndFlush("[SERVER]: " + msg + '\n');
    }
}
