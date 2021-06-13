package com.example.android.booklist;

public class Input {

    private String mtitle;
    private String mauthor;
    private String mimage;
    private  String murl;
    public Input(String title,String author,String image,String url)
    {
        mtitle=title;
        mauthor=author;
        mimage=image;
        murl=url;
    }
    public String getMtitle()
    {
        return mtitle;
    }
    public String getMauthor()
    {
        return mauthor;
    }
    public String getMimage()
    {
        return mimage;
    }
    public String getMurl()
    {
        return murl;
    }
}
