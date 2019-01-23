package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oldwang on 2019/1/23 0023.
 */

public class CollectionBean implements Serializable {

    public int page;
    public int size;
    public int totalElements;
    public int totalPages;
    public List<CollectionListbean> list = new ArrayList<>();
}
