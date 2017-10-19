package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class Print {
    public Print() {}
    public void PrintAtAll(String msg, Channel CurrentChan, ClientManager clientManager, ChannelHandlerContext ctx)
    {
        //for (Client clt: clientManager.lclient) {
          //  if (clt.ctx.channel() != CurrentChan)
                CurrentChan.writeAndFlush("Player " + clientManager.getClientByChannel(ctx).id + ": " + msg + '\n');
//        }

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

    public void  PrintToYou(String msg, Channel CurrentChan, ClientManager clientManager)
    {
        CurrentChan.writeAndFlush("You: " + msg + '\n');
    }
}
