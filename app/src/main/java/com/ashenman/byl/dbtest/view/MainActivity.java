package com.ashenman.byl.dbtest.view;

import android.os.Bundle;

import com.ashenman.byl.dbtest.manager.DbManager;
import com.ashenman.byl.dbtest.R;
import com.ashenman.byl.dbtest.modle.User;
import com.zhy.autolayout.AutoLayoutActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by android on 2016/9/6.
 */
public class MainActivity extends AutoLayoutActivity implements View.OnClickListener {
    private List<User> currentList;
    private DbManager db;

    private Button bt1, bt2, bt3, bt4, bt5, bt6;
    private EditText et1, et2, et3, et4, et5;
    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        findview();
        creatDBManaer();
    }

    public void creatDBManaer() {
        db = new DbManager(MainActivity.this, User.class);
    }

    public void findview() {
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        bt5 = (Button) findViewById(R.id.bt5);
        bt6 = (Button) findViewById(R.id.bt6);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);


        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);

        tv = (TextView) findViewById(R.id.tv);
    }

    //    Class<T> clazz, String select, String[] selectArgs
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findByString(User.class, "name", editToStr(et5));
                showList(currentList);
                break;
            case R.id.bt2:
                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findAll(User.class);
                showList(currentList);
                break;
            case R.id.bt3:
                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findByInt(User.class, "idnum", editToInt(et5));
                showList(currentList);
                break;
            case R.id.bt4:
                User user = new User();
                user.setName(editToStr(et1));
                user.setAge(editToInt(et2));
                user.setSchool(editToStr(et3));
                user.setIdnum(editToInt(et4));
                db.add(user);

                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findAll(User.class);
                showList(currentList);
                break;
            case R.id.bt5:
                db.delete("name like ?", editToStr(et5));
                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findAll(User.class);
                showList(currentList);
//                db.delete("idnum=?", 111 + "");
                break;
            case R.id.bt6:
                db.update("name",editToStr(et5), "idnum>", 100);
                if (null != currentList) {
                    currentList.clear();
                }
                currentList = db.findAll(User.class);
                showList(currentList);
                break;
        }
    }

    public void showList(List list) {
        tv.setText("");
        StringBuffer buffer = new StringBuffer();
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                buffer.append(list.get(i).toString());
            }
            tv.setText(buffer.toString());
        }
    }

    public int StrToInt(String s) {
        return Integer.parseInt(s == null ? "0" : s);
    }

    public int editToInt(EditText e) {
        String s = e.getText().toString();
        return Integer.parseInt(parseStr(s));
    }

    public String editToStr(EditText e) {
        String s = e.getText().toString();
        return parseStr(s);
    }
    public String parseStr(String s){
        String str="";
        if(s==null||s.equals("")){
            return "0";
        }
        str=s;
        return str;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
