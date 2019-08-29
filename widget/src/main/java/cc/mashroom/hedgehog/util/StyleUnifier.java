package cc.mashroom.hedgehog.util;

import  android.graphics.Typeface;
import  android.widget.TextView;

import  com.aries.ui.widget.BasisDialog;
import  com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import  com.aries.ui.widget.alert.UIAlertDialog;
import  com.aries.ui.widget.progress.UIProgressDialog;
import  com.google.common.collect.Lists;

import  java.util.List;

import  cc.mashroom.util.ObjectUtils;

public  class  StyleUnifier
{
    public  static  <T extends BasisDialog   <?>>  T  unify( BasisDialog<T>  dialog,Typeface  typeface )
    {
        for( TextView  text : getDialogTexts(dialog) )
        {
            if( text != null )  text.setTypeface( typeface );
        }
        return  (T) dialog;
    }

    public  static  List<TextView>  getDialogTexts( BasisDialog<?>  dialog )
    {
        if( dialog  instanceof UIProgressDialog )
        {
            return  Lists.newArrayList( ObjectUtils.cast(dialog,UIProgressDialog.class ).getMessage() );
        }
        else
        if( dialog  instanceof UIActionSheetDialog   )
        {
            return  Lists.newArrayList( ObjectUtils.cast(dialog,UIActionSheetDialog.class).getTitle(),ObjectUtils.cast(dialog,UIActionSheetDialog.class).getCancel() );
        }
        else
        if( dialog  instanceof UIAlertDialog    )
        {
            return  Lists.newArrayList( ObjectUtils.cast(dialog,UIAlertDialog.class).getTitle(),ObjectUtils.cast(dialog,UIAlertDialog.class).getMessage() );
        }
        else
        {
            return  Lists.newArrayList();
        }
    }
}