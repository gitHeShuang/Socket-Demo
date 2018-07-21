
import com.alibaba.fastjson.JSON;
import com.socket.config.*;
import com.socket.model.User;

import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        try {
            Listener.getInstance().start();
            System.out.println("客户端等待5秒");
            Thread.sleep(1000 * 5);
            Scanner scanner = new Scanner(System.in);
            System.out.println("------------填写用户信息-------------");
            System.out.println("姓名:");
            User user = new User();
            user.setUserName(scanner.next());
            user.setStatus(1);
            System.out.println("收信人:");
            user.setToUser(scanner.next());
                while (true) {
                    Socket socket = SocketClient.openSocket();
                    System.out.println("消息:");
                    String info = scanner.next();
                    user.setMessage(info);

                    SocketUtil.Send(JSON.toJSONString(user), socket);
                    Thread.sleep(500);
                    System.out.println("------------服务器回复列表------------------");
                    String respstr = SocketUtil.Accept(socket);
                    User resUser = JSON.parseObject(respstr,User.class);
                    System.out.println(resUser.getUserName()+":说" + resUser.getMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
