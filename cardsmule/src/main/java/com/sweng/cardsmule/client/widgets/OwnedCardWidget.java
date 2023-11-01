package com.sweng.cardsmule.client.widgets;


import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardEdit;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardRemove;
import com.sweng.cardsmule.client.handlers.HandleOwnedCardSelection;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public class OwnedCardWidget extends Composite {
    private static final OwnedCardUiBinder uiBinder = GWT.create(OwnedCardUiBinder.class);
    @UiField
    OwnedCardStyle style;
    @UiField
    HTMLPanel cardContainer;
    @UiField
    HeadingElement cardName;
    @UiField
    HTMLPanel cardStatus;
    @UiField
    DivElement cardDescription;
    @UiField
    HTMLPanel cardActions;
    
    Button editButton;
    boolean isSelected = false;
    boolean isEditable = false;
    OwnedCardFetched ownedCard;

    public OwnedCardWidget(OwnedCardFetched ownedCard, HandleOwnedCardSelection selectionHandler,
    		HandleOwnedCardRemove removeHandler, HandleOwnedCardEdit editHandler) {
        this.ownedCard = ownedCard;
        initWidget(uiBinder.createAndBindUi(this));
        cardContainer.add(new Hyperlink("Open Details",
                "carddetails/" + ownedCard.getCardGame() + "/" + ownedCard.getReferenceCardId()));

        // selezione carta
        if (selectionHandler != null) {
            cardContainer.addDomHandler(e -> {
                setSelected();
                selectionHandler.changeSelection();
            }, ClickEvent.getType());
        }

        // modifico una carta
        if (editHandler != null) {
            editButton = new Button("Edit", (ClickHandler) e -> {
                e.stopPropagation();
                if (isEditable) {
                    editHandler.onConfirmCardEdit(null, ownedCard.copyWithModifiedStatusAndDescription(
                            Grade.getGrade(Integer.parseInt(((GradeWidget) cardStatus.getWidget(0)).getSelection())),
                            cardDescription.getInnerText()
                    ));
                }
                toggleEditMode();
            });
            editButton.setStyleName(style.editButton());
            cardActions.add(editButton);
        }

        // rimuovo una carta
        if (removeHandler != null) {
            Button deleteButton = new Button("Delete", (ClickHandler) e -> {
                e.stopPropagation();
                if (Window.confirm("Are you sure you want to remove this card?")) {
                    removeHandler.onClickDeleteButton(ownedCard);
                }
            });
            deleteButton.setStyleName(style.deleteButton());
            cardActions.add(deleteButton);
        }

        cardName.setInnerText(ownedCard.getName());
        setOwnedCard();
    }

    public OwnedCardFetched getOwnedCard() {
        return ownedCard;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected() {
        if (!isEditable) {
            isSelected = !isSelected;
            if (isSelected) {
                getElement().addClassName(style.cardSelected());
            } else {
                getElement().removeClassName(style.cardSelected());
            }
        }
    }

    private void toggleEditMode() {
        isEditable = !isEditable;
        if (isEditable) {
            getElement().removeClassName(style.cardSelected());
            isSelected = false;
            editButton.addStyleName(style.toggle());
            cardDescription.setAttribute("contenteditable", "true");
            cardStatus.getElement().setInnerText("");
            cardStatus.add(new GradeWidget());
        } else {
            editButton.removeStyleName(style.toggle());
            cardDescription.removeAttribute("contenteditable");
            setOwnedCard();
        }
    }

    private void setOwnedCard() {
        cardStatus.getElement().setInnerText(ownedCard.getGrade().name());
        cardDescription.setInnerText(ownedCard.getDescription());
    }

    interface OwnedCardStyle extends CssResource {
        String cardSelected();

        String deleteButton();

        String editButton();

        String toggle();
    }

    interface OwnedCardUiBinder extends UiBinder<Widget, OwnedCardWidget> {
    }
}