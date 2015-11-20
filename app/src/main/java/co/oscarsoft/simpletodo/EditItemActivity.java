package co.oscarsoft.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import simpletodo.oscarsoft.co.simpletodo.R;


public class EditItemActivity extends AppCompatActivity {

    final static String L_TASK_INDEX = "TASK_IDX";
    final static String L_TASK_NAME = "TASK_NAME";

    private String taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String taskname = getIntent().getStringExtra(EditItemActivity.L_TASK_NAME);
        taskIndex = getIntent().getStringExtra(EditItemActivity.L_TASK_INDEX);
        EditText etSaveItem = (EditText)findViewById(R.id.etSaveItem);
        etSaveItem.setText(taskname);
    }

    public void onSaveItem(View v) {
        EditText etSaveItem = (EditText)findViewById(R.id.etSaveItem);
        String itemText = etSaveItem.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EditItemActivity.L_TASK_INDEX, taskIndex);
        returnIntent.putExtra(EditItemActivity.L_TASK_NAME, itemText);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
