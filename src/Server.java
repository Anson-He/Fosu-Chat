import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static void main(String[] args) {
        //动态数组保存客户端处理的线程
        Vector<UserThread> vector = new Vector <>();
        //固定大小的线程池，用来处理不同客户端
        ExecutorService es = Executors.newFixedThreadPool(50);
        //创建服务器端的Socket
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress("172.19.37.79", 12001));
            System.out.println("服务器已启动，正在等待连接...");
            while(true){
                //接受客户端的Socket，若没有，阻塞在这
                Socket socket = server.accept();
                //每来一个客户端，创建一个线程处理它
                UserThread user = new UserThread(socket,vector);
                es.execute(user);  //开启线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 客户端处理线程：
 */
class UserThread implements Runnable{
    private String name; //客户端的用户名称，唯一
    private Socket socket;
    private Vector<UserThread> vector;   //客户端处理线程的集合
    private ObjectInputStream oIn;    //输入流
    private ObjectOutputStream oOut;  //输出流
    private boolean flag = true;  //标记

    public UserThread(Socket socket, Vector<UserThread> vector) {
        this.socket = socket;
        this.vector = vector;
        vector.add(this);    //把当前线程也加入vector中
    }

    @Override
    public void run() {
        try {
            //1、构造输入输出流
            System.out.println("客户端：" + socket.getInetAddress().getHostAddress() + "已连接！");
            oIn = new ObjectInputStream(socket.getInputStream());
            oOut = new ObjectOutputStream((socket.getOutputStream()));
            //2、循环读取
            while(flag){
                //读取消息对象
                Message message = (Message) oIn.readObject();
                //获取消息类型，登录还是发送消息
                int type = message.getType();
                //3、判断
                switch (type){
                    //如果是发送消息
                    case MessageType.TYPE_SEND:
                        String to = message.getTo();//发送给谁
                        UserThread ut;
                        //遍历vector，找到接收信息的客户端
                        int size = vector.size();
                        for (int i = 0; i < size+1; i++) {
                            try{
                                ut = vector.get(i);
                                System.out.println(ut.name);
                                //如果名字相同，且不是自己，就把信息发给它
                                if(to.equals(ut.name) && ut != this){
                                    ut.oOut.writeObject(message); //发送消息对象
                                    //把发送信息记录进数据库
                                    if(!message.getInfo().equals("753621459530154984512061561894984561321") && !message.getInfo().equals("321456987456321123654789")) {
                                        System.out.println("正在储存发送消息");
                                        Connection con;
                                        PreparedStatement ps0 = null;
                                        con = GetDBConnection.getConnection();//连接数据库
                                        System.out.println("数据库连接成功");
                                        try {
                                            String s0 = "insert into recod(f,t,i) values(?,?,?)";
                                            ps0 = con.prepareStatement(s0);
                                            ps0.setString(1, message.getFrom().substring(0,11));
                                            ps0.setString(2, message.getTo().substring(0,11));
                                            ps0.setString(3, message.getInfo());
                                            ps0.executeUpdate();//执行语句
                                            System.out.println("数据库更新成功!");
                                            con.close();
                                            break;
                                        } catch (SQLException eq) {
                                            eq.printStackTrace();
                                        }
                                    }
                                    break;
                                }
                            }
                            catch(Exception e){
                                for(int p=0;p<size+1;p++){
                                    try{
                                        ut = vector.get(p);
                                        if(to.substring(0,11).equals(ut.name.substring(0,11)) && ut != this){
                                            message.setTo(message.getTo().substring(0,11));
                                            ut.oOut.writeObject(message); //发送消息对象
                                            //把发送信息记录进数据库
                                            if(!message.getInfo().equals("753621459530154984512061561894984561321") && !message.getInfo().equals("321456987456321123654789")) {
                                                System.out.println("正在储存发送消息");
                                                Connection con;
                                                PreparedStatement ps0 = null;
                                                con = GetDBConnection.getConnection();//连接数据库
                                                System.out.println("数据库连接成功");
                                                try {
                                                    String s0 = "insert into recod(f,t,i) values(?,?,?)";
                                                    ps0 = con.prepareStatement(s0);
                                                    ps0.setString(1, message.getFrom().substring(0,11));
                                                    ps0.setString(2, message.getTo().substring(0,11));
                                                    ps0.setString(3, message.getInfo());
                                                    ps0.executeUpdate();//执行语句
                                                    System.out.println("数据库更新成功!");
                                                    con.close();
                                                } catch (SQLException eq) {
                                                    eq.printStackTrace();
                                                }
                                                try {
                                                    PreparedStatement ps = null;
                                                    PreparedStatement ps2 = null;
                                                    con = GetDBConnection.getConnection();//连接数据库
                                                    System.out.println("数据库连接成功");
                                                    String s2 = "insert into newmes(f,t,i) values(?,?,?)";
                                                    ps2 = con.prepareStatement(s2);
                                                    ps2.setString(1, message.getFrom().substring(0,11));
                                                    ps2.setString(2, message.getTo().substring(0,11));
                                                    ps2.setString(3, message.getInfo());
                                                    ps2.executeUpdate();//执行语句
                                                    System.out.println("离线消息更新成功!");
                                                    con.close();
                                                    break;
                                                } catch (SQLException eq) {
                                                    eq.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                    catch (Exception zz){
                                        if(message.getInfo().equals("753621459530154984512061561894984561321")){
                                            System.out.println("正在储存离线好友请求");
                                            Connection con;
                                            PreparedStatement ps0 = null;
                                            con = GetDBConnection.getConnection();//连接数据库
                                            System.out.println("数据库连接成功");
                                            try {
                                                String s0 = "insert into friend(f,t) values(?,?)";
                                                ps0 = con.prepareStatement(s0);
                                                ps0.setString(1, message.getFrom().substring(0,11));
                                                ps0.setString(2, message.getTo().substring(0,11));
                                                ps0.executeUpdate();//执行语句
                                                System.out.println("数据库更新成功!");
                                                con.close();
                                                break;
                                            } catch (SQLException eq) {
                                                eq.printStackTrace();
                                            }

                                        }
                                        if(!message.getInfo().equals("753621459530154984512061561894984561321") && !message.getInfo().equals("321456987456321123654789")) {
                                            System.out.println("这是离线消息，正在储存");
                                            Connection con;
                                            PreparedStatement ps = null;
                                            PreparedStatement ps2 = null;
                                            con = GetDBConnection.getConnection();//连接数据库
                                            System.out.println("数据库连接成功");
                                            try {
                                                String s2 = "insert into newmes(f,t,i) values(?,?,?)";
                                                ps2 = con.prepareStatement(s2);
                                                ps2.setString(1, message.getFrom().substring(0,11));
                                                ps2.setString(2, message.getTo().substring(0,11));
                                                ps2.setString(3, message.getInfo());
                                                ps2.executeUpdate();//执行语句
                                                String s = "insert into recod(f,t,i) values(?,?,?)";
                                                ps = con.prepareStatement(s);
                                                ps.setString(1, message.getFrom().substring(0,11));
                                                ps.setString(2, message.getTo().substring(0,11));
                                                ps.setString(3, message.getInfo());
                                                ps.executeUpdate();//执行语句
                                                System.out.println("消息记录更新成功!");
                                                System.out.println("离线消息更新成功!");
                                                con.close();
                                                break;
                                            } catch (SQLException eq) {
                                                eq.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        break;
                    //如果是登录
                    case MessageType.TYPE_LOGIN:
                        name = message.getFrom();//获取用户名
                        message.setInfo("欢迎您！");//设置登录成功信息
                        oOut.writeObject(message);
                        break;
                     //如果是线程关闭
                    case MessageType.TYPE_CLOSE:
                        for(int k=0;k<vector.size();k++){
                            ut = vector.get(k);
                            if(ut.name.equals(message.getFrom())){
                                vector.remove(k);
                                System.out.println(message.getFrom()+"线程关闭成功");
                            }
                        }
                }

            }



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}