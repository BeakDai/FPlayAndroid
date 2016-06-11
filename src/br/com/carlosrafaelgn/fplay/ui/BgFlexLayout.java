//
// FPlayAndroid is distributed under the FreeBSD License
//
// Copyright (c) 2013-2014, Carlos Rafael Gimenes das Neves
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
// ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// The views and conclusions contained in the software and documentation are those
// of the authors and should not be interpreted as representing official policies,
// either expressed or implied, of the FreeBSD Project.
//
// https://github.com/carlosrafaelgn/FPlayAndroid
//
package br.com.carlosrafaelgn.fplay.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BgFlexLayout extends ViewGroup {
	public static class LayoutParams extends ViewGroup.LayoutParams {
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams p) {
			super(p);
		}

		public LayoutParams(LayoutParams source) {
			super(source);
		}
	}

	private int flexSize;
	private View flexChild;

	public BgFlexLayout(Context context) {
		this(context, null);
	}

	public BgFlexLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BgFlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setFlexChild(View flexChild) {
		this.flexChild = flexChild;
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	public CharSequence getAccessibilityClassName() {
		return BgFlexLayout.class.getName();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int count = getChildCount();
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		//how much space we have in our hands
		final int availableHeight = ((heightMode == MeasureSpec.UNSPECIFIED) ?
			Integer.MAX_VALUE :
			MeasureSpec.getSize(heightMeasureSpec)) - getPaddingTop() - getPaddingBottom();

		int pass = 0;
		int childState, contentsWidth = 0, contentsHeight;

		int widthMeasureSpecForChildren = widthMeasureSpec;

		do {
			if (pass == 1) {
				pass = 2;
				widthMeasureSpecForChildren = MeasureSpec.makeMeasureSpec(contentsWidth, MeasureSpec.EXACTLY);
			}

			flexSize = 0;

			childState = 0;
			contentsHeight = 0;

			int flexIndex = -1;

			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);

				if (child == null || child.getVisibility() == GONE)
					continue;

				if (child == flexChild) {
					flexIndex = i;
					continue;
				}

				final LayoutParams p = (LayoutParams)child.getLayoutParams();
				final int oldWidth = p.width;
				if (pass == 2)
					p.width = LayoutParams.MATCH_PARENT;
				measureChild(child, widthMeasureSpecForChildren, heightMeasureSpec);
				p.width = oldWidth;

				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				if (pass != 2) {
					pass = 1;
					if (contentsWidth < width)
						contentsWidth = width;
				}

				childState = combineMeasuredStates(childState, child.getMeasuredState());

				contentsHeight += height;
			}

			if (flexIndex >= 0) {
				final View child = getChildAt(flexIndex);

				int heightAvailableForFlex = availableHeight - contentsHeight;
				if (heightAvailableForFlex < 0) heightAvailableForFlex = 0;

				final LayoutParams p = (LayoutParams)child.getLayoutParams();
				final int oldWidth = p.width;
				if (pass == 2)
					p.width = LayoutParams.MATCH_PARENT;
				measureChild(child, widthMeasureSpecForChildren, MeasureSpec.makeMeasureSpec(heightAvailableForFlex, MeasureSpec.AT_MOST));
				p.width = oldWidth;

				final int width = child.getMeasuredWidth();
				flexSize = child.getMeasuredHeight();

				if (pass != 2) {
					pass = 1;
					if (contentsWidth < width)
						contentsWidth = width;
				}

				childState = combineMeasuredStates(childState, child.getMeasuredState());

				contentsHeight += flexSize;
			}

			setMeasuredDimension(resolveSizeAndState(Math.max(contentsWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, childState),
				resolveSizeAndState(Math.max(contentsHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, 0));

			if (pass == 1) {
				contentsWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
				if (contentsWidth < 0) contentsWidth = 0;
			}
		} while (pass != 2);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int childLeft = getPaddingLeft();
		//LinearLayout

		final int count = getChildCount();

		int childTop = getPaddingTop();

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child == null || child.getVisibility() == GONE)
				continue;

			final int childWidth = child.getMeasuredWidth();
			final int childHeight = ((child == flexChild) ? flexSize : child.getMeasuredHeight());

			//int gravity = lp.gravity;
			//if (gravity <= 0) gravity = this.gravity;

			child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

			childTop += childHeight;
		}
	}
}
