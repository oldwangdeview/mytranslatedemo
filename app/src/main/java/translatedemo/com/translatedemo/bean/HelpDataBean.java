package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oldwang on 2019/1/20 0020.
 *         "page": 1,
 "size": 20,
 "totalElements": 2,
 "totalPages": 1,
 */

public class HelpDataBean implements Serializable {
    public int page;
    public int size;
    public int totalElements;
    public int totalPages;
    public List<Helpcenter_ListBean> list = new ArrayList<>();

}
