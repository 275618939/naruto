package com.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.movie.R;
import com.movie.ui.MissSelfQueryActivity;

public class SelfFragment extends Fragment implements OnClickListener {

	
	//我发起的约会
	public static final int MY_MISS = 0X210;
	//我参与的约会
	public static final int MY_PART = 0X211;
	//我应邀的约会
	public static final int MY_INVITATION = 0X212;
	//用户参与的约会
	public static final int USER_INVITATION = 0X213;
	//电影下的约会
	public static final int MOVIE_INVITATION = 0X214;
	//约会类型
	public static final String MISS_KEY = "miss_type";
	//电影下的约会
	public static final String CONDITION_KEY = "miss_query_key";
	RelativeLayout myMissLayout;
	RelativeLayout myParticipateLayout;
	RelativeLayout myInvitationLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_self, null);
		initView(view);
		return view;
	}

	private void initView(View view) {

		myMissLayout = (RelativeLayout) view.findViewById(R.id.my_miss_layout);
		myParticipateLayout = (RelativeLayout) view.findViewById(R.id.my_participate_layout);
		myInvitationLayout = (RelativeLayout) view.findViewById(R.id.my_invitation_layout);
		myMissLayout.setOnClickListener(this);
		myParticipateLayout.setOnClickListener(this);
		myInvitationLayout.setOnClickListener(this);

	}
	private void goToMyMissQuery(int type){
		Intent cinemaPoi = new Intent(getActivity(),MissSelfQueryActivity.class);
		cinemaPoi.putExtra(MISS_KEY, type);
		startActivity(cinemaPoi);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.my_miss_layout:
				goToMyMissQuery(MY_MISS);
				break;
			case R.id.my_participate_layout:
				goToMyMissQuery(MY_PART);
				break;
			case R.id.my_invitation_layout:
				goToMyMissQuery(MY_INVITATION);
				break;
	
			default:
				break;
	
			}

	}

}
