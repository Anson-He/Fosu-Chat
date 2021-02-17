package index;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registered extends JFrame{
    private static JButton bt1;//确定按钮
    private static JButton bt2;//返回按钮
    private static JLabel jl_1;//注册的版面
    private static JFrame jf_1;//注册的框架
    private static JTextField jtext1;//学号
    private static JPasswordField jtext2;//密码
    private static JPasswordField jtext3;//确认密码
    private static JTextField jtext4;//密保1
    private static JTextField jtext5;//密保2
    private static JTextField jtext6;//姓名
    private static JLabel name;//姓名
    private static JLabel jl_admin;//学号
    private static JLabel jl_password;//密码
    private static JLabel jl_password_again;//确认密码
    private static JLabel mb1;//密保问题1
    private static JLabel mb2;//密保问题2
    private static JLabel jl_title;
    public void init (){//初始化登陆界面
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        Font title_font = new Font("黑体",Font.PLAIN,50);//设置大标题字体
        jf_1=new JFrame("Fosu-Contact");
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
        jl_title = new JLabel("Registered");
        jl_title.setBounds(190,5,500,100);
        jl_title.setFont(title_font);

        jl_admin=new JLabel("学号");
        jl_admin.setBounds(160, 100, 100, 50);
        jl_admin.setFont(font);

        name = new JLabel("姓名");
        name.setBounds(160,180,60,50);
        name.setFont(font);

        jl_password=new JLabel("密码");
        jl_password.setBounds(160, 260, 60, 50);
        jl_password.setFont(font);

        jl_password_again = new JLabel("确认密码");
        jl_password_again.setBounds(120,340,100,50);
        jl_password_again.setFont(font);

        mb1 = new JLabel("密保问题1：请问你最喜欢的运动是什么？");
        mb1.setBounds(150,380,500,50);
        mb1.setFont(font);

        mb2 = new JLabel("密保问题2：请问你最喜欢的颜色是什么？");
        mb2.setBounds(150,460,500,50);
        mb2.setFont(font);

        bt1=new JButton("确定");
        bt1.setBounds(200, 570, 100, 50);
        bt1.setFont(font);

        bt2=new JButton("返回");
        bt2.setBounds(350, 570, 100, 50);
        bt2.setFont(font);

        //加入文本框
        jtext1=new JTextField("请输入学号");
        jtext1.setBounds(210, 100, 250, 50);
        jtext1.setFont(font);

        jtext6=new JTextField("请输入与学号匹配的姓名");
        jtext6.setBounds(210,180,250,50);
        jtext6.setFont(font);

        jtext2=new JPasswordField("123456");//密码输入框
        jtext2.setBounds(210, 260, 250, 50);
        jtext2.setFont(font);

        jtext3=new JPasswordField("123456");//确认密码框
        jtext3.setBounds(210,340,250,50);
        jtext3.setFont(font);

        jtext4=new JTextField();//密保问题答案1
        jtext4.setBounds(210,420,250,50);
        jtext4.setFont(font);

        jtext5=new JTextField();//密保问题答案2
        jtext5.setBounds(210,500,250,50);
        jtext5.setFont(font);

        jl_1.add(jtext1);
        jl_1.add(jtext2);
        jl_1.add(jtext3);
        jl_1.add(jtext4);
        jl_1.add(jtext5);
        jl_1.add(jtext6);

        jl_1.add(jl_admin);
        jl_1.add(jl_password);
        jl_1.add(bt1);
        jl_1.add(bt2);
        jl_1.add(jl_title);
        jl_1.add(name);
        jl_1.add(mb1);
        jl_1.add(mb2);
        jl_1.add(jl_password_again);

        jf_1.add(jl_1);
        jf_1.setVisible(true);
        jf_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_1.setLocation(300,400);
        ActionListener bt1_s = new ActionListener() {//确定按钮的事件
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //判断两次密码是否相同
                String pwd1;
                pwd1 = new String(jtext2.getPassword());
                String pwd2;
                pwd2 = new String(jtext3.getPassword());
                System.out.println(pwd1);
                System.out.println(pwd2);
                while(true) {
                    if (pwd1.equals("")) {
                        JOptionPane.showMessageDialog(null, "密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if (jtext1.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "学号不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if (jtext4.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "密保1不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if (jtext5.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "密保2不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    if (!pwd2.equals(pwd1)) {
                        JOptionPane.showMessageDialog(null, "两次密码不一样！", "密码错误", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    else{
                        Connection con;
                        Statement sql;
                        ResultSet rs;
                        ResultSet rs0;
                        PreparedStatement ps = null;
                        ResultSet rs3;
                        con = GetDBConnection.getConnection();//连接数据库
                        System.out.println("数据库连接成功");
                        try{
                            //判断学号与姓名是否匹配
                            sql = con.createStatement();
                            rs0 = sql.executeQuery("select * from login where id ="+jtext1.getText());
                            //判断学号是否已经注册
                            try{
                                if(rs0.next()){
                                    JOptionPane.showMessageDialog(null, "该学号已被注册！","错误",JOptionPane.ERROR_MESSAGE);
                                break;
                                }
                            }
                            catch (Exception e){}
                            rs = sql.executeQuery("select name from registered where id ="+jtext1.getText());
                            //System.out.println(jtext6.getText());
                            String n = null;
                            String n3 = null;
                            while(rs.next()){
                                n = rs.getString(1);
                                System.out.println(n);
                            }
                            if(!n.equals(jtext6.getText())){
                                JOptionPane.showMessageDialog(null, "学号与姓名不匹配！","错误",JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                            else{
                                //注册成功要把登录信息写入数据库
                                rs3 = sql.executeQuery("select class1 from registered where id ="+jtext1.getText());
                                while (rs3.next()){
                                    n3 = rs3.getString(1);
                                    System.out.println(n3);
                                }
                                String s = "insert into login(id,name,class1,pwd,mb1,mb2) values(?,?,?,?,?,?)";
                                try{
                                    ps = con.prepareStatement(s);
                                    ps.setString(1,jtext1.getText());
                                    ps.setString(2,jtext6.getText());
                                    ps.setString(3,n3);
                                    ps.setString(4,pwd1);
                                    ps.setString(5,jtext4.getText());
                                    ps.setString(6,jtext5.getText());

                                    ps.executeUpdate();//执行语句
                                    System.out.println("数据库更新成功!");
                                }
                                catch (SQLException e){
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "注册失败！","错误",JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                                System.out.println("注册成功");
                                rs3.close();
                                con.close();
                                //注册成功后跳转
                                login ll=new login();//为跳转的界面
                                ll.init();
                                jf_1.dispose();//销毁当前界面
                                JOptionPane.showMessageDialog(null, "注册成功！","恭喜",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        catch (Exception e){
                            JOptionPane.showMessageDialog(null, "没有该学号信息，请联系管理员！","错误",JOptionPane.ERROR_MESSAGE);
                            //e.printStackTrace();
                        }
                        break;
                    }
                }

            }
        };
        bt1.addActionListener(bt1_s);
        ActionListener bt2_s = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login ll=new login();//为跳转的界面
                ll.init();
                jf_1.dispose();//销毁当前界面
            }
        };
        bt2.addActionListener(bt2_s);
    }

    public static void main(String[] args) {
        Registered ml=new Registered();
        ml.init();

    }
}