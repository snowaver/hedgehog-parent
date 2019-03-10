package cc.mashroom.hedgehog.widget;

import  android.content.Context;
import  android.util.AttributeSet;
import  android.widget.AbsListView;
import  android.widget.AdapterView;
import  android.widget.BaseExpandableListAdapter;
import  android.widget.ExpandableListView;
import  android.widget.FrameLayout;

import  cc.mashroom.util.ObjectUtils;
import  lombok.AccessLevel;
import  lombok.Getter;
import  lombok.Setter;
import  lombok.experimental.Accessors;

public  class  PinnedHeaderExpandableListViewLayout  extends  FrameLayout  implements  ExpandableListView.OnScrollListener
{
	public  PinnedHeaderExpandableListViewLayout( Context  context,AttributeSet  attributeSet )
	{
		super(  context, attributeSet );
	}

	@Getter( value=AccessLevel.PRIVATE )
	@Setter
	@Accessors( chain=true )
	protected  int  indicatorGroupId= 0;
	@Getter( value=AccessLevel.PRIVATE )
	@Setter
	@Accessors( chain=true )
	protected  int  indicatorGroupHeight = 0;
	@Getter
	@Setter
	@Accessors( chain=true )
	protected  BaseExpandableListAdapter    expandableListAdapter;
	@Getter
	@Setter
	@Accessors( chain=true )
	protected  ExpandableListView   expandableListView;

	public  PinnedHeaderExpandableListViewLayout    notifyExpandableListAdapterDatasetChanged()
	{
		expandableListAdapter.notifyDataSetChanged(  );

		return  this;
	}

	public  void  onScroll(  AbsListView  listview,int  firstVisibleItem,int  visibleItemCount,int  totalItemCount )
	{
		ExpandableListView  expandableListView = ObjectUtils.cast( listview,ExpandableListView.class );

		int  currentGroupPosition = caculateGroupPosition( expandableListView,0,0 );
		
		if( currentGroupPosition == AdapterView.INVALID_POSITION )
		{
			return;
		}
		
		FrameLayout  indicatorGroupLayout= ObjectUtils.cast( super.findViewWithTag("header") );
		
		if( indicatorGroupLayout.getChildCount() == 0 )
		{
			indicatorGroupLayout.addView( expandableListView.getExpandableListAdapter().getGroupView(currentGroupPosition,expandableListView.isGroupExpanded(currentGroupPosition),null,null) );
		}

		setIndicatorGroupHeight(expandableListView.getExpandableListAdapter().getGroupView(currentGroupPosition,expandableListView.isGroupExpanded(currentGroupPosition),indicatorGroupLayout.getChildAt(0),null).getHeight()).setIndicatorGroupId( currentGroupPosition != indicatorGroupId ? currentGroupPosition : indicatorGroupId );

		int  topnextGroupPosition = caculateGroupPosition( expandableListView,0,indicatorGroupHeight );
		
		if( topnextGroupPosition == AdapterView.INVALID_POSITION )
		{
			return;
		}

		MarginLayoutParams  indicatorGroupLayoutParams = ObjectUtils.cast( indicatorGroupLayout.getLayoutParams() );

		indicatorGroupLayoutParams.topMargin = (topnextGroupPosition != indicatorGroupId ? expandableListView.getChildAt(expandableListView.pointToPosition(0,indicatorGroupHeight)-expandableListView.getFirstVisiblePosition()).getTop() : indicatorGroupHeight)-indicatorGroupHeight;

		indicatorGroupLayout.setLayoutParams( indicatorGroupLayoutParams );
	}

	public  void  onScrollStateChanged(AbsListView  view,int  scrollState )
	{

	}

	public  PinnedHeaderExpandableListViewLayout expandAllGroups()
	{
		for( int  i = 0;i <= expandableListView.getExpandableListAdapter().getGroupCount()-1; i = i+1 )
		{
			expandableListView.expandGroup( i );
		}

		return  this;
	}

	public  PinnedHeaderExpandableListViewLayout  setAdapter( BaseExpandableListAdapter  baseExpandableListAdapter )
	{
		setExpandableListView( ObjectUtils.cast( super.findViewWithTag("content"), ExpandableListView.class ) );

		expandableListView.setOnScrollListener( this );

		expandableListView.setAdapter( this.expandableListAdapter= baseExpandableListAdapter );

		return  this;
	}

	private  int  caculateGroupPosition( ExpandableListView  expandableListView,int  x,int  y )
	{
		int  position = expandableListView.pointToPosition( x,y );

		return  position == AdapterView.INVALID_POSITION ? AdapterView.INVALID_POSITION : expandableListView.getPackedPositionGroup( expandableListView.getExpandableListPosition( position ) );
	}
}
