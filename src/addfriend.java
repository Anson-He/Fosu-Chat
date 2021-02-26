import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class addfriend {
    String user_id;
    String user_name;
    String user_class1;
    public void setid_add(String id){
        this.user_id = id;
    }
    public void setname_add(String name)
    {
        this.user_name = name;
    }
    public void setclass1_add(String class1){
        this.user_class1 = class1;
    }
    private static JFrame jf_1;//主框架
    private static JPanel jp_1;//主版面
    private static JLabel jl_1;//按学号索引
    private static JTextField jtf_1;//输入学号
    private static JButton bt1;//确定
    private static JButton bt2;//取消
    //单线程池
    ExecutorService es = Executors.newSingleThreadExecutor();
    //创建客户端
    Socket socket = null;
    ObjectOutputStream oOut = null;
    ObjectInputStream oIn = null;
    private boolean flag = true; //标记
    Message message;
    String es_name;

    public void init(){
        try {
            es_name =user_id+"add";
            socket = new Socket("47.98.162.4", 12001);
            System.out.println("服务器连接成功！");
            //构建输出输入流
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());
            //1、客户端登录处理
            //向服务器发送登录信息（名字和消息类型）
            String name = es_name;
            //登录时，只自己的名字和消息类型为登录
            message = new Message(name, null, MessageType.TYPE_LOGIN, null);
            //发送给服务器
            System.out.println("正在发送给服务器");
            oOut.writeObject(message);
            System.out.println("已发送");
            //服务器返回 欢迎信息
            message = (Message) oIn.readObject();
            //打印服务器返回的信息+当前客户端的名字
            System.out.println(message.getInfo() + message.getFrom());
        }
        catch (IOException | ClassNotFoundException g){
            g.printStackTrace();
        }

        jf_1 = new JFrame("添加好友");
        jf_1.setSize(400, 200);
        jf_1.setResizable(false);

        jp_1 = new JPanel(null);

        jl_1 = new JLabel("请输入学号:");
        jl_1.setFont(new Font("黑体",Font.PLAIN,20));
        jl_1.setBounds(50,20,200,20);

        jtf_1 = new JTextField();
        jtf_1.setBounds(100,50,200,50);

        bt1 = new JButton("确定");
        bt1.setBounds(270,120,60,30);
        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                message = new Message(es_name,jtf_1.getText(),MessageType.TYPE_SEND,"753621459530154984512061561894984561321");
                try {
                    oOut.writeObject(message);
                    JOptionPane.showMessageDialog(null, "成功发送请求！","恭喜",JOptionPane.ERROR_MESSAGE);

                }
                catch (IOException i){
                    i.printStackTrace();
                    JOptionPane.showMessageDialog(null, "添加失败！","错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bt2 = new JButton("取消");
        bt2.setBounds(80,120,60,30);
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    message = new Message(es_name,null,MessageType.TYPE_CLOSE,null);
                    oOut.writeObject(message);
                    oOut.close();
                    flag = false;
                    es.shutdownNow();
                    socket.close();
                    jf_1.dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        jp_1.add(jl_1);
        jp_1.add(jtf_1);
        jp_1.add(bt2);
        jp_1.add(bt1);
        jf_1.add(jp_1);

        jf_1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jf_1.setVisible(true);
        jf_1.setLocation(600,200);
    }
    public static void main(String args[]){
        new login().init();
    }
}
