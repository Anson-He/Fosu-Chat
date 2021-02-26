public class MyContent  {   //列表信息实体类
    private String name;
    private String class1;
    private String id;
    public MyContent(String id,String name,String class1) {
        this.name = name;
        this.class1 = class1;
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return class1;
    }
    public void setContent(String content) {
        this.class1 = content;
    }
}
