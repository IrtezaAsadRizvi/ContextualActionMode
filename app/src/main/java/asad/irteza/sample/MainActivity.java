package asad.irteza.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] groceries =new String[]{
            "potato","mango","salt","milk","cereal","chicken","flower"
    };
    ArrayList<String> groceries_list = new ArrayList<String>();
    ArrayAdapter<String> list_adapter;
    List selections = new ArrayList();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        for(String grocery:groceries){
            groceries_list.add(grocery);
        }
        list_adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.row_layout,R.id.row_item,groceries_list);
        listView.setAdapter(list_adapter);
        registerForContextMenu(listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            //when selecting item from list
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if (b == true){
                    selections.add(groceries_list.get(i));

                    count++;
                    actionMode.setTitle(count+" items selected");
                }else {
                    selections.remove(groceries_list.get(i));
                    count--;
                    actionMode.setTitle(count+" items selected");
                }
            }
            //inflating menu in onCreate
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_mode_layout,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            //context menu options
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.delete_mode){
                    for (Object item: selections){
                        String ITEM = item.toString();
                        groceries_list.remove(ITEM);
                    }
                }
                list_adapter.notifyDataSetChanged();
                actionMode.finish();
                return true;
            }
            //reseting everything when destroyed
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                count = 0;
                selections.clear();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.simple_contextual_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                groceries_list.remove(info.position);
                list_adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
