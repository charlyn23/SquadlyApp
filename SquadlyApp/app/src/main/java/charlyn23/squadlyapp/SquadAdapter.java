package charlyn23.squadlyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class SquadAdapter extends ArrayAdapter<Contact> {
    public List<Contact> squad;

    public SquadAdapter(Context context, int resource, List<Contact> squad) {
        super(context, R.layout.squad_item, squad);
        this.squad = squad;
    }

    public void setContactList(List<Contact> contactList) {
        this.squad = squad;
        notifyDataSetChanged();
    }

    public List<Contact> getSquad() {
        return squad;
    }

    public void setSquad(List<Contact> squad) {
        this.squad = squad;
    }

    class ViewHolder {
        TextView contactName ;
    }

    @Override
    public int getCount() {
        return squad.size();
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.squad_item, parent, false);
            holder = new ViewHolder();
            holder.contactName = (TextView)view.findViewById(R.id.contactName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        if (squad != null && squad.size() > position) {
            Contact currentContact = squad.get(position);
            holder.contactName.setText(currentContact.getName());
        }
        return view;
    }
}

