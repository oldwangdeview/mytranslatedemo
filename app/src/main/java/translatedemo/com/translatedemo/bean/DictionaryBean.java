package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/16 0016.
 *  "id": 2,
 "name": "汉藏大辞典",
 "image": "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546072647947&di=8717180745475fbca1beb7003600e4b1&imgtype=0&src=http%3A%2F%2Fimg006.hc360.cn%2Fm2%2FM02%2F5C%2FED%2FwKhQclRGgEGEX8UwAAAAABKZplU410.jpg",
 "type": 2,
 "isMemberVisible": 1,
 "counts": 0
 */

public class DictionaryBean implements Serializable {

    public int id;
    public String name;
    public String image;
    public int type;
    public int isMemberVisible;
    public int counts;
    public boolean islode = false;
}
