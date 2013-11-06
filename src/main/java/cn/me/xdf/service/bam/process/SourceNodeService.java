package cn.me.xdf.service.bam.process;

import cn.me.xdf.service.SimpleService;
import com.sun.tools.internal.xjc.reader.gbind.SourceNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-11-6
 * Time: 下午2:46
 * 素材学习记录
 */
@Service
@Transactional(readOnly = true)
public class SourceNodeService extends SimpleService {

    @Transactional(readOnly = false)
    public Object saveSourceNode(SourceNode sourceNode){
        return null;
    }
}
