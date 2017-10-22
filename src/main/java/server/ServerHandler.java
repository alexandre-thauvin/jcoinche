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
    static ClientManager clientManager;
    public ServerHandler(ClientManager clientManager) {this.clientManager = clientManager;}
    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private GameManager     gameManager = new GameManager();
    private Print           print = new Print();
    static Table                   table = new Table();
    static int bet = 1;
    static int bet_number = 79;
    int pass = 0;
    static int indexPlayer = 0;
    Rules rules = new Rules();
    Deck deck = new Deck();

    public static int getBet() {
        return bet;
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
                            clientManager.add(new Client(MainServer.x, true, ctx.channel()));
                            clientManager.lclient.get(MainServer.x - 1).ctx.writeAndFlush("You entered in a game\n");
                            ctx.writeAndFlush("May the Odds be ever in your favour\n");
                        } else {
                            clientManager.add(new Client(MainServer.x, false, ctx.channel()));
                            ctx.writeAndFlush("Waiting, the game is full\n");
                        }
                        MainServer.x++;
                        ctx.writeAndFlush("There are " + clientManager.lclient.size() + " in the server\n");
                        ctx.writeAndFlush("You are in the team " + clientManager.getClientById(MainServer.x - 1).team + '\n');
                        channels.add(ctx.channel());
                        if (clientManager.lclient.size() == 4) {
                            print.ServerToAll("THE GAME STARTS\n", clientManager);
                            gameManager.fRun(clientManager);
                        }
                    }

                });
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            if (msg.toLowerCase().contains("bet".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx == ctx.channel())
            {
                    if (msg.toLowerCase().contains("pass")) {
                        pass++;
                        bet++;
                        if (indexPlayer == 3)
                            indexPlayer = 0;
                        else
                            indexPlayer++;
                        gameManager.bet(clientManager);
                    } else if (gameManager.checkBet(msg.split("\\s+")[1], msg.split("\\s+")[2], clientManager)) {
                        gameManager.bet(clientManager);
                }
                print.PrintAtAll(msg, clientManager, ctx);
            }
            else if (msg.toLowerCase().contains("bet".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx != ctx.channel())
                print.ServerToOne("It's not your turn\n", clientManager.getClientByChannel(ctx.channel()));
            else if (msg.toLowerCase().contains("put".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx == ctx.channel())
            {
                    if (rules.checkPut(msg)) {
                        print.PrintAtAll(msg, clientManager, ctx);
                        if (rules.checkFolds())
                        {
                            if (!rules.checkWinParty()) {
                                gameManager.checkHand(clientManager.lclient.get(indexPlayer));
                                if (rules.checkEndTurn()) {
                                    rules.countScoreParty();
                                    deck.create();
                                    deck.distribution(clientManager.lclient);
                                    bet = 1;
                                    clientManager.resetStarter();
                                    clientManager.resetContrat();
                                    clientManager.lclient.get(0).starter = true;
                                    pass = 0;
                                    bet_number = 79;
                                    indexPlayer = 0;
                                    print.ServerToAll("BET STARTS\n", clientManager);
                                    print.ServerToAll("Player " + (clientManager.lclient.get(0).id) + " must bet\n", clientManager);
                                    print.ServerToOne("Usage: bet <mise> <suite>\n", clientManager.lclient.get(0));
                                }
                            }
                        }
                        else {
                            print.ServerToAll("Player " + (ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).id) + " must play\n", ServerHandler.clientManager);
                            print.ServerToOne("Usage: put <value> <suite>\n\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
                            gameManager.checkHand(clientManager.lclient.get(indexPlayer));
                        }

                    }
            }
            else if (msg.toLowerCase().contains("put".toLowerCase()) && clientManager.lclient.get(indexPlayer).ctx != ctx.channel())
                print.ServerToOne("It's not your turn\n", clientManager.getClientByChannel(ctx.channel()));
            else if (msg.toLowerCase().contentEquals("hand".toLowerCase())) {
                        gameManager.checkHand(clientManager.getClientByChannel(ctx.channel()));
            }
            else if (msg.toLowerCase().contentEquals("table".toLowerCase())) {
                table.checkTable(clientManager.getClientByChannel(ctx.channel()));
            }
            else if (msg.toLowerCase().contentEquals("contract".toLowerCase())) {
                print.ServerToOne("contract: " + table.contrat + " " + table.atout + "\n", clientManager.getClientByChannel(ctx.channel()));
            }
            else if (clientManager.lclient.get(indexPlayer).ctx == ctx.channel() && "ping".equals(msg.toLowerCase()))
                clientManager.lclient.get(indexPlayer).ctx.writeAndFlush("Pong\n");
            else if ("exit".equals(msg.toLowerCase())) {

                ctx.writeAndFlush("Don't be made bro\n");
                clientManager.lclient.remove(clientManager.getClientByChannel(ctx.channel()));
                print.ServerToAll("Looking for a new player\n", clientManager);
                ctx.close();
        }
        else
            print.PrintAtAll(msg, clientManager, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}