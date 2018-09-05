package com.example.yasin.taksmssender.Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Class.Message;
import com.example.yasin.taksmssender.EditActivity;
import com.example.yasin.taksmssender.Fragment.EditSmsFragment;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import java.util.List;

public class RecyclerAdapterSms extends RecyclerView.Adapter<RecyclerAdapterSms.MyViewHolder> {

    String contentLength;
    private static final String Tag = RecyclerAdapterSms.class.getSimpleName();
    private List<LandScape> mData;
    private LayoutInflater mInflater;
    private Context context;

    public RecyclerAdapterSms(Context context, List<LandScape> data) {
        this.mData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(Tag, "onCreateViewHolder");
        View view = mInflater.inflate(R.layout.list_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(Tag, "onBindViewHolder" + position);

        LandScape currentObj = mData.get(position);
        holder.setData(currentObj, position, context);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView des;
        ImageView delete, edit;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtTitle);
            des = (TextView) itemView.findViewById(R.id.txtDes);
            delete = (ImageView) itemView.findViewById(R.id.img_row_delete);
            edit = (ImageView) itemView.findViewById(R.id.img_row_edit);
        }

        public void setData(final LandScape currentObj, int position, final Context context) {
            this.title.setText(currentObj.getTitle());
            this.des.setText(currentObj.getDescription());
            this.position = position;

            final SQLiteOpenHelper database = new SQLiteOpenHelper(context);

            this.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message.alert_msg(context, "حذف این آیتم", "آیا قصد حذف کردن این آیتم رو دارید؟", currentObj.getId());
                }
            });

            this.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final AlertDialog dialog;
                    View mView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_edit_data, null);
                    final EditText edtSubject = (EditText) mView.findViewById(R.id.edtSubjectAlertDialog);
                    final EditText edtContent = (EditText) mView.findViewById(R.id.edtContentAlertDialog);
                    final TextView txtChar = (TextView) mView.findViewById(R.id.txtChar);
                    txtChar.setText(edtContent.getText().length() + "");
                    Button button = (Button) mView.findViewById(R.id.btnDoneAlertDialog);
                    Button btnExit = (Button) mView.findViewById(R.id.btnExitAlertDialog);
                    edtContent.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            contentLength = edtContent.getText().length() + "";

                            txtChar.setText(contentLength);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    edtSubject.setText(currentObj.getTitle());
                    edtContent.setText(currentObj.getDescription());
                    builder.setView(mView);
                    dialog = builder.create();
                    dialog.show();

                    btnExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String subject = edtSubject.getText().toString().trim();
                            String content = edtContent.getText().toString().trim();
                            long check;
                            if (subject.isEmpty() || content.isEmpty()) {
                                Toast.makeText(context, "فیلدی نمی تواند خالی باشد.", Toast.LENGTH_SHORT).show();
                            } else {
                                SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(view.getContext());

                                SQLiteDatabase database1 = openHelperTak.getWritableDatabase();
                                ContentValues cv = new ContentValues();
                                cv.put(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS, subject);
                                cv.put(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS, content);
                                check = database1.update(SmsContentContract.SmsEntry.TABLE_NAME_SMS, cv, SmsContentContract.SmsEntry._ID +"==?", new String[]{currentObj.getId()});
                                if (check > 0) {
                                    ((EditActivity)context).sayHi();
                                    Toast.makeText(view.getContext(), "اطلاعات با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(view.getContext(), "خطایی رخ داده است.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        }
                    });

                }
            });
        }
    }
}

