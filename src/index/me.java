package index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class me {
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

    public void init(){
        //输出个人信息
        System.out.println(user_id);
        System.out.println(user_name);
        System.out.println(user_class1);

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
                myfriend mf = new myfriend();
                mf.setid(user_id);
                mf.setname(user_name);
                mf.init();
            }
        };
        bt1.addActionListener(bs_1);
        ActionListener bs_3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login l = new login();
                l.init();
                jf_1.dispose();
            }
        };
        bt3.addActionListener(bs_3);
    }
    public static void main(String args[]){
        login ll = new login();
        ll.init();
    }
}
