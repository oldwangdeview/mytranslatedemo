package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oldwang on 2019/1/16 0016.
 *   "id": 47,
 "userId": 49141,
 "content": "测试反馈",
 "createTime": 1547620558876,
 "userHead": "http://10.0.75.1:8080/img/IMG_99e37e74fb0c43ee9f4353754c98c8ec.jpg",
 "userPhone": "135478754892",
 "userNickName": "kkkkkkkkk",
 "status": 0,
 "images": [
 "http://39.104.188.55:8001/tranlate/img/IMG_d36a740ccc7144ff85e971081b9bf22b.png",
 "http://39.104.188.55:8001/tranlate/img/IMG_825547f6d9314e7ab472815d8eaaa225.png"
 ]
 */

public class FeedBackBean implements Serializable {
    public int id;
    public int userId;
    public String content;
    public long createTime;
    public String userHead;
    public String userPhone;
    public String userNickName;
    public int status;
    public List<String> images = new ArrayList<>();
}
