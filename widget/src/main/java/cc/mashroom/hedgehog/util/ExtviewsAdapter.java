package cc.mashroom.hedgehog.util;

import  android.graphics.Typeface;
import  android.widget.TextView;

import  com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import  com.aries.ui.widget.alert.UIAlertDialog;
import  com.aries.ui.widget.progress.UIProgressDialog;

public  class  ExtviewsAdapter
{
    public  static  UIProgressDialog  adapter( UIProgressDialog  progressDialog ,  Typeface  typeface )
    {
        TextView  progressDialogTitle = progressDialog.getMessage();

        if( progressDialogTitle    != null )     progressDialogTitle.setTypeface( typeface );  return     progressDialog;
    }

    public  static  UIAlertDialog  adapter( UIAlertDialog  alertDialog , Typeface  typeface )
    {
        TextView  alertDialogTitle = alertDialog.getTitle();

        if( alertDialogTitle != null )  alertDialogTitle.setTypeface( typeface );

        TextView  alertDialogMessage     = alertDialog.getMessage();

        if( alertDialogMessage     != null )      alertDialogMessage.setTypeface( typeface );  return        alertDialog;
    }

    public  static  UIActionSheetDialog  adapter( UIActionSheetDialog  actionSheetDialog,Typeface  typeface )
    {
        TextView  actionSheetTitle =   actionSheetDialog.getTitle();

        if( actionSheetTitle != null )  actionSheetTitle.setTypeface( typeface );

        TextView  actionSheetCancelTitle = actionSheetDialog.getCancel();

        if( actionSheetCancelTitle != null )  actionSheetCancelTitle.setTypeface( typeface );  return  actionSheetDialog;
    }
}