package br.edu.ifsp.sbv.desafiodolook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.R;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.Album;

/**
 * Created by Adriel on 11/30/2017.
 */

public class PhotoAdapter extends ArrayAdapter<Album> {

    private Context context;
    private List<Album> listPhotos = null;

    static class ViewHolder{
        private NetworkImageView netImageViewPhoto;
        private TextView txtViewDate;
    }

    public PhotoAdapter(Context context, List<Album> listPhotos) {
        super(context, 0, listPhotos);
        this.listPhotos = listPhotos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoAdapter.ViewHolder viewHolder = new PhotoAdapter.ViewHolder();
        Album photo = listPhotos.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_photo, parent, false);

            viewHolder.netImageViewPhoto = (NetworkImageView) convertView.findViewById(R.id.netImgViewPhoto);
            viewHolder.txtViewDate = (TextView) convertView.findViewById(R.id.txtViewDate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (PhotoAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.netImageViewPhoto.setImageUrl(photo.getUrlPicture() , VolleySingleton.getInstance(context).getImageLoader());
        viewHolder.txtViewDate.setText(photo.getDateCreation().toString());

        return convertView;
    }
}
