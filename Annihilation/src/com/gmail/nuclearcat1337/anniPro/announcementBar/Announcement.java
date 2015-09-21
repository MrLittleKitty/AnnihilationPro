package com.gmail.nuclearcat1337.anniPro.announcementBar;

public final class Announcement
{
    private String message;
    private Runnable callBack;
    private int time;
    private boolean permanent;

    public Announcement(String message)
    {
        this.message = message;
        permanent = true;
        callBack = null;
    }

    public String getMessage()
    {
        return message;
    }

    public Announcement setMessage(String str)
    {
        this.message = str;
        return  this;
    }

    public Announcement setTime(int time)
    {
        this.time = time;
        if(this.time > 0)
            permanent = false;
        return  this;
    }

    public Announcement setPermanent(boolean permanent)
    {
        this.permanent = permanent;
        if(permanent)
            time = 0;
        return  this;
    }

    public Announcement setCallback(Runnable callBack)
    {
        this.callBack = callBack;
        return  this;
    }

    public Runnable getCallBack()
    {
        return callBack;
    }

    public boolean isPermanent()
    {
        return permanent;
    }

    public int getTime()
    {
        return time;
    }

    public void destroy()
    {
        message = null;
        callBack = null;
    }



}
