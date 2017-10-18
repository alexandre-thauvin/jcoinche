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

    private SslContext sslCtx;

    public InitServer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline lpipe = ch.pipeline();

        lpipe.addLast(sslCtx.newHandler(ch.alloc()));

        lpipe.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        lpipe.addLast(new StringDecoder());
        lpipe.addLast(new StringEncoder());
        lpipe.addLast(new ServerHandler());
    }
}