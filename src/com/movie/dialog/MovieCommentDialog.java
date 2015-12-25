package com.movie.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.RatingBar;

import com.movie.R;
import com.movie.app.BaseDialog;

public class MovieCommentDialog extends BaseDialog {

	EditText comments;
	RatingBar ratingBar;

	public MovieCommentDialog(Context context) {
		super(context);
		setDialogContentView(R.layout.include_dialog_movie_comments);
		comments=(EditText) findViewById(R.id.comment_input);
		ratingBar=(RatingBar) findViewById(R.id.movie_star);
	}

	@Override
	public void setTitle(CharSequence text) {
		super.setTitle(text);
	}

	public void setButton(CharSequence text,
			DialogInterface.OnClickListener listener) {
		super.setButton1(text, listener);
	}

	public void setButton(CharSequence text1,
			DialogInterface.OnClickListener listener1, CharSequence text2,
			DialogInterface.OnClickListener listener2) {
		super.setButton1(text1, listener1);
		super.setButton2(text2, listener2);
	}
	public String getComments(){
		return this.comments.getText().toString();
	}
	public int getRatingBarValue(){
		return (int)ratingBar.getRating()*10*2;
	}


}
