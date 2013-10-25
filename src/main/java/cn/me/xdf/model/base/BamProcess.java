package cn.me.xdf.model.base;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-25
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public interface BamProcess {

    public static final String THROGHT_FIELD = "through";

    public static final String PRI_FIELD = "fdId";

    public static final String PRI_THROUGH = "through";

    public String getFdId();

    public boolean getThrough();

    public void setThrough(boolean through);
}
