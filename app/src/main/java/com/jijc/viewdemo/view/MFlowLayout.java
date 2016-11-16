package com.jijc.viewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jijc.viewdemo.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MFlowLayout extends LinearLayout {

	/**
	 * 横向间隔
	 */
	private static final int HORIZONTALSPECING = UIUtils.dip2px(13);
	/**
	 * 垂直方向的间隔
	 */
	private static final int VERTICALSPCING =UIUtils.dip2px(13);
	/**
	 * 有多行
	 */
	private List<Line> lines = new ArrayList<MFlowLayout.Line>();
	private Line currentLine; // 当前行
	private int useWidth;// 当前行使用的宽度

	public MFlowLayout(Context context) {
		super(context);
	}

	public MFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 测量的时候调用 会调用很多次 是有责任测量子view
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		lines.clear();// 初始化集合
		currentLine = null;
		useWidth = 0;// 初始化每行使用的 宽度
		// widthMeasureSpec 当前控件 测量规则 mode +size
		// MeasureSpec.AT_MOST;// wrap_content
		// MeasureSpec.EXACTLY;// 精确值 match_parent 40dp
		// MeasureSpec.UNSPECIFIED //未指定
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 通过规则 解析出来 宽的
		// mode /模式
		int widthSize = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();// 通过规则 解析 宽的 值
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);// 通过规则 解析出来 高的
		// mode /模式
		int heightSize = MeasureSpec.getSize(heightMeasureSpec)-getPaddingTop()-getPaddingBottom();// 通过规则 解析 高的 值

		// 如果 父容器 是 精确模式MeasureSpec.EXACTLY ,那 子view 一定是 MeasureSpec.AT_MOST;
		// 其他情况 一定是和父容器的 模式 一样
		// int childMode ;// 子view 的mode
		// if (widthMode == MeasureSpec.EXACTLY) {// 如果父容器是精确值 子view 的mode
		// childMode = MeasureSpec.AT_MOST;
		// }else{
		// childMode = widthMode;// 子view 的模式 = 父容器的模式
		// }

		int childWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
				: widthMode;
		int childHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
				: heightMode;
		// 有 模式了 ---->生成规则
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthMode,
				widthSize);// 值 一定是 和 父容器一样
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
				childHeightMode, heightSize);// 值 一定是 和 父容器一样
		// 将生成的规则 去测量 子view
		// 拿到所有的子view 重新测量
		currentLine = new Line();
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			// 通过measure方法 测量子view 参数为 测量规则
			view.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			// 测量完成后就可以获取 子view 的大小
			int measuredWidth = view.getMeasuredWidth();// 测量后的子view 大小
			useWidth += measuredWidth;// 当前view 的宽度加上
			if (useWidth <= widthSize) { // 如果当前 使用的宽度比 父容器的宽度大 ---换行
				currentLine.addChild(view); // 能添加进去 将当前view 对象添加到当前行
				useWidth += HORIZONTALSPECING;
				if (useWidth > widthSize) {// 添加完 横向间隔后 比 父容器宽度大
					// 换行
					newLine();
				}
			} else {
				// 换行
				newLine();
				// 一定要几个将当前view 添加进去
				useWidth += measuredWidth;
				currentLine.addChild(view);
			}
		}

		// 将最后一行也添加进去
		if (!lines.contains(currentLine)) {
			lines.add(currentLine);
		}

		// 计算所有行高
		int totalHeight = 0;// 行高的和
		for (Line line : lines) {
			int height = line.getHeight();
			totalHeight += height;
		}
		totalHeight += (lines.size() - 1) * VERTICALSPCING;

		// 存储测量后的宽高 (当前容器)

		setMeasuredDimension(widthSize+getPaddingLeft()+getPaddingRight(),
				resolveSize(totalHeight+getPaddingTop()+getPaddingBottom(), heightMeasureSpec));
	}

	/**
	 * 换行 1. 将已经填满的行 添加到集合中 2. 重新创建行对象
	 */
	private void newLine() {
		lines.add(currentLine);
		currentLine = new Line();// 换行
		useWidth = 0;// 清空之前行 使用的宽度
	}

	/**
	 * 放置 子view 的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		l+=getPaddingLeft();
		t+=getPaddingTop();
		for (Line line : lines) {
			line.layout(l, t);
			t += line.getHeight();
			t += VERTICALSPCING;
		}

	}

	/**
	 * 一行
	 *
	 * @author wxj
	 *
	 */
	class Line {
		private List<View> children = new ArrayList<View>();
		private int height;// 行高
		private int lineWidth;// 当前行的宽度

		public void addChild(View view) {
			children.add(view);
			height = view.getMeasuredHeight();
			// 记录更高的值
			if (height < view.getMeasuredHeight()) {
				height = view.getMeasuredHeight();
			}
			lineWidth += view.getMeasuredWidth();//  计算所有的宽度
		}

		/**
		 * 获取行高
		 *
		 * @return
		 */
		public int getHeight() {
			return height;
		}

		/**
		 * 放置子view
		 *
		 * @param l
		 * @param t
		 */
		public void layout(int l, int t) {
//			getMeasuredWidth()// 父容器的宽度
			int remain = getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-lineWidth-(children.size()-1)*HORIZONTALSPECING;
			int raminSpace = 0;
			if (remain != 0) {
				raminSpace = remain / 2;
			}
			for (int i = 0; i < children.size(); i++) {
				View view = children.get(i);
				if (i==0){
					l+=raminSpace;
					view.layout(l, t, l + view.getMeasuredWidth(), t+ view.getMeasuredHeight());
					l+=view.getMeasuredWidth();
					l+=HORIZONTALSPECING;
				}else {

					view.layout(l, t, l+ view.getMeasuredWidth(), t+ view.getMeasuredHeight());
					l+=view.getMeasuredWidth();
					l+=HORIZONTALSPECING;
				}
				//放置 子view 的 实际方法
//				view.layout(l, t, l+ getMeasuredWidth(), t+ getMeasuredHeight());
//				l+=raminSpace/2;
			}
		}

	}
}
