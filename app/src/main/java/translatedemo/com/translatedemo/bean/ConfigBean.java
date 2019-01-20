package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/12 0012.
 *         "id": 14,
 "field": "帮助中心",
 "content": "<p>反对&nbsp;<img src=\"http://img.baidu.com/hi/jx2/j_0014.gif\"/></p>",
 "createTime": "2019-01-10 21:49:32",
 "status": 0
 */

public class ConfigBean implements Serializable {
    public long id;
    public String field;
    public String content;
    public String createTime;
    public long status;

}
