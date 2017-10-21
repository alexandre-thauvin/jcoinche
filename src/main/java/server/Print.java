package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;

public class Print {
    public Print() {}
    public void PrintAtAll(String msg, ClientManager clientManager, ChannelHandlerContext ctx)
    {
        for (Client clt: clientManager.lclient) {
          if (clt.ctx != ctx.channel())
                clt.ctx.writeAndFlush("Player " + clientManager.getClientByChannel(ctx.channel()).id + ": " + msg + '\n');
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
