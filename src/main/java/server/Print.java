package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;

public class Print {
    public Print() {}
    public void PrintAtAll(String msg, ChannelGroup channels, ClientManager clientManager, ChannelHandlerContext ctx)
    {
        for (Channel c: channels) {
          if (c != ctx.channel())
                c.writeAndFlush("Player " + clientManager.getClientByChannel(ctx).id + ": " + msg + '\n');
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

    public void  PrintToYou(String msg, ChannelHandlerContext ctx, ClientManager clientManager)
    {
        ctx.writeAndFlush("You: " + msg + '\n');
    }
}
