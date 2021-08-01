package com.shamlatech.api_response;
import java.io.Serializable;
/**
 * Created by Martin Mundia Mugambi on 12-11-2019.
 */
public class Res_Subject_Report implements Serializable{

    private String subject;
    private String item;
    private String comment;
    private String teacher;


    public String getSubject()
    {
        return subject;
    }

    public void setSubject (String subject)
    {
        this.subject = subject;
    }

    public String getItem ()
    {
        return item;
    }

    public void setItem (String item)
    {
        this.item = item;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getTeacher ()
    {
        return teacher;
    }

    public void setTeacher (String teacher)
    {
        this.teacher = teacher;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [subject = "+subject+", item = "+item+", comment = "+comment+", teacher = "+teacher+"]";
    }
}











