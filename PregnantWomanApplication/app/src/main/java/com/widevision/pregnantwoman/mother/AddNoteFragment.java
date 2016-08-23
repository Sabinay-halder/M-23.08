package com.widevision.pregnantwoman.mother;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newtrainee on 3/8/15.
 */
public class AddNoteFragment extends HideKeyFragment implements Validator.ValidationListener {

    @NotEmpty(message = "Enter your note.")
    @Bind(R.id.noteEdt)
    EditText mNoteEdt;
    @Bind(R.id.saveBt)
    ImageView mSaveBtn;
    @NotEmpty(message = "Enter title.")
    @Bind(R.id.titleEdt)
    EditText mTitleEdt;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_note_activity, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        String note = mNoteEdt.getText().toString().trim();
        String title = mTitleEdt.getText().toString().trim();
        ActiveAndroidDBHelper dbHelper = ActiveAndroidDBHelper.getInstance();
        dbHelper.addNote("", note, title, Constant.getCurrentTime());
        Constant.tagForNoteFragment = 1;
        MainFragmentActivity.fragmentManager.popBackStack();
        Constant.setAlert(getActivity(), "Note added successfully.");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }
}
