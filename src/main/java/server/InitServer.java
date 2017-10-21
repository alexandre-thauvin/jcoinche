package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;


public class InitServer extends ChannelInitializer<SocketChannel> {

    private SslContext ctx;
    private ClientManager clientManager;

    public InitServer(SslContext ctx, ClientManager clientManager) {
        this.ctx = ctx;
        this.clientManager = clientManager;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline lpipe = ch.pipeline();

        lpipe.addLast(ctx.newHandler(ch.alloc()));

        lpipe.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        lpipe.addLast(new StringDecoder());
        lpipe.addLast(new StringEncoder());
        lpipe.addLast(new ServerHandler(clientManager));
    }
}