package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 12-Jun-18.
 */

public class Res_Forum_Comments implements Serializable {
    private String comment_id;

    private String comment_on;

    private String comment_by_name;

    private String comment_by_id;

    private String comment;

    public String getComment_id ()
    {
        return comment_id;
    }

    public void setComment_id (String comment_id)
    {
        this.comment_id = comment_id;
    }

    public String getComment_on ()
    {
        return comment_on;
    }

    public void setComment_on (String comment_on)
    {
        this.comment_on = comment_on;
    }

    public String getComment_by_name ()
    {
        return comment_by_name;
    }

    public void setComment_by_name (String comment_by_name)
    {
        this.comment_by_name = comment_by_name;
    }

    public String getComment_by_id ()
    {
        return comment_by_id;
    }

    public void setComment_by_id (String comment_by_id)
    {
        this.comment_by_id = comment_by_id;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [comment_id = "+comment_id+", comment_on = "+comment_on+", comment_by_name = "+comment_by_name+", comment_by_id = "+comment_by_id+", comment = "+comment+"]";
    }
}
