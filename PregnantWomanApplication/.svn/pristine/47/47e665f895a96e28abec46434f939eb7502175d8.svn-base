package com.widevision.pregnantwoman.Bean;//package com.widevision.pregnantwoman;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
//import com.widevision.pregnantwoman.database.BabyInfoTable;
//import com.widevision.pregnantwoman.model.HideKeyActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by mercury-two on 22/8/15.
// */
//public class BabyNameList extends HideKeyActivity {
//
//    @Bind(R.id.lvbaby)
//    ListView babyList;
//    @Bind(R.id.LLladdnew)
//    LinearLayout babyAddnewde;
//    @Bind(R.id.babydialocancle)
//    TextView cancledBttn;
//
//
//    private ActiveAndroidDBHelper helper;
//    ArrayList<BabyNameBean> babynamebean = new ArrayList<BabyNameBean>();
//    String[] name;
//    private String nameofbaby,dob;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.baby_namelist_dialog);
//        ButterKnife.bind(BabyNameList.this);
//
//        helper = ActiveAndroidDBHelper.getInstance();
//
//
//        name = new String[]{"ana", "anki"};
//
//
//
//        for (int i = 0; i < name.length; i++) {
//            BabyNameBean b = new BabyNameBean();
//            b.setName(name[i]);
//            babynamebean.add(b);
//        }
//
//        BabyNameAdapter babyadap = new BabyNameAdapter(BabyNameList.this );
//        babyList.setAdapter(babyadap);
//
//
//        babyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                Intent in = new Intent(getApplicationContext(), MainFragmentActivity.class);
//                String itemClicked = name[position];
//                in.putExtra("name", itemClicked);
//                startActivity(in);
//
//            }
//        });
//
//
//        babyAddnewde.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dialog.dismiss();
//                Intent positveActivity = new Intent(BabyNameList.this, BabyHomeActivity.class);
//                startActivity(positveActivity);
//                finish();
//            }
//        });
//        cancledBttn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dialog.dismiss();
//            }
//        });
//    }
//
//
//    public class BabyNameAdapter extends BaseAdapter {
//
//        Activity content;
//        LayoutInflater inflat;
//
//
//        private BabyNameAdapter(Activity content) {
//            this.content = content;
//
//            inflat = (LayoutInflater) content
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//
//        @Override
//        public int getCount() {
//            return babynamebean.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return babynamebean.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup viewGroup) {
//
//            view = inflat.inflate(R.layout.baby_listdata, null);
//
//            TextView name = (TextView) view.findViewById(R.id.txtbaby1);
//            ImageView image = (ImageView) view.findViewById(R.id.imgbaby1);
//            final BabyNameBean be = babynamebean.get(position);
//
//            name.setText(be.getName());
//            image.setBackgroundResource(R.drawable.icn_4);
//
//            return view;
//        }
//    }
//
//
//}
