package br.edu.ifsp.sbv.desafiodolook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.R;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.Album;
import br.edu.ifsp.sbv.desafiodolook.model.Duel;

/**
 * Created by Adriel on 11/29/2017.
 */

public class DuelAdapter extends BaseAdapter {

    private Context context;
    private List<Duel> listDuel;

    static class ViewHolder{
        private NetworkImageView netImageView1;
        private NetworkImageView netImageView2;
    }

    public DuelAdapter(Context context, List<Duel> listDuel) {
        this.context = context;
        this.listDuel = listDuel;
    }

    @Override
    public int getCount() {
        return listDuel.size();
    }

    @Override
    public Object getItem(int position) {
        return listDuel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Duel duel = listDuel.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_duel, parent, false);

            viewHolder.netImageView1 = (NetworkImageView) convertView.findViewById(R.id.netImgView1);
            viewHolder.netImageView2 = (NetworkImageView) convertView.findViewById(R.id.netImgView2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.netImageView1.setImageUrl(duel.getAlbumLeft().getUrlPicture() , VolleySingleton.getInstance(context).getImageLoader());
        viewHolder.netImageView2.setImageUrl(duel.getAlbumRight().getUrlPicture(), VolleySingleton.getInstance(context).getImageLoader());

        return convertView;
    }

}
