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
    public static int bet = 1;


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {

        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(
                                "Hello and have fun in Coinche server !\n");
                        if (clientManager.lclient.size() < 4) {
                            clientManager.add(new Client(MainServer.x, true, ctx));
                            clientManager.lclient.get(MainServer.x - 1).ctx.writeAndFlush("&&You entered in a game\n");
                            ctx.writeAndFlush("May the Odds be ever in your favour\n");
                        } else {
                            clientManager.add(new Client(MainServer.x, false, ctx));
                            ctx.writeAndFlush("Waiting, the game is full\n");
                        }
                        MainServer.x++;
                        ctx.writeAndFlush("There are " + clientManager.lclient.size() + " in the server\n");

                        channels.add(ctx.channel());
                        if (clientManager.lclient.size() == 2) {
                            print.ServerToAll("THE GAME STARTS\n", clientManager);
                            gameManager.f_run(clientManager);
                        }
                    }

                });
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        for (Channel c: channels) {

            if (c != ctx.channel() && !"hand".equals(msg.toLowerCase()))
                print.PrintAtAll(msg, c, clientManager);
            if (msg.toLowerCase().contains("bet".toLowerCase()) && c != ctx.channel())
            {

                gameManager.check_bet(msg.split("\\s+")[1], msg.split("\\s+")[2], clientManager);
                if (gameManager.bet_number != 120 && gameManager.pass != 3 && bet != 4) {
                    gameManager.bet(clientManager);
                }
                if (gameManager.play_turn)
                    gameManager.d_run(clientManager);

            }
            else if (c != ctx.channel() && msg.toLowerCase().contentEquals("hand".toLowerCase())) {
                for (Client clt: clientManager.lclient)
                {
                    if (clt.ctx == ctx)
                        gameManager.check_hand(clt);
                }
            }
            else if (c == ctx.channel() && "ping".equals(msg.toLowerCase()))
                c.writeAndFlush("Pong\n");
            else if ("exit".equals(msg.toLowerCase()) && c == ctx.channel()) {

                c.writeAndFlush("Don't be made bro\n");
                ctx.close();
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}