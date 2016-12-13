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

public class ContentAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contactList;

    public ContentAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, R.layout.list_row, contacts);
        this.contactList = contacts;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView contactName;
        TextView contactPhoneNumber;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder();
            holder.contactName = (TextView)view.findViewById(R.id.contactName);
            holder.contactPhoneNumber = (TextView)view.findViewById(R.id.contactPhoneNumber);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        if (contactList != null && contactList.size() > position) {
            Contact currentContact = contactList.get(position);
            holder.contactName.setText(currentContact.getName());
            holder.contactPhoneNumber.setText(currentContact.getPhoneNumber());
        }
        return view;
    }
}

