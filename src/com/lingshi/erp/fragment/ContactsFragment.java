package com.lingshi.erp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.lingshi.erp.R;

@SuppressLint("InflateParams")
public class ContactsFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contacts_fragment, null);
		ExpandableListView contacts = (ExpandableListView) view
				.findViewById(R.id.contacts_expand_list_view);
		return view;
	}
}
