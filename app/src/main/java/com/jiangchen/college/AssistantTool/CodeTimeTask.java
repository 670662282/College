package com.jiangchen.college.AssistantTool;

import android.os.AsyncTask;
import android.widget.TextView;

import com.jiangchen.college.R;

/**
 * Created by Dell- on 2015/12/6 0006.
 * 按钮倒计时
 */
public class CodeTimeTask extends AsyncTask<Void, Void, Void> {

    private int time = 60;

    private TextView textView;
    private static CodeTimeTask codeTimeTask;

    private static boolean isNew;
    private static boolean isRun;



    //构造私有化
    private CodeTimeTask() {
    }

    public void startTimer(TextView textView) {
        //
        this.textView = textView;
        if (isNew) {
            execute();
        }
    }

    public synchronized static CodeTimeTask getInstance() {

        if (codeTimeTask == null) {
            isNew = true;
            codeTimeTask = new CodeTimeTask();

        }
        return codeTimeTask;
    }


    @Override
    protected void onPreExecute() {
        time = 60;
        isNew = false;
        isRun = true;
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (; time > 0; time--) {
            publishProgress();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {

        if (textView != null) {
            if (textView.isEnabled()) {
                textView.setEnabled(false);
            }
            textView.setText(time+"s");
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        end();
    }

    public void end() {
        if (textView != null) {
            textView.setEnabled(true);
            textView.setText(R.string.get_code);
        }

        taskCancel();
    }

    public void taskCancel() {
        if (codeTimeTask != null) {
            cancel(true);
            codeTimeTask = null;
            isNew = true;
            isRun = false;
        }
    }

    public static boolean isRun() {
        return isRun;
    }

}
