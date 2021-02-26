import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class communication {
    String user_id = null;//信息接收者的用户信息
    String to_name = "XXX";//信息接收者的用户信息
    String f = null;//信息传递者的用户信息
    public void setid(String id){
        this.user_id = id;
    }
    public void setto(String to){this.to_name = to;}
    public void setf(String f1){
        this.f = f1;
    }
    private static JFrame jf_3;
    private static TextArea jta_1;
    private static TextArea jta_2;
    private static JPanel jp_2;
    private static JLabel jl_2;
    private static JButton bt_1;
    private static JButton bt_2;
    Message message;
    //单线程池
    ExecutorService es = Executors.newSingleThreadExecutor();
    //创建客户端
    Socket socket = null;
    ObjectOutputStream oOut = null;
    ObjectInputStream oIn = null;
    private boolean flag = true; //标记
    public void init(){
        try{
            socket = new Socket("47.98.162.4", 12001);
            System.out.println("服务器连接成功！");
            //构建输出输入流
            oOut =new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());
            //1、客户端登录处理
            //向服务器发送登录信息（名字和消息类型）
            String name = f+"send";
            //登录时，只自己的名字和消息类型为登录
            message = new Message(name, null, MessageType.TYPE_LOGIN, null);
            System.out.println(message.toString());
            //发送给服务器
            System.out.println("正在发送给服务器");
            oOut.writeObject(message);
            System.out.println("已发送");
            //服务器返回 欢迎信息
            message = (Message) oIn.readObject();
            //打印服务器返回的信息+当前客户端的名字
            System.out.println(message.getInfo() + message.getFrom());
            //2、启动读取消息的线程
            class readInfoThread implements Runnable {
                private ObjectInputStream oIn; //输入流 用来读操作

                public readInfoThread(ObjectInputStream oIn) {
                    this.oIn = oIn;
                }

                @Override
                public void run() {
                    try {
                        //循环 不断读取消息
                        while (flag) {
                            //读取信息
                            message = (Message) oIn.readObject();
                            //输出用户名+内容
                            jta_1.setText(jta_1.getText() + "\n\n" + to_name + ":" + message.getInfo());

                            System.out.println("[" + message.getFrom() + "]对我说：" + message.getInfo());

                        }
                        //没有数据就关闭
                        if(oIn != null){
                            oIn.close();
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            es.execute(new readInfoThread(oIn));  //读取线程完成

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        bt_1 = new JButton("发送");
        bt_1.setBounds(390,360,70,30);
        bt_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                message = new Message();
                message.setTo(user_id+"send");
                //谁发的，从自己这发
                message.setFrom(f+"send");
                //类型 发送消息
                message.setType(MessageType.TYPE_SEND);
                //发送的内容
                //System.out.println("Info：");
                message.setInfo(jta_2.getText());
                /*----到此需要发送的消息 对象 封装完毕----*/
                //发送给服务器
                try{
                    oOut.writeObject(message);
                    jta_1.setText(jta_1.getText()+"\n\n"+"你:"+jta_2.getText());
                    jta_2.setText(null);
                }
                catch (IOException s)
                {
                    s.printStackTrace();
                    JOptionPane.showMessageDialog(null, "发送失败！","错误",JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        bt_2 = new JButton("关闭");
        bt_2.setBounds(220,360,70,30);
        bt_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    message = new Message(f+"send",null,MessageType.TYPE_CLOSE,null);
                    oOut.writeObject(message);
                    jf_3.dispose();
                }
                catch (IOException f){
                    f.printStackTrace();
                }
            }
        });

        jf_3= new JFrame("聊天窗口");
        //jf_1.setLayout(null);
        jf_3.setSize(500,450);
        jf_3.setResizable(false);
        jf_3.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        jp_2 = new JPanel(null);

        jl_2 = new JLabel(to_name);
        jl_2.setBounds(220,5,50,25);
        jl_2.setFont(new Font("黑体",Font.PLAIN,15));

        jta_1 = new TextArea("",5,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
        jta_1.setEditable(false);
        jta_1.setFont(new Font("宋体",Font.PLAIN,15));
        jta_1.setBounds(15,40,450,150);

        jta_2 = new TextArea("",5,40,TextArea.SCROLLBARS_VERTICAL_ONLY);
        jta_2.setFont(new Font("宋体",Font.PLAIN,15));
        jta_2.setBounds(15,200,450,150);

        jp_2.add(jl_2);
        jp_2.add(jta_1);
        jp_2.add(jta_2);
        jp_2.add(bt_1);
        jp_2.add(bt_2);
        jf_3.add(jp_2);

        jf_3.setVisible(true);
        jf_3.setLocation(550,150);

        //更新聊天窗口
        String text = "";
        Connection con= null;
        ResultSet rs = null;
        Statement sql;
        con = GetDBConnection.getConnection();
        System.out.println("数据库连接成功");

        try{
            String ss = "select * from recod where f = "+user_id+" and t = "+f+" or f = "+f+" and t = "+user_id+";";
            System.out.println(ss);
            sql = con.createStatement();
            rs = sql.executeQuery(ss);
            while(rs.next()){
                String fr = rs.getString(1);
                //System.out.println("fr:"+fr);
                String to = rs.getString(2);
                String mes = rs.getString(3);
                //System.out.println("mes:"+mes);
                if(fr.equals(user_id)){
                    text = text+"\n\n"+to_name+":"+mes;
                }
                if(fr.equals(f)){

                    text = text+"\n\n"+"我:"+mes;
                }
            }
            con.close();
            //System.out.println("text:"+text);
            jta_1.setText(text);
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public static void main(String args[]){
        login c = new login();
        c.init();
    }
}
