package com.eshel.jump;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Eshel on 2019/4/8.
 */

public class ImpeccableAtTextWatcher implements TextWatcher {
	public static final String TAG = "AtTextWatcher";

	private int color;
	private char atEndFlag = (char) 8197;
	private AtListener mListener;
	private int atIndex = -1;
	private int endFlagIndex = -1;
	private EditText et;
	private List<AtUserBean> callUsers;
	private List<Object> cacheSpans = new ArrayList<>();

	private boolean needNotifyAtColor;
	private boolean closeListener;

	public ImpeccableAtTextWatcher(EditText et, int highLightColor, AtListener listener) {
		this.et = et;
		this.color = highLightColor;
		this.mListener = listener;
	}

	public List<AtUserBean> getCallUsers() {
		return callUsers;
	}

	/**
	 * 点击 @ 按钮使用此方法插入带 @ 的高亮字符
	 */
	public void insertTextForAtIndex(CharSequence nickName, long userId) {
		if (containsUser(userId))//去重逻辑
			return;

		int selectionStart = et.getSelectionStart();
		int selectionEnd = et.getSelectionEnd();

		if (selectionEnd > selectionStart) {
			selectionStart = selectionEnd;
		}
		int atIndex = selectionStart;

		closeListener = true;
		et.getText().insert(atIndex, "@");
		closeListener = false;

		this.atIndex = atIndex;// -- \\外部传入的是光标的位置, 而成员变量为 @ 字符的位置
		StringBuilder sb = new StringBuilder();
		insertTextForAtInternal(et, nickName, sb, userId);
	}

	public boolean containsUser(long userId) {
		if (callUsers == null || callUsers.isEmpty())
			return false;

		for (AtUserBean callUser : callUsers) {
			if (callUser != null && callUser.userId == userId)
				return true;
		}
		return false;
	}

	public void insertTextForAt(CharSequence nickName, long userId) {
		if (atIndex == -1)
			return;

		containsUser(userId);
		StringBuilder sb = new StringBuilder();
		insertTextForAtInternal(et, nickName, sb, userId);
	}

	private void insertTextForAtInternal(EditText et, CharSequence text, StringBuilder sb, long userId) {
		sb.append(text);
		sb.append(atEndFlag);
		text = sb.toString();
/*        SpannableString ss = new SpannableString(text);
        if(color != 0) {
            ss.setSpan(new ForegroundColorSpan(color), 0 , ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/

		Editable text1 = et.getText();
		int start = atIndex;
		int length = text.length() + 1;
		text1.insert(atIndex + 1, text);
		disposeCallusers(start, length, userId);
		notifyAtColor();
	}

	private void notifyAtColor() {
		needNotifyAtColor = false;
		if (color == 0)
			return;
		Editable text = et.getText();
		if (!cacheSpans.isEmpty()) {
			for (Object span : cacheSpans) {
				text.removeSpan(span);
			}
			cacheSpans.clear();
		}


		if (callUsers == null || callUsers.isEmpty())
			return;

		SpannableStringBuilder sb;
		if (text instanceof SpannableStringBuilder) {
			sb = (SpannableStringBuilder) text;
		} else {
			sb = new SpannableStringBuilder(text);
		}

		for (AtUserBean callUser : callUsers) {
			if (callUser != null) {
				ForegroundColorSpan span = new ForegroundColorSpan(color);
				sb.setSpan(span, callUser.range.getStart(), callUser.range.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				cacheSpans.add(span);
			}
		}

		if (!(text instanceof SpannableStringBuilder)) {
			et.setText(sb);
		}
	}

	private AtUserBean disposeCallusers(int start, int length, long userId) {
		if (callUsers == null) {
			callUsers = new ArrayList<>();
		}
		AtUserBean atInfo = new AtUserBean(userId, start, length);
		callUsers.add(atInfo);
		return atInfo;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (count == 1) {//删除一个字符
			char c = s.charAt(start);
			if (c == atEndFlag) {
				endFlagIndex = start;
				return;
			}
		}

		if (count != 0) {//删除字符
			int end = start + count;
			disposeDelAt(start, end);
		}

		if (after != 0) {//插入字符
			//此处传入 count 而不是 end 是因为 beforeTextChanged 时还未真实插入字符, 故 end 无用
			disposeInsertAt(start, after);
		}
	}

	private void disposeInsertAt(int start, int count) {
		if (callUsers == null || callUsers.isEmpty())
			return;

		Iterator<AtUserBean> it = callUsers.iterator();

		while (it.hasNext()) {
			AtUserBean next = it.next();
			if (next == null || next.range == null)
				continue;

			if (start > next.range.getStart() && start < next.range.getEnd()) {
				it.remove();
				needNotifyAtColor = true;
				continue;
			} else {
				//未影响高亮区域, 不做处理
			}

			if (start >= next.range.getEnd()) {
				//未影响该处高亮@字符, 不处理
			} else {
				//受到插入影响, 需要处理
				//如能走到此处, 则插入位置的肯定不在高亮区域内

				//加上插入的字符数量
				next.range.from += count;
			}
		}
	}

	private void disposeDelAt(int start, int end) {
		if (callUsers == null || callUsers.isEmpty())
			return;

		Iterator<AtUserBean> it = callUsers.iterator();

		while (it.hasNext()) {
			AtUserBean next = it.next();
			if (next == null || next.range == null)
				continue;

			if (end <= next.range.getStart() || start >= next.range.getEnd()) {
				//未删除高亮区域, 不做处理
			} else {
				it.remove();
				needNotifyAtColor = true;
				continue;
			}

			if (start >= next.range.getEnd()) {
				//未影响该处高亮@字符, 不处理
			} else {
				//受到删除影响, 需要处理
				//如能走到此处, 则删除的肯定不包含高亮区域

				//减去删除的字符数量
				next.range.from -= (end - start);
			}
		}
	}

	/**
	 * @param s      新文本内容，即文本改变之后的内容
	 * @param start  被修改文本的起始偏移量
	 * @param before 被替换旧文本长度
	 * @param count  替换的新文本长度
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (count == 1) {//新增(输入)一个字符
			char c = s.charAt(start);
			if (c == '@') {
				atIndex = start;
				if (mListener != null && !closeListener) {
					mListener.triggerAt();
				}
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.i(TAG, "afterTextChanged() called with: s = [" + s + "]");
		if (endFlagIndex != -1) {
			int index = endFlagIndex;
			while ((index -= 1) != -1) {
				char c = s.charAt(index);
				if (c == '@') {
					break;
				}
			}
			int endFlagIndex = this.endFlagIndex;
			this.endFlagIndex = -1;
			//endFlagIndex 是@字符串结束符号位置, 所以真实结束位置需要加1
			if (index != -1 && contains(index, endFlagIndex + 1))
				s.delete(index, endFlagIndex);
		}

		if (needNotifyAtColor) {
			notifyAtColor();
		}
	}

	/**
	 * 删除的位置是否包含高亮字符
	 */
	private boolean contains(int start, int end) {
		if (callUsers == null || callUsers.isEmpty())
			return false;

		for (AtUserBean at : callUsers) {
			if (at.range == null)
				continue;
			if (at.range.getStart() == start && at.range.getEnd() == end)
				return true;
		}

		return false;
	}

	/**
	 * 输入 @ 监听
	 */
	public interface AtListener {
		/**
		 * 通过软键盘输入@字符触发的监听
		 */
		void triggerAt();
	}
}
