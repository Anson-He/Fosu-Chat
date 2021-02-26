import javax.sql.rowset.serial.SerialException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

public class newfriend {
    Socket socket;
    Message message;
    ObjectOutputStream oOut;
    String id= null;
    String name0= null;
    String class1= null;
    String id_wo= null;
    String name_wo = null;
    String class1_wo = null;
    String info = "请求添加为好友";
    String f= null;
    String t= null;
    public void setInfo(String info){
        this.info = info;
    }
    public void setF(String f){
        this.f = f;
    }
    public void setT(String t){
        this.t = t;
    }
    private static JFrame jf_1 = null;
    private static JButton bt_1 = null;
    private static JButton bt_2 = null;
    private static JLabel jl_1 = null;
    private static JPanel jp_1 = null;

    public void init(){
        Connection con;
        Connection con2;
        ResultSet rs;
        ResultSet rs2;
        String s;
        Statement sql;
        Statement sql2;
        con = GetDBConnection.getConnection();
        System.out.println("数据库连接成功");
        s = "select * from login where id = "+f;
        String s2 = "select * from login where id = "+t;
        System.out.println(s);
        System.out.println(s2);
        try{
            sql = con.createStatement();
            rs = sql.executeQuery(s);
            while(rs.next()){
                id = rs.getString(1);
                name0 = rs.getString(2);
                class1 = rs.getString(3);
            }
            con.close();
        }
        catch (SQLException ss){
            ss.printStackTrace();
        }
        con2 = GetDBConnection.getConnection();
        try{
            sql2 = con2.createStatement();
            rs2 = sql2.executeQuery(s2);
            while(rs2.next()){
                id_wo = rs2.getString(1);
                name_wo = rs2.getString(2);
                class1_wo = rs2.getString(3);
            }
        }
        catch (SQLException ll){
            ll.printStackTrace();
        }
        System.out.println(name0+"加"+name_wo);
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        jf_1 = new JFrame("好友请求");
        jf_1.setSize(300, 200);
        jf_1.setResizable(false);

        jp_1 = new JPanel();
        jp_1.setLayout(null);

        bt_1 = new JButton("同意");
        bt_1.setBounds(25,100,80,30);
        bt_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Connection con;
                    PreparedStatement ps;
                    String s;
                    Statement sql;
                    con = GetDBConnection.getConnection();
                    System.out.println("数据库连接成功");
                    sql = con.createStatement();
                    System.out.println(name_wo);
                    System.out.println(id_wo);
                    s = "insert into "+name_wo+id_wo.substring(id_wo.length()-3,id_wo.length())+" values(?,?,?);";
                    ps = con.prepareStatement(s);
                    ps.setString(1, id);
                    ps.setString(2, name0);
                    ps.setString(3,class1);
                    ps.executeUpdate();
                    System.out.println("添加成功");
                    con.close();
                } catch (SQLException ss) {
                    JOptionPane.showMessageDialog(null, "添加失败！","恭喜",JOptionPane.ERROR_MESSAGE);
                    ss.printStackTrace();
                }
                try{
                    Connection con4;
                    PreparedStatement ps4;
                    String s4;
                    Statement sql4;
                    con4 = GetDBConnection.getConnection();
                    System.out.println("数据库连接成功");
                    sql4 = con4.createStatement();
                    System.out.println(name_wo);
                    System.out.println(id_wo);
                    s4 = "insert into "+name0+id.substring(id.length()-3,id.length())+" values(?,?,?);";
                    ps4 = con4.prepareStatement(s4);
                    ps4.setString(1, id_wo);
                    ps4.setString(2, name_wo);
                    ps4.setString(3,class1_wo);
                    ps4.executeUpdate();
                    System.out.println("添加成功");
                    con4.close();
                    JOptionPane.showMessageDialog(null, "好友添加成功！","恭喜",JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ss) {
                    JOptionPane.showMessageDialog(null, "添加失败！","恭喜",JOptionPane.ERROR_MESSAGE);
                    ss.printStackTrace();
                }
                try {
                    socket = new Socket("47.98.162.4", 12001);
                    System.out.println("服务器连接成功！");
                    //构建输出输入流
                    oOut = new ObjectOutputStream(socket.getOutputStream());
                    //1、客户端登录处理
                    //向服务器发送登录信息（名字和消息类型）
                    String name = id_wo;
                    message = new Message(name, id, MessageType.TYPE_SEND, "321456987456321123654789");
                    //发送给服务器
                    System.out.println("正在发送给服务器");
                    oOut.writeObject(message);
                    System.out.println("已发送");

                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
                Connection con3= null;
                PreparedStatement ps3 = null;
                ResultSet rs3 = null;
                Statement sql3 = null;
                con3 = GetDBConnection.getConnection();//连接数据库
                System.out.println("数据库连接成功");
                String qq = "delete from friend where f=? and t=?";
                try{
                    ps3 = con3.prepareStatement(qq);
                    ps3.setString(1,f);
                    ps3.setString(2,t);
                    ps3.executeUpdate();
                    System.out.println("删除成功");
                    con3.close();
                }
                catch (SQLException ea){
                    ea.printStackTrace();
                }
                try {
                    oOut.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jf_1.dispose();
            }
        });

        bt_2 = new JButton("拒绝");
        bt_2.setBounds(190,100,80,30);
        bt_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Connection con4= null;
                PreparedStatement ps4 = null;
                ResultSet rs4 = null;
                Statement sql4 = null;
                con4 = GetDBConnection.getConnection();//连接数据库
                System.out.println("数据库连接成功");
                String qq4 = "delete from friend where f=? and t=?";
                try{
                    ps4 = con4.prepareStatement(qq4);
                    ps4.setString(1,f);
                    ps4.setString(2,t);
                    ps4.executeUpdate();
                    System.out.println("删除成功");
                    con4.close();
                }
                catch (SQLException ea){
                    ea.printStackTrace();
                }
                jf_1.dispose();
                try {
                    oOut.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        jl_1 = new JLabel(name0+info);
        jl_1.setBounds(10,10,300,100);
        jl_1.setFont(font);

        jp_1.add(bt_1);
        jp_1.add(bt_2);
        jp_1.add(jl_1);
        jf_1.add(jp_1);

        jf_1.setVisible(true);
        jf_1.setLocation(600,300);
        //jf_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String args[]){
        new login().init();
    }
}
