package com.codeeraayush.firestoreimage;

public class Upload {
    private String Title;
   private String Caption;
    private String imageUrl;

    public Upload() {
    }

    public Upload(String title, String caption, String imageUrl) {
        Title = title;
        Caption = caption;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
