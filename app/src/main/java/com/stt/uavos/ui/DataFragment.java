package com.stt.uavos.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.stt.uavos.R;
import com.stt.uavos.adapter.TaskAdapter;
import com.stt.uavos.db.UAVOSDB;
import com.stt.uavos.model.Mission;
import com.stt.uavos.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 数据界面
 */

public class DataFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DataFragment";
    private ListView listview;
    private List<Task> taskList;
    private UAVOSDB uavosdb;
    private TaskAdapter adapter;
    private View view;
    private Button btn_delete_all,btn_query_all_task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_data_fragment, container, false);

        listview = (ListView) view.findViewById(R.id.lv_task_list);
        btn_delete_all = (Button) view.findViewById(R.id.btn_delete_all);
        btn_query_all_task = (Button) view.findViewById(R.id.btn_query_all_task);
        btn_delete_all.setOnClickListener(this);
        btn_query_all_task.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllTask();
        adapter = new TaskAdapter(getActivity(), R.layout.layout_item_task, taskList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = taskList.get(position);
                String taskId = task.getTaskId();
                List<Mission> missionList = uavosdb.loadMission(taskId);
                for (Mission mission : missionList) {
                    Log.e(TAG + "任务数据表中的数据1：", mission.getData());
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                final Task task = taskList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认删除任务:" + task.getTaskName());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getActivity(), "点击了确定按钮", Toast.LENGTH_SHORT).show();
                        taskList.remove(position);//选择行的位置
                        uavosdb.deleteOneTask(task.getTaskName());
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete_all:
                //TODO===删除任务时的提示：
                showAlertDialog();
                break;
            case R.id.btn_query_all_task:
                getAllTask();
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
                break;
        }
    }

    public void getAllTask() {
        if(taskList == null) {
            taskList = new ArrayList<Task>();
        }
        if (taskList.size() > 0) {
            taskList.clear();
        }
        uavosdb = UAVOSDB.getInstance(getActivity());
        taskList = uavosdb.loadAllTask();
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除所有任务");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "点击了确定按钮", Toast.LENGTH_SHORT).show();
                uavosdb.deleteAllTask();
                taskList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "点击了取消按钮", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
