package com.widevision.pregnantwoman.mother;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.NoteRecordTable;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newtrainee on 4/9/15.
 */
public class EditNoteFragment extends HideKeyFragment {

    private View view;
    @Bind(R.id.editBtn)
    ImageView editBtn;
    @Bind(R.id.saveBt)
    ImageView saveBtn;
    @Bind(R.id.cancelBt)
    ImageView cancelBtn;

    @Bind(R.id.noteEdt)
    EditText noteEdt;
    @Bind(R.id.titleEdt)
    EditText noteTitleEdt;
    @Bind(R.id.noteTxt)
    TextView noteTxt;
    @Bind(R.id.titleTxt)
    TextView noteTitleTxt;

    NoteRecordTable note;
    private ActiveAndroidDBHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_note_dialog, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);
        Constant.tagForNoteFragment = 1;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            note = (NoteRecordTable) bundle.getSerializable("value");
        }
        helper = ActiveAndroidDBHelper.getInstance();
        noteEdt.setText(note.note);


        noteTxt.setText(note.note);
        noteTitleEdt.setText(note.title);
        noteTitleTxt.setText(note.title);
        noteTxt.setMovementMethod(new ScrollingMovementMethod());
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteTxt.setVisibility(View.GONE);
                noteTitleTxt.setVisibility(View.GONE);
                noteEdt.setVisibility(View.VISIBLE);
                noteTitleEdt.setVisibility(View.VISIBLE);

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update entry in database
                String notestr = noteEdt.getText().toString().trim();
                String title = noteTitleEdt.getText().toString().trim();
                if (!notestr.equals("") && !title.equals("")) {
                    Constant.tagForNoteFragment = 1;
                    helper.updateNote("", notestr, title, Constant.getCurrentTime(), note.getId());
                    MainFragmentActivity.fragmentManager.popBackStack();
                } else if (notestr.equals("")) {
                    Constant.setAlert(getActivity(), "Please add your Note.");
                } else {
                    Constant.setAlert(getActivity(), "Title required.");
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentActivity.fragmentManager.popBackStack();
            }
        });
        return view;
    }
}
