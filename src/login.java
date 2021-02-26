import java.awt.Font;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

public class login extends JFrame{
    private static JButton bt1;//登陆按钮
    private static JButton bt2;//修改按钮
    private static JButton bt3;//注册按钮
    private static JLabel jl_1;//登录的版面
    private static JFrame jf_1;//登陆的框架
    private static JTextField jtext1;//用户名
    private static JPasswordField jtext2;//密码
    private static JLabel jl_admin;
    private static JLabel jl_password;
    private static JLabel jl_title;

    public void init (){//初始化登陆界面
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        Font title_font = new Font("黑体",Font.PLAIN,50);//设置大标题字体
        jf_1=new JFrame("Fosu-Chat");
        jf_1.setSize(650, 680);

        jf_1.setResizable(false);
        //给登陆界面添加背景图片
        ImageIcon bgim = new ImageIcon("src/b812c8fcc3cec3fd2d916676d088d43f87942702.jpg") ;//背景图案
        bgim.setImage(bgim.getImage().
                getScaledInstance(bgim.getIconWidth(),
                        bgim.getIconHeight(),
                        Image.SCALE_DEFAULT));
        jl_1=new JLabel();
        jl_1.setIcon(bgim);

        //大标题
        jl_title = new JLabel("Login");
        jl_title.setBounds(250,120,500,100);
        jl_title.setFont(title_font);

        jl_admin=new JLabel("学号");
        jl_admin.setBounds(160, 240, 100, 50);
        jl_admin.setFont(font);

        jl_password=new JLabel("密码");
        jl_password.setBounds(160, 320, 60, 50);
        jl_password.setFont(font);

        bt1=new JButton("登陆");
        bt1.setBounds(110, 400, 100, 50);
        bt1.setFont(font);

        bt2=new JButton("修改密码");
        bt2.setBounds(390, 400, 200, 50);
        bt2.setFont(font);

        bt3=new JButton("注册");
        bt3.setBounds(250, 400, 100, 50);
        bt3.setFont(font);

        //加入文本框
        jtext1=new JTextField("请输入学号");
        jtext1.setBounds(210, 240, 250, 50);
        jtext1.setFont(font);

        jtext2=new JPasswordField();//密码输入框
        jtext2.setBounds(210, 320, 250, 50);
        jtext2.setFont(font);

        jl_1.add(jtext1);
        jl_1.add(jtext2);

        jl_1.add(jl_admin);
        jl_1.add(jl_password);
        jl_1.add(bt1);
        jl_1.add(bt2);
        jl_1.add(bt3);
        jl_1.add(jl_title);

        jf_1.add(jl_1);
        jf_1.setVisible(true);
        jf_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_1.setLocation(400,50);
        //登陆点击事件
        ActionListener bt1_ls=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                //判断密码是否正确
                String pwd;
                String n=null;
                pwd = new String(jtext2.getPassword());
                System.out.println(pwd);
                Connection con;
                Statement sql;
                ResultSet rs;
                ResultSet rs2;
                con = GetDBConnection.getConnection();//连接数据库
                System.out.println("数据库连接成功");
                try{
                    sql = con.createStatement();
                    rs2 = sql.executeQuery("select * from login where id ="+jtext1.getText());
                    if(!rs2.next()){
                        JOptionPane.showMessageDialog(null, "没有账户信息！","错误",JOptionPane.ERROR_MESSAGE);
                    }
                    rs = sql.executeQuery("select pwd from login where id ="+jtext1.getText());
                    while(rs.next()){
                        n = rs.getString(1);
                        System.out.println(n);
                    }
                    if(n.equals(pwd)){
                        System.out.println("登录成功");
                        System.out.println("账户初始化中...");
                        me user = new me();
                        ResultSet a;
                        ResultSet b;
                        String a1;
                        String b1;
                        a = sql.executeQuery("select name from login where id="+jtext1.getText());
                        while(a.next()){
                            a1 = a.getString(1);
                            System.out.println(a1);
                            user.setname(a1);
                        }
                        b = sql.executeQuery("select class1 from login where id="+jtext1.getText());
                        while(b.next()){
                            b1 = b.getString(1);
                            System.out.println(b1);
                            user.setclass1(b1);
                        }
                        user.setid(jtext1.getText());
                        System.out.println("账户初始化成功！");
                        user.init();
                        jf_1.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "密码错误！","错误",JOptionPane.ERROR_MESSAGE);
                    }
                    con.close();
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, "登陆错误！","错误",JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        bt1.addActionListener(bt1_ls);
        //注册点击事件(跳转到注册页面)
        ActionListener bt3_ls=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Registered ml = new Registered();
                ml.init();
                //Registered ml=new Registered();//为跳转的界面
                jf_1.dispose();//销毁当前界面
            }
        };
        bt3.addActionListener(bt3_ls);
        //修改密码跳转
        ActionListener bt2_ls=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Password pl = new Password();
                pl.init();
                jf_1.dispose();
            }
        };
        bt2.addActionListener(bt2_ls);
    }
    public static void main(String[] args) {
        //初始化登陆界面

        login hl =new login();
        hl.init();
    }
}
