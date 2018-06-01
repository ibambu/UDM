package com.ibamb.udm.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.ibamb.udm.R;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.util.TaskBarQuiet;

public class SpeciallySearchActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SearchView specSearch;
    private int resultCode = 0;//返回主线程时会根据这个值判断是否是带关键字搜索设备。

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (specSearch != null) {
                // 得到输入管理对象
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                    imm.hideSoftInputFromWindow(specSearch.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                }
                specSearch.clearFocus(); // 不获取焦点
                resultCode = Constants.FLAG_SPECIALLY_SEARCH;//返回值，表示带关键字搜索。
                finish();
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specially_search);

        mToolbar = (Toolbar) findViewById(R.id.spec_search_toolbar);
        setSupportActionBar(mToolbar);
        this.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.spec_search_menu, menu);
        specSearch = (SearchView) menu.findItem(R.id.sv_specially_search).getActionView();
        specSearch.setSubmitButtonEnabled(true);
        specSearch.setIconifiedByDefault(true);
        specSearch.setFocusable(true);
        specSearch.setIconified(false);
        specSearch.requestFocusFromTouch();
        specSearch.setOnQueryTextListener(this.searchViewListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("SEARCH_KEY_WORD", specSearch.getQuery().toString());
        this.setResult(resultCode, intent);
        super.finish();
    }
}
