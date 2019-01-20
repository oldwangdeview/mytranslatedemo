package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/2 0002.
 *
 * msg
 *
 "id": 49146,
 "nickName": "1",
 "weixinOpenid": "",
 "qqOpenid": "",
 "orignalImage": "",
 "headSmallImage": "",
 "headBigImage": "",
 "phone": "",
 "sex": 0,
 "age": 0,
 "education": "",
 "balance": 0,
 "registrationTime": "2019-01-02 10:34:02",
 "updateTime": "2019-01-02 10:34:02",
 "memberBeginTime": "",
 "memberEndTime": "",
 "isMember": 0,
 "userStatus": 0,
 "memberMonth": "",
 "buyTime": "",
 "loginTime": "2019-01-02 10:34:02"
 */

public class LoginBean implements Serializable{

    public Integer id;
    public String nickName;
    public String weixinOpenid;
    public String qqOpenid;
    public String orignalImage;
    public String headSmallImage;
    public String headBigImage;
    public String phone;
    public String sex;
    public String age;
    public String education;
    public Integer balance;
    public String registrationTime;
    public String updateTime;
    public String memberBeginTime;
    public String memberEndTime;
    public int isMember;
    public String userStatus;
    public String buyTime;
    public String loginTime;
    public String memberMonth;

}
