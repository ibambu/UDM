package com.ibamb.udm.tag;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.module.instruct.beans.ValueMapping;

import java.util.List;

/**
 * Created by luotao on 18-3-17.
 */

public class UdmSpinner extends LinearLayout {

    private TextView vAttrValue;
    private ImageView vImageView;
    private String[] optitions;

    public UdmSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (getTag() != null) {
            Parameter parameter = ParameterMapping.getMappingByTagId(getTag().toString().toUpperCase(),null);
            if (parameter != null) {
                List<ValueMapping> displayEnumValues = parameter.getValueMappings();
                if (displayEnumValues != null && !displayEnumValues.isEmpty()) {
                    optitions = new String[displayEnumValues.size()];
                    for (int i = 0; i < displayEnumValues.size(); i++) {
                        optitions[i] = displayEnumValues.get(i).getDisplayValue();
                    }
                }
            }
        } else {
            int attrCount = attrs.getAttributeCount();
            for (int i = 0; i < attrCount; i++) {
                String name = attrs.getAttributeName(i);
                String value = attrs.getAttributeValue(i);

                if ("entries".equals(name)) {
                    optitions = getResources().getStringArray(Integer.parseInt(value.replaceAll("@", "")));
                    break;
                }
            }
        }
        View view = LayoutInflater.from(context).inflate(R.layout.tag_udm_spinner_layout, this);
        vAttrValue = (TextView) view.findViewById(R.id.attr_value);
        vImageView = (ImageView) view.findViewById(R.id.show_more_icon);
        vImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                int checkItemIdx = 0;
                if(optitions!=null){
                    for(int i=0;i<optitions.length;i++){
                        if(optitions[i].equalsIgnoreCase(vAttrValue.getText().toString())){
                            checkItemIdx = i;
                            break;
                        }
                    }
                    builder.setSingleChoiceItems(optitions, checkItemIdx,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vAttrValue.setText(optitions[which]);
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

            }
        });
        vAttrValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                int checkItemIdx = 0;
                if(optitions!=null){
                    for(int i=0;i<optitions.length;i++){
                        if(optitions[i].equalsIgnoreCase(vAttrValue.getText().toString())){
                            checkItemIdx = i;
                            break;
                        }
                    }
                    builder.setSingleChoiceItems(optitions, checkItemIdx,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vAttrValue.setText(optitions[which]);
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
    }


    public String getValue() {
        return vAttrValue.getText().toString();
    }

    public void setValue(String value) {
        vAttrValue.setText(value);
    }

    public String[] getOptitions() {
        return optitions;
    }

    public void setOptitions(String[] optitions) {
        this.optitions = optitions;
    }

}
