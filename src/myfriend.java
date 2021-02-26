import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class myfriend extends JFrame {
    myfriend m = null;
    communication com = null;
    List<String> f = new ArrayList<String>();//发信息列表
    String user_id = null;//登陆界面传递过来的用户信息
    String user_name = null;//登陆界面传递过来的用户信息
    String user_class1 = null;//登陆界面传递过来的用户信息
    public void setid(String id){
        this.user_id = id;
    }
    public void setname(String name){
        this.user_name = name;
    }
    public void setclass1(String class1){
        this.user_class1 = class1;
    }

    private static JFrame jf_2 = null;//好友列表的框架


    private static JList<Object> l = new JList<Object>();
    //滚动面板
    private JScrollPane jsp = new JScrollPane();

    public void init(){
        l = new JList<Object>();
        jsp = new JScrollPane();
        //输出账户信息
        System.out.println(user_id);
        System.out.println(user_name);
        System.out.println(user_class1);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        con = GetDBConnection.getConnection();//连接数据库
        System.out.println("数据库连接成功");
        //查看是否有新信息
        try{
            String s = "select f from newmes where t = "+user_id;
            ps = con.prepareStatement(s);
            rs = ps.executeQuery();
            while(rs.next()){
                String a = rs.getString(1);
                f.add(a);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        //数据库获取所有好友信息
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

        jsp.setViewportView(l);   //添加进去滚动面板
           //添加渲染
        l.setBorder(BorderFactory.createTitledBorder("我的好友"));
        l.setModel(model);
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    System.out.println(l.getSelectedIndex());
                    System.out.println(list.get(l.getSelectedIndex()).getId());
                    String x = list.get(l.getSelectedIndex()).getId();
                    String y = list.get(l.getSelectedIndex()).getName();
                    Connection con= null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    Statement sql = null;
                    con = GetDBConnection.getConnection();//连接数据库
                    System.out.println("数据库连接成功");
                    System.out.println(user_id);
                    String qq = "delete from newmes where f=? and t=?";
                    try{
                        ps = con.prepareStatement(qq);
                        ps.setString(1,x);
                        ps.setString(2,user_id);
                        ps.executeUpdate();
                        System.out.println("删除成功");
                        con.close();
                    }
                    catch (SQLException ea){
                        ea.printStackTrace();
                    }
                    jf_2.dispose();
                    m = new myfriend();
                    m.setid(user_id);
                    m.setname(user_name);
                    m.setclass1(user_class1);
                    m.init();
                    com = new communication();
                    com.setid(x);
                    com.setto(y);
                    com.setf(user_id);
                    com.init();
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
                if(f.contains(content.getId())){
                    lb2.setForeground(Color.red);
                }
                if(isSelected){
                    lb2.setForeground(Color.yellow);
                }
                if(!f.contains(content.getId())&&!isSelected){
                    lb2.setForeground(Color.black);
                }
                mpr.add(lb2);

                return mpr;
            }
        }
        l.setCellRenderer(new MyPanelRenderer());
        //---------------------------------------

        //版面配置
        Font font =new Font("黑体", Font.PLAIN, 20);//设置字体
        jf_2=new JFrame("Fosu-Chat");
        jf_2.setSize(400, 800);
        //jf_1.setDefaultCloseOperation(EXIT_ON_CLOSE);


        jf_2.add(jsp);


        jf_2.setVisible(true);
        jf_2.setLocation(200,50);
    }
    public void reload(){
        jf_2.dispose();
    }
    public static void main(String args[]){
        login hl =new login();
        hl.init();
    }

}
