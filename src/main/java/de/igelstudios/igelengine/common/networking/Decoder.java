package de.igelstudios.igelengine.common.networking;

import de.igelstudios.igelengine.common.util.Numbers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class Decoder extends MessageToMessageDecoder<ByteBuf> {
    private int i = 0;
    private int j = 0;
    boolean id;
    private String name = "";
    private boolean lb;
    private Package nPackage;

    private int n = 0;
    private int k = 0;
    private byte[] jb;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        k = in.readableBytes();
        n = 0;
        while (n < k){
            Package p = read(in);
            if(p != null)out.add(p);
        }
    }

    private Package read(ByteBuf in){
        Package p = null;
        if(j == 0){
            if(jb == null){
                jb = new byte[4];
                lb = false;
                i = 0;
            }
            for (int m = 0; m < jb.length && n < k; m++) {
                jb[m] = in.readByte();
                i++;
                n++;
                if(m == 3){
                    lb = true;
                    break;
                }
            }

            if(!lb)return null;
            j = Numbers.toInt(jb);
            lb = true;
            id = true;
        }
        byte m = 1;
        while(n < k && i < j){
            if(m != 1){
                name += new String(new byte[]{m});
            }
            m = in.readByte();
            i++;
            n++;
            if(m == 0){
                id = false;
                break;
            }
        }
        if(!id) {
            if(nPackage == null) nPackage = new Package(name);
            while(n < k && i < j){
                nPackage.write(in.readByte());
                i++;
                n++;
            }
        }
        if(i == j){
            p = nPackage;
            i = 0;
            j = 0;
            name = "";
            nPackage = null;
        }
        return p;
    }
}
