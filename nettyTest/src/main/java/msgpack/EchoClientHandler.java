package msgpack; /**
 *
 */

import java.io.UnsupportedEncodingException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zyd
 * @date 2019年1月17日 下午6:34:05
 * @ClassName: TimeClientHandler
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private int sendNum;

    public EchoClientHandler(int sendNum) {
        this.sendNum = sendNum;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        UserInfo[] userInfos = getUserInfo();
        for (UserInfo userInfo : userInfos) {
            System.out.println(userInfo.getAge() + userInfo.getName());
            ctx.write(userInfo);
        }
        ctx.flush();
    }

    private UserInfo[] getUserInfo() {
        UserInfo[] userInfos = new UserInfo[sendNum];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNum; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABDFEF ---> " + i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        System.out.println(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
