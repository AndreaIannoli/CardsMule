package com.sweng.cardsmule.shared.models;


import java.util.Objects;

public class SwengCardMagic extends SwengCard {
    private static final long serialVersionUID = 2368581922806822154L;
    private String artist;
    private String rarity;

    public SwengCardMagic(String name, String description, String type, String artist, String rarity, String... variants) {
        super(name, description, type, variants);
        this.artist = artist;
        this.rarity = rarity;
    }

    public SwengCardMagic() {
    }

    public String getArtist() {
        return artist;
    }

    public String getRarity() {
        return rarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SwengCardMagic)) return false;
        if (!super.equals(o)) return false;
        SwengCardMagic magicCard = (SwengCardMagic) o;
        return Objects.equals(getArtist(), magicCard.getArtist()) && Objects.equals(getRarity(), magicCard.getRarity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getArtist(), getRarity());
    }
}