package com.ibamb.udm.tag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ibamb.udm.R;

/**
 * TextEdit center with reduce button left and add button right.
 */

public class IncDecTextEdit extends LinearLayout {

    private EditText valueEditText;
    private Button incrButton;
    private Button decrButton;

    public String getValueEditText() {
        return valueEditText.getText().toString();
    }

    public void setValueEditText(String value) {
        valueEditText.setText(value);
    }

    private class ButtonOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String editTextValue = valueEditText.getText().toString();
            System.out.println("editTextValue:" + editTextValue);
            if (editTextValue == null || editTextValue.trim().length() == 0) {
                editTextValue = "0";
            }
            int value = Integer.parseInt(editTextValue);
            switch (v.getId()) {
                case R.id.incr_button:
                    editTextValue=String.valueOf(value+1);
                    valueEditText.setText(editTextValue.toCharArray(), 0, editTextValue.length());
                    break;
                case R.id.decr_button:
                    editTextValue=String.valueOf(value-1);
                    valueEditText.setText(editTextValue.toCharArray(), 0, editTextValue.length());
                    break;
                default:
                    break;
            }
        }
    }

    public IncDecTextEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.tag_incrdecr_textedit_layout, this);

        incrButton = (Button) findViewById(R.id.incr_button);
        decrButton = (Button) findViewById(R.id.decr_button);
        valueEditText = (EditText) findViewById(R.id.value_edit_text);

        incrButton.setOnClickListener(new ButtonOnclickListener());
        decrButton.setOnClickListener(new ButtonOnclickListener());
    }
}
