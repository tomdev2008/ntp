package cn.me.xdf.model.organization;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-12-12
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_ORG_GROUP")
@PrimaryKeyJoinColumn(name = "FDID")
public class SysOrgGroup extends SysOrgElement {

    public SysOrgGroup() {
        super();
        setFdOrgType(new Integer(ORG_TYPE_GROUP));
    }
    /*
     * 群组成员
	 */
    private List<SysOrgElement> fdMembers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SYS_ORG_GROUP_ELEMENT", joinColumns = {
            @JoinColumn(name = "FD_GROUPID", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "FD_ELEMENTID",
                    nullable = false) })
    public List<SysOrgElement> getFdMembers() {
        return fdMembers;
    }

    public void setFdMembers(List<SysOrgElement> fdMembers) {
        this.fdMembers = fdMembers;
    }
}
