package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class MainClient {


    public static void main(String[] args) {
        MainClient mc = new MainClient();
        try {
            mc.run(args);
        }
        catch (Exception e)
        {
         System.out.print("Usage: <ip> <port>\n");
        }
    }
    public void run(String[] args) throws Exception
    {
        final String HOST = System.getProperty("host", args[0]);
        final int PORT = Integer.parseInt(System.getProperty("port", args[1]));

        final SslContext ctx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new InitClient(ctx, HOST, PORT));

            Channel ch = b.connect(HOST, PORT).sync().channel();
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");
                if ("exit".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}

