package com.ibamb.udm.tag;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ibamb.udm.R;

/**
 * Created by luotao on 18-3-17.
 */

public class UdmSpinner extends LinearLayout {
    private EditText editText;
    private Button button;
    private String[] optitions;

    public UdmSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int attrCount = attrs.getAttributeCount();
        for(int i =0;i<attrCount;i++){
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
            if("entries".equals(name)){
                optitions = getResources().getStringArray(Integer.parseInt(value.replaceAll("@","")));
                break;
            }
        }
        View view = LayoutInflater.from(context).inflate(R.layout.tag_udm_spinner_layout, this);
        editText = (EditText)view.findViewById(R.id.tag_udm_edit_text);
        button = (Button) view.findViewById(R.id.tag_udm_btn_spinner);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setItems(optitions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        editText.setText(optitions[which]);
                    }
                });
                builder.show();
            }
        });
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getValue(){
        return editText.getText().toString();
    }
    public void setValue(String value){
        editText.setText(value);
    }

    public String[] getOptitions() {
        return optitions;
    }

    public void setOptitions(String[] optitions) {
        this.optitions = optitions;
    }

    public Button getButton() {
        return button;
    }
}
