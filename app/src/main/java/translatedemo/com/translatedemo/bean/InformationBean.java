package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oldwang on 2019/1/11 0011.
 *  "page": 1,
 "size": 20,
 "totalElements": 3,
 "totalPages": 1,
 */

public class InformationBean implements Serializable {

    public int page;
    public int size;
    public int totalElements;
    public int totalPages;
    public List<ListBean_information> list = new ArrayList<>();

}
