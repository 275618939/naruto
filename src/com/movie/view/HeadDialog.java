package com.movie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.movie.R;

public class HeadDialog extends Dialog {

	public HeadDialog(Context context) {
		super(context);
	}

	public HeadDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private TextView photograph;
		private TextView pictureLib;
		private TextView random;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private DialogInterface.OnClickListener photographClickListener;
		private DialogInterface.OnClickListener pictureLibClickListener;
		private DialogInterface.OnClickListener randomClickListener;

		public Builder(Context context) {
			this.context = context;
		}

	

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		public Builder setPhotograph(DialogInterface.OnClickListener listener) {
			this.photographClickListener = listener;
			return this;
		}
		public Builder setPictureLib(DialogInterface.OnClickListener listener) {
			this.pictureLibClickListener = listener;
			return this;
		}
		public Builder setRandom(DialogInterface.OnClickListener listener) {
			this.randomClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public HeadDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final HeadDialog dialog = new HeadDialog(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_head, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			photograph = ((TextView) layout.findViewById(R.id.photograph));
			pictureLib = ((TextView) layout.findViewById(R.id.picture_lib));
			random = ((TextView) layout.findViewById(R.id.random));
			photograph.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					photographClickListener.onClick(dialog,DialogInterface.BUTTON_NEUTRAL);
				}
			});
			pictureLib.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					pictureLibClickListener.onClick(dialog,DialogInterface.BUTTON_NEUTRAL);
				}
			});
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
							}
					});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
			}

			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
								}
					});
				}
			} else {
				layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}



	}
}
