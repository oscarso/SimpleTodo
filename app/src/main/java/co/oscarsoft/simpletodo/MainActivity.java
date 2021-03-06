package co.oscarsoft.simpletodo;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import simpletodo.oscarsoft.co.simpletodo.R;




public class MainActivity extends AppCompatActivity {
    ArrayList<String>       items;
    ArrayList<Task>         tasks;
    ArrayAdapter<String>    itemsAdapter;
    ListView                lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupItemEditListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
    }

    private void setupItemEditListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(EditItemActivity.L_TASK_INDEX, ""+position);
                i.putExtra(EditItemActivity.L_TASK_NAME, items.get(position));
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int taskIndex = new Integer(data.getStringExtra(EditItemActivity.L_TASK_INDEX));
                String newTask = data.getStringExtra(EditItemActivity.L_TASK_NAME);
                items.set(taskIndex, newTask);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    private void readItems() {
        TaskDB taskDB = TaskDB.getInstance(MainActivity.this);
        tasks = taskDB.getTasks();
        if (tasks.size() > 0) {
            items = new ArrayList<String>();
            for (int i=0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                items.add(task.getTaskName());
            }
        } else {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        if (items.size() == 0) {
            return;
        }

        try {
            TaskDB taskDB = TaskDB.getInstance(MainActivity.this);
            taskDB.onRecreate();
            tasks = new ArrayList<Task>();
            for (int i=0; i < items.size(); i++) {
                Task task = new Task(items.get(i));
                tasks.add(task);
            }
            taskDB.setTasks(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
