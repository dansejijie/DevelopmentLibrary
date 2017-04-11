//package com.hyphenate.chatuidemo;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.ViewGroup;
//
//import com.netease.nim.uikit.constant.Extras;
//import com.netease.nim.uikit.widget.MessageView;
//
///**
// * Created by dansejijie on 17/4/11.
// */
//
//public class P2PMessageActivity extends Activity{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        MessageView messageView=new MessageView(this);
//
//
//        Bundle bundle=new Bundle();
//        bundle.putString(Extras.EXTRA_TOIMID,"dansejijie2");
//        bundle.putString(Extras.EXTRA_TONICKNAME,"dansejijie2");
//        bundle.putString(Extras.EXTRA_MYNICKNAME,"dansejijie");
//        messageView.setArguments(bundle);
//
//        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        messageView.setLayoutParams(layoutParams);
//
//        setContentView(messageView);
//    }
//}
