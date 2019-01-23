package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

/**
 * Created by oldwang on 2019/1/17 0017.
 */

public class TranslateBean implements Serializable {

    public int id;
    public int dictionaryId;
    public int type;
    public String dictionaryName;
    public String image;
    public String translateResult;
}
