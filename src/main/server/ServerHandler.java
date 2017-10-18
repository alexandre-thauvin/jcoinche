package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private ClientManager clientManager;
    public ServerHandler(ClientManager clientManager) {this.clientManager = clientManager;}
    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameManager     gameManager = new GameManager();
    private Print           print = new Print();
    static int x = 0;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(
                                "Hello and have fun in Coinche server !\n");
                        if (clientManager.lclient.size() < 4) {
                            clientManager.add(new Client(x, true, ctx));
                            clientManager.lclient.get(x).ctx.writeAndFlush("COUCOUYou entered in a game\n");
                            ctx.writeAndFlush("May the Odds be ever in your favour\n");
                        } else {
                            clientManager.add(new Client(x, false, ctx));
                            ctx.writeAndFlush("Waiting, the game is full\n");
                        }
                        ctx.writeAndFlush("There are " + clientManager.lclient.size() + " in the server\n");

                        channels.add(ctx.channel());
                        if (clientManager.lclient.size() == 2) {
                            print.ServerToAll("THE GAME STARTS\n", clientManager);
                            gameManager.run(clientManager);
                        }
                        x++;
                    }

                });
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        for (Channel c: channels) {
            if (c != ctx.channel()) {
                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
            }
            else if (c == ctx.channel() && "ping".equals(msg.toLowerCase()))
                c.writeAndFlush("Pong\n");
            else if ("exit".equals(msg.toLowerCase()) && c == ctx.channel()) {

                c.writeAndFlush("Don't be made bro\n");
                ctx.close();
                break;
            }
            else {
                c.writeAndFlush("You: " + msg + '\n');
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}