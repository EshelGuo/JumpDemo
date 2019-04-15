package com.eshel.imageloader.testreply;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ReplyTextView extends LinearLayout {
	private static final char endFlag = (char) 8198;

	private TextView firstView;
	private TextView secondView;
	private TextView thirdlyView;
	private TextView moreView;

	private int residueLine = 3;//剩余行数
	private int showReplyLength = 0;//显示回复条数

	private List<ReplyData> replyDatas = new ArrayList<>(3);
	private ReplyInitHandler handler = new ReplyInitHandler() {
		@Override
		public void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more) {
		}

		@Override
		public String getMoreText(int replyLength) {
			return String.format(Locale.CHINA, "共有%d条回复", replyLength);
		}
	};
	private float mPhotoIconWidth;
	private float mEllipsisWidth;
	//获取[查看图片]文本
	private String mPhotoText;
	//获取 查看图片 icon 资源ID
	private int mPhotoIconId;
	//获取 查看图片 颜色
	private int mPhotoTextColor;
	//获取作者颜色
	private int mAuthorTextColor;
	//获取内容颜色
	private int mContentTextColor;
	//获取更多按钮颜色
	private int mMoreTextColor;
	//点击更多事件
	private OnClickListener moreClickListener;
	//点击作者事件
	private AuthorOnClickListener mAuthorClickListener;
	//点击图片事件
	private PhotoOnClickListener mPhotoOnClickListener;

	int allReplyCount;

	public ReplyTextView(Context context) {
		this(context, null);
	}

	public ReplyTextView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ReplyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		setOrientation(VERTICAL);
		View.inflate(getContext(), R.layout.view_reply, this);
		firstView = findViewById(R.id.reply_first);
		secondView = findViewById(R.id.reply_second);
		thirdlyView = findViewById(R.id.reply_thirdly);
		moreView = findViewById(R.id.reply_more);

		firstView.setOnTouchListener(new ClickSpanOnTouchListener());
		secondView.setOnTouchListener(new ClickSpanOnTouchListener());
		thirdlyView.setOnTouchListener(new ClickSpanOnTouchListener());
		moreView.setOnTouchListener(new ClickSpanOnTouchListener());

		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ReplyTextView);
		float px = ta.getDimensionPixelSize(R.styleable.ReplyTextView_reply_text_size, 13);
		mPhotoText = ta.getString(R.styleable.ReplyTextView_reply_photo_show_text);
		mPhotoIconId = ta.getResourceId(R.styleable.ReplyTextView_reply_photo_show_icon_id, R.drawable.icon_reply);
		mPhotoTextColor = ta.getColor(R.styleable.ReplyTextView_reply_photo_show_text_color, Color.RED);
		mAuthorTextColor = ta.getColor(R.styleable.ReplyTextView_reply_author_text_color, Color.BLACK);
		mContentTextColor = ta.getColor(R.styleable.ReplyTextView_reply_content_text_color, Color.BLACK);
		mMoreTextColor = ta.getColor(R.styleable.ReplyTextView_reply_more_text_color, Color.BLUE);
		ta.recycle();

		setTextSizePx((int) px);
	}

	public void setPhotoOnClickListener(PhotoOnClickListener listener) {
		mPhotoOnClickListener = listener;
		notifyReply(allReplyCount);
	}

	public void setMoreClickListener(OnClickListener listener) {
		moreClickListener = listener;
		notifyReply(allReplyCount);
	}

	public void setAuthorClickListener(AuthorOnClickListener listener) {
		mAuthorClickListener = listener;
		notifyReply(allReplyCount);
	}

	public void setPhotoText(String text) {
		mPhotoText = text;
		notifyReply(allReplyCount);
	}

	public void setPhotoIcon(int resId) {
		mPhotoIconId = resId;
		notifyReply(allReplyCount);
	}

	public void setPhotoTextColor(int color) {
		mPhotoTextColor = color;
		notifyReply(allReplyCount);
	}

	public void setAuthorTextColor(int color) {
		mAuthorTextColor = color;
		notifyReply(allReplyCount);
	}

	public void setContentTextColor(int color) {
		mContentTextColor = color;
		notifyReply(allReplyCount);
	}

	public void setMoreTextColor(int color) {
		mMoreTextColor = color;
		notifyReply(allReplyCount);
	}

	public void setTextSize(int sp) {
		firstView.setTextSize(sp);
		secondView.setTextSize(sp);
		thirdlyView.setTextSize(sp);
		moreView.setTextSize(sp);
	}

	public void setTextSizePx(int px) {
		firstView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
		secondView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
		thirdlyView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
		moreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
	}

	public void initTextsStyle(ReplyInitHandler handler) {
		if (handler == null)
			return;
		this.handler = handler;
		handler.initTextStyle(firstView, secondView, thirdlyView, moreView);
	}

	public void resetReply() {
		replyDatas.clear();
	}

	public void addReply(String author, String content, String photoUrl) {
		replyDatas.add(new ReplyData(author, content, photoUrl));
	}

	public void notifyReply(int allReplyCount) {
		this.allReplyCount = allReplyCount;

		residueLine = 3;
		showReplyLength = 0;

		int size = replyDatas.size();
		if (size == 0) {
			setVisibility(GONE);
			return;
		} else {
			setVisibility(VISIBLE);
		}
		notifyReplyText(replyDatas.get(0), firstView);

		notifyReplyText(size >= 2 ? replyDatas.get(1) : null, secondView);
		notifyReplyText(size >= 3 ? replyDatas.get(2) : null, thirdlyView);


		if (allReplyCount > showReplyLength) {
			moreView.setVisibility(VISIBLE);
			String moreText = handler.getMoreText(allReplyCount);
			SpannableString ss = new SpannableString(moreText);
			ss.setSpan(new ForegroundColorSpan(mMoreTextColor), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if (moreClickListener != null)
						moreClickListener.onClick(widget);
				}
			}, 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			moreView.setText(ss);
		} else {
			moreView.setVisibility(GONE);
		}
	}

	private void notifyReplyText(final ReplyData data, TextView tv) {
		if (residueLine <= 0 || data == null) {
			tv.setVisibility(GONE);
			return;
		} else {
			tv.setVisibility(VISIBLE);
		}

		int iconId = mPhotoIconId;
		TextPaint paint = tv.getPaint();
		String iconText = "";

		if (data.photoUrl != null) {
			iconText = "[i]";
			if (mPhotoIconWidth == 0) {
				mPhotoIconWidth = new ImageSpan(getContext(), iconId).getDrawable().getIntrinsicWidth();
			}
		}

		if (mEllipsisWidth == 0) {
			String ellipsisSymbol = "...";
			mEllipsisWidth = paint.measureText(ellipsisSymbol);
		}

		float authorWidth = paint.measureText(data.author);

		String photoText = "";
		float photoWidth = 0;

		if (data.photoUrl != null) {
			photoText = mPhotoText + endFlag;
			photoWidth = paint.measureText(photoText);
		}

		int width = tv.getWidth();
		if (width == 0) {
			tv.measure(0, 0);
			width = tv.getMeasuredWidth();
		}
		int maxLine = 2;
		if (maxLine > residueLine)
			maxLine = residueLine;
		int lineWidth = width - tv.getPaddingLeft() - tv.getPaddingRight();
		CharSequence contentText = TextUtils.ellipsize(data.content, paint, lineWidth * 2 - mPhotoIconWidth - authorWidth - photoWidth - mEllipsisWidth, TextUtils.TruncateAt.END);

		CharSequence wholeText = data.author + contentText + iconText + photoText;

		int authorStart = 0;
		int authorEnd = data.author.length() + authorStart;

		int contentStart = authorEnd;
		int contentEnd = contentStart + contentText.length();

		int iconStart = -1;
		int iconEnd = -1;

		if (data.photoUrl != null) {
			iconStart = contentEnd;
			iconEnd = iconStart + iconText.length();
		}

		int imageTextStart = -1;
		int imageTextEnd = -1;

		if (data.photoUrl != null) {
			imageTextStart = iconEnd;
			imageTextEnd = imageTextStart + photoText.length() - 1;
		}

		SpannableString ss = new SpannableString(wholeText);
		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				if (mAuthorClickListener != null)
					mAuthorClickListener.onClick(widget, replyDatas.indexOf(data), data.author);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		}, authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(mAuthorTextColor), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		ss.setSpan(new ForegroundColorSpan(mContentTextColor), contentStart, contentEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		if (imageTextStart != -1) {
			ImageSpan imageSpan = new CenterAlignImageSpan(getContext(), iconId);
			ss.setSpan(imageSpan, iconStart, iconEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

			ss.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if (mPhotoOnClickListener != null)
						mPhotoOnClickListener.onClick(widget, replyDatas.indexOf(data), data.photoUrl);
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
				}
			}, iconStart, imageTextEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new ForegroundColorSpan(mPhotoTextColor), imageTextStart, imageTextEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		tv.setText(ss);
		int lineCount = tv.getLineCount();
		if (lineCount > maxLine) {
			tv.setLines(maxLine);
			lineCount = maxLine;
		}

		residueLine -= lineCount;
		showReplyLength++;
	}

	public interface ReplyInitHandler {
		//初始化文字样式
		void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more);

		//获取更多按钮文本
		String getMoreText(int replyLength);
	}

	public static class ReplyData {
		public String author;
		public String content;
		public String photoUrl;

		public ReplyData(String author, String content, String photoUrl) {
			this.author = author;
			this.content = content;
			this.photoUrl = photoUrl;
		}
	}

	public int dp2px(float dpValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public interface AuthorOnClickListener {
		//index 最小为0 , 最大为2
		void onClick(View widget, int index, String authorName);
	}

	public interface PhotoOnClickListener {
		//index 最小为0 , 最大为2
		void onClick(View widget, int index, String photoUrl);
	}
}
