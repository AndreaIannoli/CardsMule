package com.sweng.cardsmule.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class GradeWidget extends Composite {
	private static final StatusWidgetUiBinder uiBinder = GWT.create(StatusWidgetUiBinder.class);
    @UiField
    ListBox grade;

    public GradeWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public String getSelection() {
        return grade.getSelectedValue();
    }

    public void clearSelection() {
        grade.setItemSelected(0, true);
    }

    interface StatusWidgetUiBinder extends UiBinder<Widget, GradeWidget> {
    }
}
