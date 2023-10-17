package com.sweng.cardsmule.client.widgets;


import java.util.List;
import java.util.function.Function;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.UserCard;

public class UsersListWidget extends ListWidget{
    private static final UserListUIBinder uiBinder = GWT.create(UserListUIBinder.class);
    private static final String NO_USERS_TEXT = "No users found";
    boolean showExchangeButton;
	
    interface UserListUIBinder extends UiBinder<Widget, UsersListWidget> {
    }
    
    @UiConstructor
    public UsersListWidget(String title, boolean showExchangeButton) {
        this.showExchangeButton = showExchangeButton;
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        setNoItemsText(NO_USERS_TEXT);
    }

    @Override
    protected void setupTableHeader(TableRowElement row) {
        row.insertCell(0).setInnerText("User");
        row.insertCell(1).setInnerText("Grade");
        if (showExchangeButton) row.insertCell(2).setInnerText("");
    }

    public void setTable(List<? extends UserCard> pCards, Function<UserCard, Button> createButton) {
        if (!pCards.isEmpty()) table.removeRow(noItemsRow);
        pCards.forEach(pCard -> addRow(pCard.getUserEmail(), pCard.getGrade(), createButton.apply(pCard)));
    }

    private void addRow(String email, Grade grade, Button button) {
        int numRows = (table.getRowCount());
        table.setText(numRows, 0, email);
        table.setText(numRows, 1, (grade.getValue() + " (" + grade.name() + ")"));
        if (showExchangeButton) table.setWidget(numRows, 2, button);
    }
}
