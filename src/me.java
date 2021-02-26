import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class me {
    addfriend af;
    myfriend mf;
    String user_id;//登陆界面传递过来的用户信息
    String user_name;//登陆界面传递过来的用户信息
    String user_class1;//登陆界面传递过来的用户信息
    public void setid(String id){
        this.user_id = id;
    }
    public void setname(String name)
    {
        this.user_name = name;
    }
    public void setclass1(String class1){
        this.user_class1 = class1;
    }

    private static JFrame jf_1;//个人信息框架
    private static JLabel id;//学号
    private static JLabel name;//姓名
    private static JLabel class1;//班级
    private static JPanel jp_1;//版面
    private static JButton bt1;//我的好友按钮
    private static JButton bt2;//添加好友按钮
    private static JButton bt3;//注销按钮
    private static JLabel jl_1;//离线消息提醒
    private static JLabel jl_2;//消息提醒

    //单线程池
    ExecutorService es = Executors.newSingleThreadExecutor();
    //创建客户端
    Socket socket = null;
    ObjectOutputStream oOut = null;
    ObjectInputStream oIn = null;
    private boolean flag = true; //标记
    Message message;

    public void mes(){
        System.out.println(message.getInfo());
        if(message.getInfo().equals("321456987456321123654789")){
            JOptionPane.showMessageDialog(null, "好友添加成功,请刷新好友列表！","恭喜",JOptionPane.ERROR_MESSAGE);
        }
        if(message.getInfo().equals("753621459530154984512061561894984561321"))
        {
            Font font_mes = new Font("黑体",Font.PLAIN,15);
            jl_1 = new JLabel("您有新的好友请求");
            jl_1.setBounds(130, 135, 200, 50);
            jl_1.setForeground(Color.red);
            jl_1.setFont(font_mes);
            jl_1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 1) {
                        jp_1.remove(jl_1);
                        jf_1.repaint();
                        newfriend nf = new newfriend();
                        nf.setF(message.getFrom().substring(0,11));
                        nf.setT(message.getTo().substring(0,11));
                        nf.init();

                    }
                }
            });
            jp_1.add(jl_1);
            jf_1.repaint();
        }
        if(!message.getInfo().equals("753621459530154984512061561894984561321") && !message.getInfo().equals("321456987456321123654789")) {
            Font font_mes = new Font("黑体", Font.PLAIN, 15);
            jl_1 = new JLabel("您有新的信息");
            jl_1.setBounds(130, 135, 200, 50);
            jl_1.setForeground(Color.red);
            jl_1.setFont(font_mes);
            jl_1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        jp_1.remove(jl_1);
                        jf_1.repaint();
                        mf.reload();
                        mf.init();
                    }
                }
            });
            jp_1.add(jl_1);
            jf_1.repaint();
        }

    }

    public void init(){

        mf = new myfriend();
        mf.setid(this.user_id);
        mf.setname(this.user_name);
        mf.init();
        //输出个人信息
        System.out.println(user_id);
        System.out.println(user_name);
        System.out.println(user_class1);
        try {
            socket = new Socket("47.98.162.4", 12001);
            System.out.println("服务器连接成功！");
            //构建输出输入流
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());
            //1、客户端登录处理
            //向服务器发送登录信息（名字和消息类型）
            String name = user_id;
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
                            System.out.println("[" + message.getFrom() + "]对我说：" + message.getInfo());
                            mes();
                        }
                        //没有数据就关闭
                        if (oIn != null) {
                            oIn.close();
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            es.execute(new readInfoThread(oIn));  //读取线程完成
        }
        catch (IOException | ClassNotFoundException g){
            g.printStackTrace();
        }


        Font font =new Font("黑体", Font.PLAIN, 25);//设置字体
        jf_1 = new JFrame("个人信息");
        //jf_1.setLayout(null);
        jf_1.setSize(400,300);
        jf_1.setResizable(false);

        jp_1 = new JPanel();
        jp_1.setLayout(null);

        id = new JLabel(user_id);
        id.setBounds(10,10,200,50);
        id.setFont(font);

        name = new JLabel(user_name);
        name.setBounds(10,50,200,50);
        name.setFont(font);

        class1 = new JLabel(user_class1);
        class1.setBounds(10,100,500,50);
        class1.setFont(font);

        bt1 = new JButton("我的好友");
        bt1.setBounds(10,180,180,30);
        bt1.setFont(font);

        bt2 = new JButton("添加好友");
        bt2.setBounds(200,180,180,30);
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("???"+user_id);
                af = new addfriend();
                af.setid_add(user_id);
                af.setname_add(user_name);
                af.setclass1_add(user_class1);
                af.init();
            }
        });
        bt2.setFont(font);

        bt3 = new JButton("注销");
        bt3.setBounds(100,220,180,30);
        bt3.setFont(font);

        jp_1.add(bt1);
        jp_1.add(bt2);
        jp_1.add(bt3);
        jp_1.add(name);
        jp_1.add(class1);
        jp_1.add(id);
        jf_1.setContentPane(jp_1);
        jf_1.setVisible(true);
        jf_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_1.setLocation(550,150);

        //点击按钮的事件
        ActionListener bs_1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mf.reload();
                myfriend mf = new myfriend();
                mf.setid(user_id);
                mf.setname(user_name);
                mf.setclass1(user_class1);
                mf.init();
            }
        };
        bt1.addActionListener(bs_1);
        ActionListener bs_3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                message = new Message(user_id,null,MessageType.TYPE_CLOSE,null);
                try {
                    oOut.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mf.reload();
                login l = new login();
                l.init();
                jf_1.dispose();
            }
        };
        bt3.addActionListener(bs_3);
        Connection con;
        Statement sql;
        ResultSet rs;
        con = GetDBConnection.getConnection();
        try{
            sql = con.createStatement();
            rs = sql.executeQuery("select * from friend where t = "+user_id);
            while(rs.next()){
                String f = rs.getString(1);
                String t = rs.getString(2);
                newfriend nf = new newfriend();
                nf.setF(f);
                nf.setT(user_id);
                nf.init();
            }
            con.close();
        }
        catch (SQLException zz){
            zz.printStackTrace();
        }

    }

    public static void main(String args[]){
        login ll = new login();
        ll.init();
    }
}
