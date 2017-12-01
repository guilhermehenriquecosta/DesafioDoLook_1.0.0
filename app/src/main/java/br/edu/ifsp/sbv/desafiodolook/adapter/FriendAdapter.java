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

/**
 * Created by Adriel on 12/1/2017.
 */

public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private List<Friend> listFriends = null;

    static class ViewHolder{
        private NetworkImageView netImageViewFriend;
        private TextView txtViewFriendName;
        private TextView txtViewFriendEmail;
    }

    public FriendAdapter(Context context, List<Friend> listFriends) {
        super(context, 0, listFriends);
        this.listFriends = listFriends;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendAdapter.ViewHolder viewHolder = new FriendAdapter.ViewHolder();
        Friend friend = listFriends.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_friend, parent, false);

            viewHolder.netImageViewFriend = (NetworkImageView) convertView.findViewById(R.id.netImgViewFriend);
            viewHolder.txtViewFriendName = (TextView) convertView.findViewById(R.id.textViewFriendName);
            viewHolder.txtViewFriendEmail = (TextView) convertView.findViewById(R.id.textViewFriendEmail);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (FriendAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.netImageViewFriend.setImageUrl(friend.getUserFollow().getUrlAvatar() , VolleySingleton.getInstance(context).getImageLoader());
        viewHolder.txtViewFriendName.setText(friend.getUserFollow().getUserName());
        viewHolder.txtViewFriendEmail.setText(friend.getUserFollow().getEmail());

        return convertView;
    }
}
