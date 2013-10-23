package cn.me.xdf.model.organization;
/**
 * 此类作为页面视图引用，不会持久化到数据库
 */
public class User {

    private String id;
    private String name;
    private String realName;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String fdId, String name, String realName) {
        this.id = fdId;
        this.name = name;
        this.realName = realName;
    }

    public String getId() {
        return id;
    }

    public void setId(String fdId) {
        this.id = fdId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}