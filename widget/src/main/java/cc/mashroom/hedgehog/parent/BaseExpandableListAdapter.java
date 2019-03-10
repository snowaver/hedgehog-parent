package cc.mashroom.hedgehog.parent;

public  abstract  class  BaseExpandableListAdapter  extends  android.widget.BaseExpandableListAdapter
{
    public  boolean  isChildSelectable( int  groupPosition,int  childPosition )
    {
        return  true;
    }

    public  long  getGroupId( int  groupPosition )
    {
        return  groupPosition;
    }

    public  long  getChildId( int  groupPosition,int  childPosition )
    {
        return  childPosition;
    }

    public  boolean  hasStableIds()
    {
        return  true;
    }
}
