package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/14 0014.
 *  "id": 13,
 "field": "份地方",
 "content": "<p><img src=\"http://img.baidu.com/hi/jx2/j_0001.gif\"/></p>",
 "createTime": "2019-01-10 21:46:42",
 "status": 0
 */

public class NoticeListBean implements Serializable {

    public int id;
    public String field;
    public String content;
    public String createTime;
    public int status;
}
