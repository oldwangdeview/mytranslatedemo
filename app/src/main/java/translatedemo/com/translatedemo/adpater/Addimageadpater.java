package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.interfice.DeleteImage;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class Addimageadpater extends BaseAdapter {
    public static int MAX_IMAGE = 4;
    private List<String> filepath = new ArrayList<>();
    private Context mContent ;
    ImageView addimage;
    ImageView image;
    ImageView detle_image;
    private ListOnclickLister mlister;
    private DeleteImage DeleteImage;
    public Addimageadpater(Context mContent,List<String> filepath){
        this.mContent = mContent;
        this.filepath = filepath;

    }
    @Override
    public int getCount() {
        return filepath.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
          if(convertView==null){
              convertView = UIUtils.inflate(mContent, R.layout.addimage_item);
          }
          addimage = convertView.findViewById(R.id.addimage);
          image = convertView.findViewById(R.id.image);
          detle_image = convertView.findViewById(R.id.detle_image);
          if(!TextUtils.isEmpty(filepath.get(position))){
              if("addimage".equals(filepath.get(position))){
                  addimage.setVisibility(View.VISIBLE);
                  image.setVisibility(View.GONE);
                  detle_image.setVisibility(View.GONE);
              }else{
                  addimage.setVisibility(View.GONE);
                  image.setVisibility(View.VISIBLE);
                  detle_image.setVisibility(View.VISIBLE);
                  UIUtils.loadImageView(mContent,filepath.get(position),image);
              }

              detle_image.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      filepath.remove(position);
                      if(filepath.indexOf("addimage")<0&&position==3){
                          filepath.add("addimage");
                      }
                      if(DeleteImage!=null){
                          DeleteImage.deleteiamge(0);
                      }
                      notifyDataSetChanged();
                  }
              });

          }
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        return convertView;
    }

    public void setOnclickItemLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
    public void setdeteimagelister(DeleteImage deteimagelister){this.DeleteImage = deteimagelister;}
    public List<String> getimagepath(){
        List<String> path = new ArrayList<>();
        for(String image:filepath){

            if(!"addimage".equals(image)){
                new LogUntil(mContent,"imagepath",image);
                if(image.indexOf("file:/")>=0){
                    path.add(image.replace("file:/",""));
                }else {
                    path.add(image);
                }
            }
        }
        return  path;
    }
}
