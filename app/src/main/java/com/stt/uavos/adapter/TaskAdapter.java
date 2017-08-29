package com.stt.uavos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stt.uavos.R;
import com.stt.uavos.model.Task;

import java.util.List;

/**
 * @ description:
 * @ time: 2017/8/24.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class TaskAdapter extends ArrayAdapter<Task> {

    private int resourceId;

    public TaskAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Task task = getItem(position);
        View  view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_task_name = (TextView)view.findViewById(R.id.tv_task_name);
            viewHolder.tv_probe_type = (TextView)view.findViewById(R.id.tv_probe_type);
            viewHolder.tv_task_mode = (TextView)view.findViewById(R.id.tv_task_mode);
            viewHolder.tv_start_time = (TextView)view.findViewById(R.id.tv_start_time);
            viewHolder.tv_end_time = (TextView)view.findViewById(R.id.tv_end_time);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(task.getTaskName() !=null) {
            viewHolder.tv_task_name.setText(task.getTaskName());
        }
        if(task.getProbeType() != null) {
            viewHolder.tv_probe_type.setText(task.getProbeType());
        }
        if(task.getTaskMode() != null) {
            viewHolder.tv_task_mode.setText(task.getTaskMode());
        }
        if(task.getStartTime() != null) {
            viewHolder.tv_start_time.setText(task.getStartTime());
        }
        if(task.getEndTime() != null) {
            viewHolder.tv_end_time.setText(task.getEndTime());
        }
        return view;
    }

    class ViewHolder{
        TextView tv_task_name,tv_probe_type,tv_task_mode,tv_start_time,tv_end_time;
    }
}
