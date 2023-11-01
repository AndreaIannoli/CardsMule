package com.sweng.cardsmule.client.widgets;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
//questa classe per la gestione del caso in cui la tabella sia vuota e un  metodo astratto per configurare l'intestazione della tabella.
public abstract class SetUpTableWidget extends Composite {
	protected int noItemsRow;
    @UiField
    HeadingElement tableHeading;
    @UiField(provided = true)
    FlexTable table;
    
    protected void setNoItemsText(String text) {
        noItemsRow = table.getRowCount();
        table.setText(noItemsRow, 0, text);
        table.getFlexCellFormatter().setColSpan(noItemsRow, 0, 3);
    }
    
    protected void setupTable() {
        table = new FlexTable();
        TableElement tableElement = table.getElement().cast();
        TableSectionElement tHead = tableElement.createTHead();
        TableRowElement row = tHead.insertRow(0);
        setupTableHeader(row);
    }
    protected abstract void setupTableHeader(TableRowElement row);
}
