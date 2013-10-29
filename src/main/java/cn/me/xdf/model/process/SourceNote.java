package cn.me.xdf.model.process;

import cn.me.xdf.model.base.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-28
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 * 素材记录
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "IXDF_NTP_SOURCE_NOTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SourceNote extends IdEntity{


}
