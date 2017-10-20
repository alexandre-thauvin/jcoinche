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
    public static int bet_number = 79;
    public static int pass = 0;
    public static int indexPlayer = 0;

    public void changeIndexPlayer(int index)
    {
        indexPlayer = index;
    }

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
                            clientManager.lclient.get(MainServer.x - 1).ctx.writeAndFlush("You entered in a game\n");
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
            if (msg.toLowerCase().contains("bet".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx.channel() == ctx.channel())
            {
                    if (msg.toLowerCase().contains("pass")) {
                        pass++;
                        bet++;
                        indexPlayer++;
                        if (pass == clientManager.lclient.size() - 1)
                            gameManager.bet(clientManager);
                    } else if (gameManager.check_bet(msg.split("\\s+")[1], msg.split("\\s+")[2], clientManager)) {
                        gameManager.bet(clientManager);
                }
                print.PrintAtAll(msg, channels, clientManager, ctx);
            }
            else if (msg.toLowerCase().contains("bet".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx.channel() != ctx.channel())
                print.ServerToOne("It's not your turn\n", clientManager.getClientByChannel(ctx));
            if (gameManager.play_turn) {
                changeIndexPlayer(clientManager.getClientByBegin().id);
                for (Client clt: clientManager.lclient)
                {
                    if (clt.starter)
                        indexPlayer = clt.id - 1;

                }
                gameManager.d_run(clientManager);
            }

            else if (clientManager.lclient.get(indexPlayer).ctx != ctx.channel() && msg.toLowerCase().contentEquals("hand".toLowerCase())) {
                for (Client clt: clientManager.lclient)
                {
                    if (clt.ctx == ctx)
                        gameManager.check_hand(clt);
                }
            }
            else if (clientManager.lclient.get(indexPlayer).ctx == ctx.channel() && "ping".equals(msg.toLowerCase()))
                clientManager.lclient.get(indexPlayer).ctx.writeAndFlush("Pong\n");
            else if ("exit".equals(msg.toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx == ctx.channel()) {

                clientManager.lclient.get(indexPlayer).ctx.writeAndFlush("Don't be made bro\n");
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}