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
import br.edu.ifsp.sbv.desafiodolook.model.Friend;
import br.edu.ifsp.sbv.desafiodolook.model.Ranking;

/**
 * Created by Guilherme on 03/12/2017.
 */

public class RankingAdapter extends ArrayAdapter<Ranking> {

    private Context context;
    private List<Ranking> listRankings = null;

    static class ViewHolder{
        private TextView txtViewRankingPosition;
        private TextView txtViewRankingName;
        private TextView txtViewRankingEmail;
        private TextView txtViewRankingData;
    }

    public RankingAdapter(Context context, List<Ranking> listRankings) {
        super(context, 0, listRankings);
        this.listRankings = listRankings;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankingAdapter.ViewHolder viewHolder = new RankingAdapter.ViewHolder();
        Ranking ranking = listRankings.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_ranking, parent, false);

            viewHolder.txtViewRankingPosition = (TextView) convertView.findViewById(R.id.textViewRankingPosition);
            viewHolder.txtViewRankingName = (TextView) convertView.findViewById(R.id.textViewRankingName);
            viewHolder.txtViewRankingEmail = (TextView) convertView.findViewById(R.id.textViewRankingEmail);
            viewHolder.txtViewRankingData = (TextView) convertView.findViewById(R.id.textViewRankingData);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (RankingAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txtViewRankingPosition.setText(ranking.getUserFollow().getUserName());
        viewHolder.txtViewRankingName.setText(ranking.getUserFollow().getEmail());
        viewHolder.txtViewRankingEmail.setText(ranking.getUserFollow().getUserName());
        viewHolder.txtViewRankingData.setText(ranking.getUserFollow().getEmail());

        return convertView;
    }
}