package br.edu.ifsp.sbv.desafiodolook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.CircularNetworkImageView;

import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.R;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Guilherme on 03/12/2017.
 */

public class RankingAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> listRanking = null;

    static class ViewHolder{
        private CircularNetworkImageView netImageViewRanking;
        private TextView txtViewRankingPosition;
        private TextView txtViewRankingName;
        private TextView txtViewRankingEmail;
        private TextView txtViewRankingData;
    }

    public RankingAdapter(Context context, List<User> listRanking) {
        super(context, 0, listRanking);
        this.listRanking = listRanking;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankingAdapter.ViewHolder viewHolder = new RankingAdapter.ViewHolder();
        User ranking = listRanking.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_ranking, parent, false);

            viewHolder.netImageViewRanking = (CircularNetworkImageView) convertView.findViewById(R.id.netImgViewRanking);
            viewHolder.txtViewRankingPosition = (TextView) convertView.findViewById(R.id.textViewRankingPosition);
            viewHolder.txtViewRankingName = (TextView) convertView.findViewById(R.id.textViewRankingName);
            viewHolder.txtViewRankingEmail = (TextView) convertView.findViewById(R.id.textViewRankingEmail);
            viewHolder.txtViewRankingData = (TextView) convertView.findViewById(R.id.textViewRankingData);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (RankingAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.netImageViewRanking.setImageUrl(ranking.getUrlAvatar() , VolleySingleton.getInstance(context).getImageLoader());
        viewHolder.txtViewRankingPosition.setText(String.valueOf(position + 1));
        viewHolder.txtViewRankingName.setText(ranking.getUserName());
        viewHolder.txtViewRankingEmail.setText(ranking.getEmail());
        viewHolder.txtViewRankingData.setText(String.valueOf(ranking.getWin() + " V, ") +
                                                String.valueOf(ranking.getTie() + " E, ") +
                                                String.valueOf(ranking.getLoss() + " D, ") +
                                                String.valueOf(ranking.getPer() + "%"));

        return convertView;
    }
}