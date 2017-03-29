package com.vladan.dragdropimages;

/**
 * Created  on 3/26/2017.
 */

public class BasisGrid {
    private String textComment;
    private int imageId;

    public BasisGrid(){

    }
    public BasisGrid(String textComment,int imageId){
        this.textComment=textComment;
        this.imageId=imageId;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
