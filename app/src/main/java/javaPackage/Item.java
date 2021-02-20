package javaPackage;

public class Item {
    int icon;
    String song;
    String singer;

    public Item(int icon, String song, String singer)
    {
        this.icon=icon;
        this.song=song;
        this.singer=singer;
    }

    public int getIcon()
    {
        return icon;
    }
    public String getSong()
    {
        return song;
    }

    public String getSinger()
    {
        return singer;
    }

    public void setIcon(int icon)
    {
        this.icon=icon;
    }

    public void setSong(String song)
    {
        this.song=song;
    }
    public void setSinger(String singer)
    {
        this.singer=singer;
    }

    public int getType()
    {
        return 1;
    }

}
