package com.widevision.pregnantwoman.mother;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.NoteRecordTable;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NotesListActivity extends HideKeyFragment {

    @Bind(R.id.addNoteBtn)
    ImageView mAddNoteBtn;
    @Bind(R.id.removeBtn)
    ImageView mRemoceBtn;
    @Bind(R.id.noteList)
    ListView mNoteList;
    @Bind(R.id.addNoteTxt)
    ImageView mAddNoteTxt;
    @Bind(R.id.addNoteTxtLayout)
    LinearLayout addNoteTxtLayout;
    @Bind(R.id.buttom)
    LinearLayout buttomLayout;
    @Bind(R.id.nameTxt)
    TextView nameTxt;

    private static int mRemoveTag = 0;

    private NoteAdapter noteAdapter;
    private List<NoteRecordTable> notesList;
    private ActiveAndroidDBHelper helper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list_activity, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);
        String motherNameStr = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");
        nameTxt.setText(motherNameStr);
        helper = ActiveAndroidDBHelper.getInstance();
        notesList = helper.viewAllNote();
        if (notesList != null && notesList.size() != 0) {
            mAddNoteTxt.setVisibility(View.GONE);
            addNoteTxtLayout.setVisibility(View.GONE);
            buttomLayout.setVisibility(View.VISIBLE);
            noteAdapter = new NoteAdapter(getActivity());
            mNoteList.setAdapter(noteAdapter);
        } else {
            AddNoteFragment myf = new AddNoteFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
            transaction.replace(R.id.parent_noteList, myf);
            transaction.addToBackStack(null);
            transaction.commit();
            mAddNoteTxt.setVisibility(View.VISIBLE);
            addNoteTxtLayout.setVisibility(View.VISIBLE);
            buttomLayout.setVisibility(View.GONE);
        }

        mAddNoteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    AddNoteFragment myf = new AddNoteFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.parent_noteList, myf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        mAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    AddNoteFragment myf = new AddNoteFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.parent_noteList, myf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        mRemoceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemoveTag = 1;
                notesList = helper.viewAllNote();
                noteAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    class NoteAdapter extends BaseAdapter {


        private final LayoutInflater inflater;
        private Holder holder;

        NoteAdapter(Context context) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new Holder();
        }

        @Override
        public int getCount() {
            return notesList.size();
        }

        @Override
        public Object getItem(int i) {
            return notesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.note_list_row_new, viewGroup, false);

            holder.noteTxt = (TextView) view.findViewById(R.id.rowNoteTxt);
            holder.deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            holder.noteTxt.setText(notesList.get(i).title);
            if (mRemoveTag == 1) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            }
            holder.noteTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Constant.buttonEnable) {
                        Constant.setButtonEnable();
//                        viewNoteDialog(notesList.get(i));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("value", notesList.get(i));
                        Constant.setButtonEnable();
                        EditNoteFragment myf = new EditNoteFragment();
                        myf.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                        transaction.add(R.id.parent_noteList, myf, Constant.TAGFRAGMENT);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog1 = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.dialog_view);
                    dialog1.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LinearLayout linearLayout = (LinearLayout) dialog1.findViewById(R.id.dialog_no_layout);
                    TextView message = (TextView) dialog1.findViewById(R.id.dialog_message);
                    TextView okBtn = (TextView) dialog1.findViewById(R.id.dialog_ok);
                    TextView cancle = (TextView) dialog1.findViewById(R.id.dialog_no);
                    cancle.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    message.setText(getResources().getString(R.string.note_delete_str));
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //delete entry from database

                            helper.deleteNote(notesList.get(i).getId());
                            dialog1.dismiss();
                            updateNotesList();
                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();

                        }
                    });
                    dialog1.show();
                }
            });
            return view;
        }

        class Holder {
            TextView noteTxt;
            ImageView deleteBtn;
        }
    }

    void updateNotesList() {
        mRemoveTag = 0;
        notesList = helper.viewAllNote();
        noteAdapter.notifyDataSetChanged();
        if (notesList != null && notesList.size() != 0) {
            mAddNoteTxt.setVisibility(View.GONE);
            addNoteTxtLayout.setVisibility(View.GONE);
            buttomLayout.setVisibility(View.VISIBLE);
        } else {
            mAddNoteTxt.setVisibility(View.VISIBLE);
            addNoteTxtLayout.setVisibility(View.VISIBLE);
            buttomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRemoveTag = 0;
        if (Constant.tagForNoteFragment == 1) {
            Constant.tagForNoteFragment = 0;
            notesList = helper.viewAllNote();
            if (notesList != null && notesList.size() != 0) {
                mAddNoteTxt.setVisibility(View.GONE);
                addNoteTxtLayout.setVisibility(View.GONE);
                buttomLayout.setVisibility(View.VISIBLE);
                noteAdapter = new NoteAdapter(getActivity());
                mNoteList.setAdapter(noteAdapter);
            } else {
                mAddNoteTxt.setVisibility(View.VISIBLE);
                addNoteTxtLayout.setVisibility(View.VISIBLE);
                buttomLayout.setVisibility(View.GONE);
            }
        }
    }
}
