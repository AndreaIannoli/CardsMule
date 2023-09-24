package com.sweng.cardsmule.shared.models;


import java.util.Objects;

public class SwengYuGiOhCard extends SwengCard {
    private static final long serialVersionUID = -5460276673235842624L;
    private String race;
    private String imageUrl;
    private String smallImageUrl;

    public SwengYuGiOhCard(String name, String description, String type, String race, String imageUrl, String smallImageUrl) {
        super(name, description, type);
        this.race = race;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
    }

    public SwengYuGiOhCard() {
    }

    public String getRace() {
        return race;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SwengYuGiOhCard)) return false;
        if (!super.equals(o)) return false;
        SwengYuGiOhCard that = (SwengYuGiOhCard) o;
        return Objects.equals(getRace(), that.getRace()) && Objects.equals(getImageUrl(), that.getImageUrl()) && Objects.equals(getSmallImageUrl(), that.getSmallImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRace(), getImageUrl(), getSmallImageUrl());
    }
}