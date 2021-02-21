package index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class myfriend extends JFrame {
    String user_id;//登陆界面传递过来的用户信息
    String user_name;//登陆界面传递过来的用户信息
    String user_class1;//登陆界面传递过来的用户信息
    public void setid(String id){
        this.user_id = id;
    }
    public void setname(String name){
        this.user_name = name;
    }
    public void setclass1(String class1){
        this.user_class1 = class1;
    }
    private static JFrame jf_1 = null;//好友列表的框架
    private static JPanel jp_1;//个人信息的版面

    private static JButton bt1 = null;//刷新好友列表按钮
    private static JButton bt2 = null;//添加好友按钮
    private static JLabel jl_name = null;//姓名
    private static JLabel jl_id = null;//学号
    private static JLabel jl_class1 = null;//班级

    private static JList<Object> l = new JList<Object>();
    //滚动面板
    private JScrollPane jsp = new JScrollPane();


    /*private static JTextField jtext1;//学号
    private static JPasswordField jtext2;//密码
    private static JPasswordField jtext3;//确认密码
    private static JTextField jtext4;//密保1
    private static JTextField jtext5;//密保2
    private static JTextField jtext6;//姓名

    private static JLabel jl_password;//密码
    private static JLabel jl_password_again;//确认密码
    private static JLabel mb1;//密保问题1
    private static JLabel mb2;//密保问题2
    private static JLabel jl_title;*/


    public void init(){
        //输出账户信息
        System.out.println(user_id);
        System.out.println(user_name);
        System.out.println(user_class1);

        //数据库获取所有好友信息
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = GetDBConnection.getConnection();//连接数据库
        System.out.println("好友列表数据库连接成功");
        List<Student> list = new ArrayList<Student>();//好友列表
        try {
            String sql = "select * from "+user_name+user_id.substring(user_id.length()-3,user_id.length());
            System.out.println(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                Student stu = new Student();
                stu.setId(rs.getString(1));
                stu.setName(rs.getString(2));
                stu.setClass1(rs.getString(3));
                list.add(stu);
            }
            System.out.println("好友列表更新成功");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultListModel<Object> model = new DefaultListModel<Object>();
        int i = 0;
        while(true){
            try{
                model.add(i,new MyContent(list.get(i).getId(),list.get(i).getName(),list.get(i).getClass1()));
                i = i+1;
            }
            catch (Exception e){
                break;
            }
        }

        l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //设置列表单元只能单选

        l.setFixedCellHeight(60);   //设置列表单元高度

        jsp.setViewportView(l);   //l添加进去滚动面板
           //添加渲染
        l.setBorder(BorderFactory.createTitledBorder("我的好友"));
        l.setModel(model);
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {

                    System.out.println(l.getAnchorSelectionIndex());   //
                }
            }
        });

        class MyPanelRenderer extends JPanel implements ListCellRenderer{   //渲染
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {

                MyPanelRenderer mpr = new MyPanelRenderer();

                mpr.setLayout(new FlowLayout(FlowLayout.LEFT,1,0));  //设置流式布局的布局方式
                MyContent content = (MyContent)value;

                JLabel lb2 = new JLabel("<html><body>"+content.getId()+"<br>"+content.getName()+"<br>"+content.getContent()+"<body></html>");
                mpr.add(lb2);

                return mpr;
            }
        }
        l.setCellRenderer(new MyPanelRenderer());
        //---------------------------------------

        //版面配置
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        jf_1=new JFrame("Fosu-Chat");
        jf_1.setSize(400, 800);


        jf_1.add(jsp);


        jf_1.setVisible(true);
        jf_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf_1.setLocation(200,50);
    }
    public static void main(String args[]){
        login hl =new login();
        hl.init();
    }


}
